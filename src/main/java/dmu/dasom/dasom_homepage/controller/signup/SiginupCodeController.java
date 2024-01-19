package dmu.dasom.dasom_homepage.controller.signup;

import dmu.dasom.dasom_homepage.service.signup.UniqueCodeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/signup01")
@CrossOrigin(origins = "http://localhost:3000") // Adjust with your React frontend URL
public class SiginupCodeController {

    private final UniqueCodeService userService;
    public SiginupCodeController(UniqueCodeService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> axiosTest(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) throws SQLException {
        System.out.println("uniqueCode ==> " + paramMap);
        // 인증 여부 확인
        boolean isAuthenticated = userService.authenticateUser(paramMap);

        if (isAuthenticated) {
            return ResponseEntity.ok("부원 인증 성공");
        } else {
            return ResponseEntity.ok("부원 인증 실패");
        }
    }
}