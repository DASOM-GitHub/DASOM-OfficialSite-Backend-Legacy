package dmu.dasom.dasom_homepage.service.logout;

import dmu.dasom.dasom_homepage.exception.LogoutErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LogoutService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public LogoutService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void logout(String authorization) {
        // 헤더 데이터가 비어있을 경우 예외 발생
        if (authorization == null)
            throw new LogoutErrorException();

        // SecurityContext에서 사용자 아이디를 가져옴
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Redis에서 토큰 삭제
        redisTemplate.delete("ACCESS_TOKEN_" + username);
        redisTemplate.delete("REFRESH_TOKEN_" + username);

        // 헤더 데이터에서 액세스 토큰을 추출하여 블랙리스트에 추가
        addToBlacklist(authorization.split(" ")[1]);
    }

    // 블랙리스트에 추가
    private void addToBlacklist(String accessToken) {
        redisTemplate.opsForValue().set("BLACKLIST_" + accessToken, "true", 30, TimeUnit.MINUTES);
    }

}
