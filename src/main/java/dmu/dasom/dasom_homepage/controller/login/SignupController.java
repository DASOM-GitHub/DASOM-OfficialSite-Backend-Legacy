package dmu.dasom.dasom_homepage.controller.login;

import dmu.dasom.dasom_homepage.domain.member.DasomMember;
import dmu.dasom.dasom_homepage.service.login.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class SignupController {

    private final SignupService signupService;

    @Autowired
    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    // 회원가입 프로세스 테스트 용
    @PostMapping()
    public String joinProc(DasomMember newMember) {
        if (signupService.saveNewMember(newMember))
            return "success";
        else
            return "fail";
    }

}
