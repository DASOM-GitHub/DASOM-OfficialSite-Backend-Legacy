package dmu.dasom.dasom_homepage.controller.admin;

import dmu.dasom.dasom_homepage.domain.admin.MemberState;
import dmu.dasom.dasom_homepage.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/delete")
    public String delteMember(MemberState memberState) {
        adminService.delete(memberState);
        // admin 페이지로 이동
        return "/admin";
    }

    @GetMapping("/modify")
    public String modifyForm(MemberState memberState) {
        return "/modifty-form";
    }

    @PostMapping("/modify")
    public String modifyMember(MemberState memberState) {
        adminService.modify(memberState);
        return "/admin";
    }

    @PostMapping("/change")
    public String stateChangeMember(MemberState memberState) {
        adminService.stateChange(memberState);
        return "/admin";
    }
    //회원 리스트
    @GetMapping("/search")
    public List<MemberState> searchMember(MemberState memberState){
        return adminService.searchMember(memberState);
    }
}