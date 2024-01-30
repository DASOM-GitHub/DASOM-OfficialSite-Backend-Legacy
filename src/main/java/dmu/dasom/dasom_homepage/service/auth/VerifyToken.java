package dmu.dasom.dasom_homepage.service.auth;

import dmu.dasom.dasom_homepage.auth.jwt.JwtUtil;
import dmu.dasom.dasom_homepage.exception.AccessTokenExpiredException;
import dmu.dasom.dasom_homepage.exception.UnAuthorizedAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerifyToken {

    private final JwtUtil jwtUtil;

    @Autowired
    public VerifyToken(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 헤더 데이터에서 토큰을 추출해 유효 상태 검증 및 반환
    public String verifyToken(String authorization) {
        // 헤더 데이터가 비어있을 경우 예외 발생
        if (authorization == null)
            throw new UnAuthorizedAccessException();

        // 헤더 데이터에서 토큰만 추출
        String accessToken = authorization.split(" ")[1];

        // 이미 만료 된 토큰일 경우 예외 발생
        if (jwtUtil.isExpired(accessToken))
            throw new AccessTokenExpiredException();

        return accessToken;
    }
}
