package dmu.dasom.dasom_homepage.controller.board;

import dmu.dasom.dasom_homepage.domain.board.project_study.*;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import dmu.dasom.dasom_homepage.service.board.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/board/study")
@PreAuthorize("hasRole('MANAGER')")
public class StudyController {
    private final StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService){
        this.studyService = studyService;
    }

    //수정하기
    @PutMapping("/{studyNo}")
    public ResponseEntity<ApiResponse<Void>> edit(@PathVariable("studyNo") int studyNo,
                                                  @RequestBody Study study) throws IOException {
        studyService.editStudy(studyNo, study);
        // 추후 ioException 수정 필요
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    @GetMapping()
    @PreAuthorize("permitAll()")
    // 모든 유저가 /study에 접근시 study 내용 반환
    public ResponseEntity<ApiResponse<List<StudyList>>> getStudies(){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, studyService.getStudys()));
    }

    @GetMapping("/{studyNo}")
    @PreAuthorize("permitAll()")
    //상세페이지에 들어왔을떄 해당 페이지의 상세 페이지 반환
    public ResponseEntity<ApiResponse<Study>> getStudy(@PathVariable("studyNo") int studyNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                true, studyService.getStudyById(studyNo)
        ));
    }

    @PostMapping()
    // study 추가 생성
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody Study study) throws IOException {
        studyService.createStudy(study);
        // 추후 ioException 수정 필요
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true));
    }

    @PatchMapping("/{studyNo}")
    public ResponseEntity<ApiResponse<Void>> editAttachedPics(@PathVariable("studyNo") int studyNo,
                                                              @RequestPart(required = false) MultipartFile thumbnailFile,
                                                              @RequestPart(required = false) MultipartFile studyFile) throws IOException {
        studyService.editAttachedPics(studyNo, thumbnailFile, studyFile);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    @DeleteMapping("/{studyNo}")
    //admin 유저가 삭제
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable("studyNo") int studyNo) {
        studyService.removeStudy(studyNo);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }
    //-----------------------부원 부분-----------------------------
    //특정 study 부원등 불러오기
    @GetMapping("/{studyNo}/participants")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<StudyParticipants>>> getParticipants(@PathVariable("studyNo") int studyNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                true,studyService.getParticipants(studyNo)
        ));
    }

    //특정 study에서 부원 추가하기
    @PostMapping("/{studyNo}/participants")
    public ResponseEntity<ApiResponse<Void>> addParticipants(@PathVariable("studyNo") int studyNo,@ModelAttribute StudyParticipants studyParticipants) {//@ModelAttribute Role role
        studyParticipants.setStudyNo(studyNo);
        studyService.addParticipant(studyParticipants);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true));
    }

    //부원 삭제하기
    @DeleteMapping("/{studyNo}/participants/{participantNo}")
    public ResponseEntity<ApiResponse<Void>> removeParticipants(@PathVariable("studyNo") int studyNo,@PathVariable("participantNo") int participantNo) {
        studyService.removeParticipant(studyNo, participantNo);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }


    // 스터디 진행 사항 조회
    @GetMapping("/{studyNo}/progress")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<StudyProgress>>> getStudyProgresses(@PathVariable int studyNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, studyService.getStudyProgresses(studyNo)));
    }

    // 스터디 진행 사항 추가
    @PostMapping("/{studyNo}/progress")
    public ResponseEntity<ApiResponse<Void>> addStudyProgress(@PathVariable int studyNo, @RequestBody StudyProgress studyProgress) {
        studyService.addStudyProgress(studyNo, studyProgress);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true));
    }

    // 스터디 진행 사항 수정
    @PutMapping("/{studyNo}/progress/{studyWeek}")
    public ResponseEntity<ApiResponse<Void>> editStudyProgress(@PathVariable int studyNo, @PathVariable int studyWeek, @RequestBody StudyProgressUpdate studyProgressUpdate) {
        studyService.editStudyProgress(studyNo, studyWeek, studyProgressUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    // 스터디 진행 사항 삭제
    @DeleteMapping("/{studyNo}/progress/{studyWeek}")
    public ResponseEntity<ApiResponse<Void>> deleteStudyProgress(@PathVariable int studyNo, @PathVariable int studyWeek) {
        studyService.deleteStudyProgress(studyNo, studyWeek);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

}
