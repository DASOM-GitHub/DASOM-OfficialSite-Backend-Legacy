package dmu.dasom.dasom_homepage.controller.signup;

import dmu.dasom.dasom_homepage.domain.member.SignupDTO;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import dmu.dasom.dasom_homepage.service.signup.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/signup")
@PreAuthorize("permitAll()")
public class SignupController {

    private final SignupService signupService;

    @Autowired
    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }


    // 부원 인증 프로세스
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyNewMember(@RequestBody Map<String, String> reqBody) {
        signupService.verifyNewMember(reqBody.get("uniqueCode"));
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    // 회원 가입 프로세스
    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> signupProc(@RequestBody SignupDTO newMember) {
        signupService.saveNewMember(newMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true));
    }

}
