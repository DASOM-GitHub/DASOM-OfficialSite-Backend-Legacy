package dmu.dasom.dasom_homepage.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dmu.dasom.dasom_homepage.auth.jwt.JwtUtil;
import dmu.dasom.dasom_homepage.auth.userdetails.CustomUserDetails;
import dmu.dasom.dasom_homepage.domain.login.LoginDTO;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// formLogin을 비활성 했기 때문에 직접 UsernamePasswordAuthenticationFilter를 커스텀 함
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RedisTemplate<String, String> redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 로그인 데이터를 객체화 시킴
            ObjectMapper objectMapper = new ObjectMapper();
            LoginDTO loginDTO = objectMapper.readValue(request.getInputStream(), LoginDTO.class);

            // 클라이언트로부터 온 요청에서 username, password 파라미터 추출
            String username = loginDTO.getUsername();
            String password = loginDTO.getPassword();

            // 검증을 위해 token으로 만듦
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

            // token 검증을 위해 AuthenticationManager로 전달
            return authenticationManager.authenticate(authToken);
        } catch (InternalAuthenticationServiceException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 로그인 성공 핸들러
    // jwt 생성 및 반환
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String username = customUserDetails.getUsername();

        // 동시 로그인을 방지하기 위해 사용자의 기존 토큰을 만료 시킴
        if (Boolean.TRUE.equals(redisTemplate.hasKey("ACCESS_TOKEN_" + username)))
            jwtUtil.expireToken(redisTemplate.opsForValue().get("ACCESS_TOKEN_" + username));

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        // 엑세스 토큰 유효시간 : 30m
        String accessToken = jwtUtil.createJwt(username, role, 60 * 30 * 1000L);
        // 리프레시 토큰 유효시간 : 6h
        String refreshToken = jwtUtil.createJwt(username, role, 60 * 60 * 6 * 1000L);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", "Bearer " + accessToken);
        responseBody.put("refreshToken", "Bearer " + refreshToken);
        responseBody.put("memberRole", role);

        // 토큰 반환
        ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>(true, responseBody);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
        
        // Redis에 저장
        redisTemplate.opsForValue().set("ACCESS_TOKEN_" + customUserDetails.getUsername(), accessToken, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("REFRESH_TOKEN_" + customUserDetails.getUsername(), refreshToken, 6, TimeUnit.HOURS);
    }

    // 로그인 실패 핸들러
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // HttpStatus.UNAUTHORIZED
        response.setStatus(401);
    }
}
