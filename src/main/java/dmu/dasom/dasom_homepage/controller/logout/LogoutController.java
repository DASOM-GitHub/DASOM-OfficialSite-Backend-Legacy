package dmu.dasom.dasom_homepage.controller.logout;

import dmu.dasom.dasom_homepage.restful.ApiResponse;
import dmu.dasom.dasom_homepage.service.logout.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
@PreAuthorize("isAuthenticated()")
public class LogoutController {

    private final LogoutService logoutService;

    @Autowired
    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        // 토큰 관련 헤더 데이터 추출 및 로그아웃 처리
        logoutService.logout(request.getHeader("Authorization"));

        // HTTP 200 OK 상태 코드 반환
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

}
