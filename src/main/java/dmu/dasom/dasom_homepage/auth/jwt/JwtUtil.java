package dmu.dasom.dasom_homepage.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final StringRedisTemplate stringRedisTemplate;

    //생성자 함수 Spring에서 주입된 JWT 시크릿키를 이용해 SecretKey를 생성
    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey, StringRedisTemplate stringRedisTemplate) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //주어진 토큰에서 'username'을 추출
    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    //주어진 토큰에서 'role'을 추출
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    //토큰의 유효기간이 만료되었는지 확인
    public Boolean isExpired(String token) {
        try {
            Date expiration = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration();

            Date now = new Date();

            return expiration.before(now);
        } catch (ExpiredJwtException ex) { // 토큰 만료 시 예외 발생함
            return true;
        }
    }

    // jwt 토큰 생성
    public String createJwt(String username, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    // 헤더 데이터에서 토큰을 추출하는 메소드
    public String parseToken(String authorization) {
        return authorization.split(" ")[1];
    }

    // 토큰의 블랙리스트 등재 여부 확인
    public boolean isBlacklisted(String accessToken) {
        return Optional.ofNullable(stringRedisTemplate)
                .map(template -> template.hasKey("BLACKLIST_" + accessToken))
                .orElse(false);
    }

    // 클라이언트로부터 온 리프레시 토큰과 Redis 속 리프레시 토큰을 비교
    public boolean verifyRefreshToken(String refreshToken) {
        if (isExpired(refreshToken))
            return false;

        String refreshTokenRedis = Optional.ofNullable(stringRedisTemplate)
                .map(template -> template.opsForValue().get("REFRESH_TOKEN_" + getUsername(refreshToken)))
                .orElse(null);
        return refreshTokenRedis != null && refreshTokenRedis.equals(refreshToken);
    }

    // 모든 검증이 끝나고 새로운 액세스 토큰과 리프레시 토큰을 발급하여 반환
    public Map<String, String> createNewTokens(String refreshToken) {
        String username = getUsername(refreshToken);
        String role = getRole(refreshToken);

        String newAccessToken = createJwt(username, role, 60 * 30 * 1000L);
        String newRefreshToken = createJwt(username, role, 60 * 60 * 6 * 1000L);

        stringRedisTemplate.opsForValue().set("ACCESS_TOKEN_" + username, newAccessToken, 30, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set("REFRESH_TOKEN_" + username, newRefreshToken, 6, TimeUnit.HOURS);

        Map<String, String> newTokens = new HashMap<>();
        newTokens.put("accessToken", "Bearer " + newAccessToken);
        newTokens.put("refreshToken", "Bearer " + newRefreshToken);
        return newTokens;
    }

    // 토큰 만료 메소드
    public void expireToken(String accessToken) {
        // 토큰에서 사용자 아이디를 가져옴
        String username = getUsername(accessToken);

        // Redis에서 토큰 삭제
        stringRedisTemplate.delete("ACCESS_TOKEN_" + username);
        stringRedisTemplate.delete("REFRESH_TOKEN_" + username);

        // 토큰을 블랙리스트에 추가
        addToBlacklist(accessToken);
    }

    // 블랙리스트 추가 메소드
    public void addToBlacklist(String accessToken) {
        stringRedisTemplate.opsForValue().set("BLACKLIST_" + accessToken, "true", 30, TimeUnit.MINUTES);
    }

}
