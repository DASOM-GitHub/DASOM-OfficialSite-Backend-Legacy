package dmu.dasom.dasom_homepage.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    // 접근 권한 테스트 용
    @GetMapping()
    public String adminControllerTest() {
        return "Admin Controller";
    }
}
