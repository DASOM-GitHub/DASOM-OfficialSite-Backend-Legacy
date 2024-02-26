package dmu.dasom.dasom_homepage.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dmu.dasom.dasom_homepage.auth.jwt.JwtUtil;
import dmu.dasom.dasom_homepage.auth.userdetails.CustomUserDetails;
import dmu.dasom.dasom_homepage.domain.member.DasomMember;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;


    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    //HTTP 요청이 들어올 때마다 한 번씩 호출되는 함수입니다. 이 함수에서 JWT 인증을 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Authorization 헤더를 찾아 검증한다
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더 데이터에서 액세스 토큰만 추출
        String accessToken = jwtUtil.parseToken(authorization);

        if (jwtUtil.isBlacklisted(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 액세스 토큰 만료 여부 검증
        if (jwtUtil.isExpired(accessToken)) {
            String authorizationRefresh = request.getHeader("AuthorizationRefresh");
            if (authorizationRefresh == null || !authorizationRefresh.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            String refreshToken = jwtUtil.parseToken(authorizationRefresh);
            // 클라이언트의 리프레시 토큰과 Redis 속 리프레시 토큰의 일치 여부 검증
            if (!jwtUtil.verifyRefreshToken(refreshToken)) {
                // 리프레시 토큰이 만료되었거나 Redis 속 토큰과 일치하지 않을 경우 401 코드 반환
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            // 새로운 액세스 토큰과 리프레시 토큰을 발급하고 202 코드와 함께 클라이언트로 반환
            Map<String, String> newTokens = jwtUtil.createNewTokens(refreshToken);
            ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>(true, newTokens);
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
            return;
        }

        // 토큰에서 username(이메일)과 role을 가져옴
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        DasomMember member = new DasomMember();
        member.setMemEmail(username);
        member.setMemPassword("temppassword");
        member.setMemRole(role);

        // UserDetails에 회원 정보 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 이동
        filterChain.doFilter(request, response);
    }
}
