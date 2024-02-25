package dmu.dasom.dasom_homepage.controller.member;

import dmu.dasom.dasom_homepage.domain.member.DasomMemberIndex;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import dmu.dasom.dasom_homepage.service.member.MemberIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/members/index")
@PreAuthorize("permitAll()")
public class MemberIndexController {
    private final MemberIndexService memberIndexService;

    @Autowired
    public MemberIndexController(MemberIndexService memberIndexService) {
        this.memberIndexService = memberIndexService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<DasomMemberIndex>>> getMemberIndexList() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, memberIndexService.getMemberIndexList()));
    }

}
