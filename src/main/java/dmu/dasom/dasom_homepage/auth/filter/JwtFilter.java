package dmu.dasom.dasom_homepage.auth.filter;

import dmu.dasom.dasom_homepage.auth.jwt.JwtUtil;
import dmu.dasom.dasom_homepage.auth.userdetails.CustomUserDetails;
import dmu.dasom.dasom_homepage.domain.member.DasomMember;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Authorization 헤더를 찾아 검증한다
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("!! Token is null !!");
            filterChain.doFilter(request, response);

            return;
        }

        System.out.println("** authorization processing **");

        // 헤더 데이터에서 토큰만 추출
        String token = authorization.split(" ")[1];

        // 토큰 만료 여부 검증
        if (jwtUtil.isExpired(token)) {
            System.out.println("!! Token is expired !!");
            filterChain.doFilter(request, response);

            return;
        }

        // 토큰에서 username(이메일)과 role을 가져옴
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

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
