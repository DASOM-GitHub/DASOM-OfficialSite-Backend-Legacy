package dmu.dasom.dasom_homepage.controller.signup;

import dmu.dasom.dasom_homepage.domain.member.DasomMember;
import dmu.dasom.dasom_homepage.service.signup.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/signup")
public class SignupController {

    private final SignupService signupService;

    @Autowired
    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @PostMapping("/verify")
    public ResponseEntity<Object> verifyNewMember(@RequestBody Map<String, Object> paramMap) {
        System.out.println("uniqueCode ==> " + paramMap);

        // 인증 여부 확인
        boolean isAuthenticated = signupService.verifyNewMember(paramMap.get("uniqueCode").toString());

        if (isAuthenticated) {
            return ResponseEntity.ok("부원 인증 성공");
        } else {
            return ResponseEntity.ok("부원 인증 실패");
        }
    }

    // 회원가입 프로세스 테스트 용
    @PostMapping()
    public ResponseEntity<String> signupProc(DasomMember newMember) {
        System.out.println("newMember => " + newMember);
        if (signupService.saveNewMember(newMember))
            return ResponseEntity.ok("부원 가입 성공");
        else
            return ResponseEntity.ok("부원 가입 실패");
    }

}
