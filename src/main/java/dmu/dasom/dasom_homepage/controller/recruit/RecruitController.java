package dmu.dasom.dasom_homepage.controller.recruit;

import dmu.dasom.dasom_homepage.domain.recruit.*;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import dmu.dasom.dasom_homepage.service.recruit.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruit")
public class RecruitController {
    private final RecruitService recruitService;

    @Autowired
    public RecruitController(RecruitService recruitService) {
        this.recruitService = recruitService;
    }

    // 모집 스케줄 추가
    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createNewRecruitSchedule(@RequestBody RecruitSchedule recruitSchedule) {
        recruitService.createNewRecruitSchedule(recruitSchedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "모집 일정 생성 성공"));
    }

    // 모집 스케줄 리스트 가져오기
    @GetMapping()
    public ResponseEntity<ApiResponse<List<RecruitScheduleIndex>>> getRecruitScheduleList() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, recruitService.getRecruitScheduleList()));
    }

    // 모집 스케줄 상세 정보 가져오기
    @GetMapping("/{recNo}")
    public ResponseEntity<ApiResponse<RecruitSchedule>> getRecruitScheduleDetails(@PathVariable int recNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, recruitService.getRecruitScheduleDetails(recNo)));
    }

    // 모집 스케줄 수정
    @PutMapping("/{recNo}")
    public ResponseEntity<ApiResponse<Void>> updateRecruitSchedule(@PathVariable int recNo, @RequestBody RecruitSchedule recruitSchedule) {
        recruitService.updateRecruitSchedule(recNo, recruitSchedule);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "모집 일정 업데이트 성공"));
    }

    // 지원자 추가
    @PostMapping("/{recNo}/applicants")
    public ResponseEntity<ApiResponse<Void>> saveNewApplicant(@PathVariable int recNo, @RequestBody DasomApplicant applicant) {
        recruitService.saveNewApplicant(recNo, applicant);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "지원자 정보 저장 성공"));
    }

    // 지원자 리스트 가져오기
    @GetMapping("/{recNo}/applicants")
    public ResponseEntity<ApiResponse<List<DasomApplicantIndex>>> getApplicantList(@PathVariable int recNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, recruitService.getApplicantList(recNo)));
    }

    // 지원자 상세 정보 가져오기
    @GetMapping("/{recNo}/applicants/{acStudentNo}")
    public ResponseEntity<ApiResponse<DasomApplicant>> getApplicant(@PathVariable int recNo, @PathVariable int acStudentNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, recruitService.getApplicant(recNo, acStudentNo)));
    }

    // 지원자 정보 업데이트
    @PutMapping("/{recNo}/applicants/{acStudentNo}")
    public ResponseEntity<ApiResponse<Void>> updateApplicantInfo(@PathVariable int recNo, @PathVariable int acStudentNo, @RequestBody DasomApplicantUpdate dasomApplicant) {
        recruitService.updateApplicantInfo(recNo, acStudentNo, dasomApplicant);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "지원자 정보 업데이트 성공"));
    }

    // 지원자 정보 삭제하기
    @DeleteMapping("/{recNo}/applicants/{acStudentNo}")
    public ResponseEntity<ApiResponse<Void>> deleteApplicant(@PathVariable int recNo, @PathVariable int acStudentNo) {
        recruitService.deleteApplicant(recNo, acStudentNo);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "지원자 정보 삭제 성공"));
    }

}
