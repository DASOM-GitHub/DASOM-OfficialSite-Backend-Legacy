package dmu.dasom.dasom_homepage.service.logout;

import dmu.dasom.dasom_homepage.auth.jwt.JwtUtil;
import dmu.dasom.dasom_homepage.exception.LogoutErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

    private final JwtUtil jwtUtil;

    @Autowired
    public LogoutService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void logout(String authorization) {
        // 토큰 만료 처리
        jwtUtil.expireToken(jwtUtil.parseToken(authorization));
    }

}
