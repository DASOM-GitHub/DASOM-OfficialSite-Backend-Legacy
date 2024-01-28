package dmu.dasom.dasom_homepage.service.logout;

import dmu.dasom.dasom_homepage.auth.jwt.JwtUtil;
import dmu.dasom.dasom_homepage.exception.AccessTokenExpiredException;
import dmu.dasom.dasom_homepage.exception.UnAuthorizedAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    @Autowired
    public LogoutService(RedisTemplate<String, String> redisTemplate, JwtUtil jwtUtil) {
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }

    public void logout(String authorization) {
        // 헤더 데이터가 비어있을 경우 예외 발생
        if (authorization == null)
            throw new UnAuthorizedAccessException();

        // 헤더 데이터에서 토큰만 추출
        String accessToken = authorization.split(" ")[1];

        // 이미 만료 된 토큰일 경우 예외 발생
        if (jwtUtil.isExpired(accessToken))
            throw new AccessTokenExpiredException();

        // Redis에서 토큰 삭제
        redisTemplate.delete("JWT_TOKEN_" + jwtUtil.getUsername(accessToken));
    }
}
