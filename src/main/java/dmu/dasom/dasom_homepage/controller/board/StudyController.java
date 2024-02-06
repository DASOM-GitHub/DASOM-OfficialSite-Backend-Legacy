package dmu.dasom.dasom_homepage.controller.board;

import dmu.dasom.dasom_homepage.domain.board.project_study.Study;
import dmu.dasom.dasom_homepage.domain.board.project_study.StudyList;
import dmu.dasom.dasom_homepage.domain.board.project_study.StudyParticipants;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import dmu.dasom.dasom_homepage.service.board.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/board/study")
public class StudyController {
    private StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService){
        this.studyService = studyService;
    }

    //수정하기
    @PutMapping()
    public ResponseEntity<ApiResponse<Void>> edit(@ModelAttribute Study study, @RequestParam(value = "studyFile") MultipartFile studyFile) throws IOException {
        studyService.editStudy(study, studyFile);
        // 추후 ioException 수정 필요 -> handler추가 필요
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    @GetMapping()
    // 모든 유저가 /study에 접근시 study 내용 반환
    public ResponseEntity<ApiResponse<List<StudyList>>> getStudies(){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, studyService.getStudys()));
    }

    @GetMapping("/{studyNo}")
    //상세페이지에 들어왔을떄 해당 페이지의 상세 페이지 반환
    public ResponseEntity<ApiResponse<Study>> getStudy(@PathVariable("studyNo") int studyNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                true, studyService.getStudyById(studyNo)
        ));
    }

    @PostMapping()
    // study 추가 생성
    public ResponseEntity<ApiResponse<Void>> create(@ModelAttribute Study study, @RequestParam(value = "studyFile") MultipartFile studyFile) throws IOException {
        studyService.createStudy(study, studyFile);
        // 추후 ioException 수정 필요
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true));
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
}