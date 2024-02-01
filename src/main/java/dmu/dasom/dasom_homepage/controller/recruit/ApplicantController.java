package dmu.dasom.dasom_homepage.controller.recruit;

import dmu.dasom.dasom_homepage.domain.recruit.*;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import dmu.dasom.dasom_homepage.service.recruit.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruit")
public class ApplicantController {
    private final ApplicantService applicantService;

    @Autowired
    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    /* GET */
    // 지원자 리스트 가져오기
    @GetMapping("/{recNo}/applicants")
    public ResponseEntity<ApiResponse<List<DasomApplicantIndex>>> getApplicantList(@PathVariable int recNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, applicantService.getApplicantList(recNo)));
    }

    // 지원자 상세 정보 가져오기
    @GetMapping("/{recNo}/applicants/{acStudentNo}")
    public ResponseEntity<ApiResponse<DasomApplicant>> getApplicant(@PathVariable int recNo, @PathVariable int acStudentNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, applicantService.getApplicant(recNo, acStudentNo)));
    }


    /* POST */
    // 지원자 추가
    @PostMapping("/{recNo}/applicants")
    public ResponseEntity<ApiResponse<Void>> saveNewApplicant(@PathVariable int recNo, @RequestBody DasomApplicant applicant) {
        applicantService.saveNewApplicant(recNo, applicant);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true));
    }


    /* PUT */
    // 지원자 정보 업데이트
    @PutMapping("/{recNo}/applicants/{acStudentNo}")
    public ResponseEntity<ApiResponse<Void>> updateApplicantInfo(@PathVariable int recNo, @PathVariable int acStudentNo, @RequestBody DasomApplicantUpdate dasomApplicant) {
        applicantService.updateApplicantInfo(recNo, acStudentNo, dasomApplicant);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }


    /* DELETE */
    // 지원자 정보 삭제하기
    @DeleteMapping("/{recNo}/applicants/{acStudentNo}")
    public ResponseEntity<ApiResponse<Void>> deleteApplicant(@PathVariable int recNo, @PathVariable int acStudentNo) {
        applicantService.deleteApplicant(recNo, acStudentNo);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

}
