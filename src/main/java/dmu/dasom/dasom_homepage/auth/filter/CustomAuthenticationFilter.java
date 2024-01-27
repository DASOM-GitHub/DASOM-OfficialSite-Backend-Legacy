package dmu.dasom.dasom_homepage.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dmu.dasom.dasom_homepage.auth.jwt.JwtUtil;
import dmu.dasom.dasom_homepage.auth.userdetails.CustomUserDetails;
import dmu.dasom.dasom_homepage.domain.login.LoginDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

// formLogin을 비활성 했기 때문에 직접 UsernamePasswordAuthenticationFilter를 커스텀 함
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        // 토큰 유효시간 : 3H
        String token = jwtUtil.createJwt(username, role, 60 * 60 * 3 * 1000L);

        // RFC 7235 정의에 따라 아래와 같은 인증 헤더 형태를 가져야 한다
        response.addHeader("Authorization", "Bearer " + token);
    }

    // 로그인 실패 핸들러
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // HttpStatus.UNAUTHORIZED
        response.setStatus(401);
    }
}
