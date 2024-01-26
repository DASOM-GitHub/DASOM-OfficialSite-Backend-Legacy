package dmu.dasom.dasom_homepage.service.logout;

import dmu.dasom.dasom_homepage.auth.userdetails.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String loginId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // 클라이언트의 토큰 삭제
        response.setHeader("Authorization", "");

        // Redis에서 토큰 삭제
        redisTemplate.delete("JWT_TOKEN_" + loginId);

        // 시큐리티 컨텍스트 클리어
        SecurityContextHolder.clearContext();
    }
}
