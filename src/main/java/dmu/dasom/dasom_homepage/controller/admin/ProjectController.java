package dmu.dasom.dasom_homepage.controller.admin;

import dmu.dasom.dasom_homepage.domain.admin.*;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import dmu.dasom.dasom_homepage.service.admin.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/board/project")
public class ProjectController {
    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    //수정하기
    @PutMapping()
    public ResponseEntity<ApiResponse> edit(@ModelAttribute Project project, @RequestParam(value = "projectFile") MultipartFile projectFile) throws IOException {
        projectService.editProject(project, projectFile);
        // 추후 ioException 수정 필요 -> handler추가 필요
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    @GetMapping()
    // 모든 유저가 /study에 접근시 study 내용 반환
    public ResponseEntity<ApiResponse<List<ProjectList>>> getStudys(){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, projectService.getProjects()));
    }

    @GetMapping("/{projectNo}")
    //상세페이지에 들어왔을떄 해당 페이지의 상세 페이지 반환
    public ResponseEntity<ApiResponse<Project>> getStudy(@PathVariable("projectNo") int projectNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                true, projectService.getProjectById(projectNo)
        ));
    }

    @PostMapping()
    // project 추가 생성
    public ResponseEntity<ApiResponse> create(@ModelAttribute Project project, @RequestParam(value = "projectFile") MultipartFile projectFile) throws IOException {
        projectService.createProject(project, projectFile);
        // 추후 ioException 수정 필요
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true));
    }


    @DeleteMapping("/{projectNo}")
    //admin 유저가 삭제
    public ResponseEntity<ApiResponse> remove(@PathVariable("projectNo") int projectNo) {
        projectService.removeProject(projectNo);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }
//-----------------------부원 부분-----------------------------
    //특정 project 부원등 불러오기
    @GetMapping("/{projectNo}/participants")
    public ResponseEntity<ApiResponse<List<ProjectParticipants>>> getParticipants(@PathVariable("projectNo") int projectNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                true,projectService.getParticipants(projectNo)
        ));
    }

    //특정 project에서 부원 추가하기
    @PostMapping("/{projectNo}/participants")
    public ResponseEntity<ApiResponse> addParticipants(@PathVariable("projectNo") int projectNo,@ModelAttribute ProjectParticipants projectParticipants) {//@ModelAttribute Role role
        projectParticipants.setProjectNo(projectNo);
        projectService.addParticipant(projectParticipants);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true));
    }

    //부원 삭제하기
    @DeleteMapping("/{projectNo}/participants/{participantNo}")
    public ResponseEntity<ApiResponse> removeParticipants(@PathVariable("projectNo") int projectNo,@PathVariable("participantNo") int participantNo) {
        projectService.removeParticipant(projectNo, participantNo);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }
}