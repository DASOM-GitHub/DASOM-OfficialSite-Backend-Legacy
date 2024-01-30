package dmu.dasom.dasom_homepage.service.logout;

import dmu.dasom.dasom_homepage.auth.jwt.InvalidTokenException;
import dmu.dasom.dasom_homepage.auth.jwt.JwtUtil;
import dmu.dasom.dasom_homepage.exception.AccessTokenExpiredException;
import dmu.dasom.dasom_homepage.exception.UnAuthorizedAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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

        // 로그아웃한 리프레시 토큰을 블랙리스트에 추가
        addToBlacklist(jwtUtil.getUsername(accessToken));
    }

    public String refreshToken(String refreshToken) {
        // 리프레시 토큰 유효성 검사
        if (jwtUtil.isExpired(refreshToken)|| isBlacklisted(jwtUtil.getUsername(refreshToken))) {
            throw new InvalidTokenException("Invalid refresh token.");
        }

        // 새 액세스 토큰 발급
        String newAccessToken = jwtUtil.refresh(refreshToken);

        // 새 액세스 토큰을 Redis에 저장
        redisTemplate.opsForValue().set("JWT_TOKEN_" + jwtUtil.getUsername(refreshToken), newAccessToken);

        return newAccessToken;
    }

    // 블랙리스트에 추가
    private void addToBlacklist(String username) {
        String key = "BLACKLIST_" + username;
        redisTemplate.opsForValue().set(key, "true");
        // 24시간(86400초) 후에 자동 삭제
        redisTemplate.expire(key, 24, TimeUnit.HOURS);
    }

    // 블랙리스트 확인
    private boolean isBlacklisted(String username) {
        return redisTemplate.hasKey("BLACKLIST_" + username);
    }
}
