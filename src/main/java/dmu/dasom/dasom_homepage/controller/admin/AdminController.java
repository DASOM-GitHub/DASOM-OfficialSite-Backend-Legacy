package dmu.dasom.dasom_homepage.controller.admin;

import dmu.dasom.dasom_homepage.domain.admin.MemberState;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import dmu.dasom.dasom_homepage.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    //회원 리스트 가져오기 (main)
    @GetMapping()
    public ResponseEntity<ApiResponse<List<MemberState>>> getMemberList(){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,adminService.getMemberList()));
    }

    //회원 정보 수정
    @PutMapping("/{memNo}")
    public ResponseEntity<ApiResponse<Void>> modifyMember(@PathVariable int memNo, @RequestBody MemberState memberState) {
        adminService.modify(memNo,memberState);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    //회원 정보 삭제
    @DeleteMapping("/{memNo}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable int memNo){
        adminService.delete(memNo);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    //회원 상태 수정
    @PatchMapping("/{memNo}")
    public ResponseEntity<ApiResponse<Void>> stateChangeMember(@PathVariable int memNo, @RequestBody MemberState memberState) {
        adminService.stateChange(memNo,memberState);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    //회원 이름 검색 리스트
    @GetMapping("/{memNo}")
    public ResponseEntity<ApiResponse<List<MemberState>>> searchMember(@PathVariable String memNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,adminService.searchMember(memNo)));
    }
}