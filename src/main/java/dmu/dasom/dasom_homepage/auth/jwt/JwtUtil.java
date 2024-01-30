package dmu.dasom.dasom_homepage.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JwtUtil {

    private final SecretKey secretKey;

    //생성자 함수 Spring에서 주입된 JWT 시크릿키를 이용해 SecretKey를 생성
    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
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



    public String refresh(String refreshToken) {
        // 리프레시 토큰 검증
        if (!isExpired(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token.");
        }

        String username = getUsername(refreshToken);

        // 새 엑세스 토큰 발급
        String newAccessToken = createJwt(username, "", 60 * 10 * 1000L);

        return newAccessToken;
    }

}
