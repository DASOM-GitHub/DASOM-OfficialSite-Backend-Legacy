package dmu.dasom.dasom_homepage.controller.mypage;

import dmu.dasom.dasom_homepage.domain.member.MyPageDTO;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import dmu.dasom.dasom_homepage.service.mypage.MyPageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/members")
@PreAuthorize("isAuthenticated()")
public class MyPageController {

    private final MyPageService myPageService;

    @Autowired
    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    // 마이페이지 정보를 가져옴
    @GetMapping("/my-page")
    public ResponseEntity<ApiResponse<MyPageDTO>> getMemInfo(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, myPageService.getMemInfo(request.getHeader("Authorization"))));
    }

    // 비밀번호 변경
    @PostMapping("/my-page")
    public ResponseEntity<ApiResponse<Void>> updateMemPw(@RequestBody Map<String, String> reqBody, HttpServletRequest request) {
        myPageService.updateMemPw(reqBody, request.getHeader("Authorization"));
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    // 마이페이지 정보 업데이트
    @PutMapping("/my-page")
    public ResponseEntity<ApiResponse<Void>> updateMemInfo(@RequestBody MyPageDTO updateInfo, HttpServletRequest request) {
        myPageService.updateMemInfo(updateInfo, request.getHeader("Authorization"));
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    // 마이페이지 자신의 프로필 사진 업데이트
    @PatchMapping("/my-page")
    public ResponseEntity<ApiResponse<Void>> updateMemProfilePic(@RequestPart(value = "profilePic", required = false) MultipartFile profilePic, HttpServletRequest request) throws IOException {
        myPageService.updateMemProfilePic(profilePic, request.getHeader("Authorization"));
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

}
