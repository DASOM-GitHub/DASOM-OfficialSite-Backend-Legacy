package dmu.dasom.dasom_homepage.controller.recruit;

import dmu.dasom.dasom_homepage.domain.recruit.*;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import dmu.dasom.dasom_homepage.service.recruit.RecruitScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruit")
@PreAuthorize("hasRole('MANAGER')")
public class RecruitScheduleController {
    private final RecruitScheduleService recruitScheduleService;

    @Autowired
    public RecruitScheduleController(RecruitScheduleService recruitScheduleService) {
        this.recruitScheduleService = recruitScheduleService;
    }

    /* GET */
    // 모집 스케줄 리스트 가져오기
    @PreAuthorize("permitAll()")
    @GetMapping()
    public ResponseEntity<ApiResponse<List<RecruitScheduleIndex>>> getRecruitScheduleList() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, recruitScheduleService.getRecruitScheduleList()));
    }

    // 모집 스케줄 상세 정보 가져오기
    @PreAuthorize("permitAll()")
    @GetMapping("/{recNo}")
    public ResponseEntity<ApiResponse<RecruitSchedule>> getRecruitScheduleDetails(@PathVariable int recNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, recruitScheduleService.getRecruitScheduleDetails(recNo)));
    }


    /* POST */
    // 모집 스케줄 추가
    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createNewRecruitSchedule(@RequestBody RecruitSchedule recruitSchedule) {
        recruitScheduleService.createNewRecruitSchedule(recruitSchedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true));
    }


    /* PUT */
    // 모집 스케줄 수정
    @PutMapping("/{recNo}")
    public ResponseEntity<ApiResponse<Void>> updateRecruitSchedule(@PathVariable int recNo, @RequestBody RecruitSchedule recruitSchedule) {
        recruitScheduleService.updateRecruitSchedule(recNo, recruitSchedule);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }


    /* DELETE */
    // 모집 스케줄 삭제
    @DeleteMapping("/{recNo}")
    public ResponseEntity<ApiResponse<Void>> deleteRecruitSchedule(@PathVariable int recNo) {
        recruitScheduleService.deleteRecruitSchedule(recNo);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }
}
