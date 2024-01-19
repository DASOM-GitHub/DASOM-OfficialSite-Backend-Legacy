package dmu.dasom.dasom_homepage.controller.signup;

import dmu.dasom.dasom_homepage.domain.member.DasomMember;
import dmu.dasom.dasom_homepage.domain.recruit.DasomApplicant;
import dmu.dasom.dasom_homepage.service.signup.SignupMemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/signup02")
@CrossOrigin(origins = "http://localhost:3000") // Adjust with your React frontend URL
public class SiginupMemberController {
    @Autowired
    private SignupMemberService userService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> axiosTest(HttpServletRequest request, DasomMember paramMap) throws SQLException {
        System.out.println("SiginupMember ==> " + paramMap);

        userService.saveSignupMember(paramMap);
        return null;
    }
}
