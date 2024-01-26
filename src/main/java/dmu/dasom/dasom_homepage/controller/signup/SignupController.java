package dmu.dasom.dasom_homepage.controller.signup;

import dmu.dasom.dasom_homepage.domain.member.DasomMember;
import dmu.dasom.dasom_homepage.domain.member.DasomNewMember;
import dmu.dasom.dasom_homepage.service.signup.SignupService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/signup")
public class SignupController {

    private final SignupService signupService;

    @Autowired
    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @ResponseBody
    @PostMapping("/verify")
    public ResponseEntity<Object> verifyNewMember( @RequestBody DasomNewMember verifyReq) {
        System.out.println("uniqueCode ==> " + verifyReq);

        // 인증 여부 확인
        boolean isAuthenticated = signupService.verifyNewMember(verifyReq.getUniqueCode());

        if (isAuthenticated) {
            return ResponseEntity.ok("부원 인증 성공");
        } else {
            return ResponseEntity.ok("부원 인증 실패");
        }
    }

    // 회원가입 프로세스 테스트 용
    @PostMapping()
    public ResponseEntity<String> signupProc(@RequestBody DasomMember newMember) {
        // 기수 수동 삽입 (테스트)
        newMember.setMemRecNo(32);

        System.out.println("newMember => " + newMember.toString());
        if (signupService.saveNewMember(newMember))
            return ResponseEntity.ok("회원가입 성공");
        else
            return ResponseEntity.ok("회원가입 실패");
    }

}
