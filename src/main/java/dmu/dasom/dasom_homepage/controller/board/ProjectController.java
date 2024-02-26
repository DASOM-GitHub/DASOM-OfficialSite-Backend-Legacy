package dmu.dasom.dasom_homepage.controller.board;

import dmu.dasom.dasom_homepage.domain.board.project_study.Project;
import dmu.dasom.dasom_homepage.domain.board.project_study.ProjectList;
import dmu.dasom.dasom_homepage.domain.board.project_study.ProjectParticipants;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import dmu.dasom.dasom_homepage.service.board.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/board/project")
@PreAuthorize("hasRole('MANAGER')")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    //수정하기
    @PutMapping("/{projectNo}")
    public ResponseEntity<ApiResponse<Void>> edit(@PathVariable("projectNo") int projectNo,
                                                  @RequestBody Project project) throws IOException {
        projectService.editProject(projectNo, project);
        // 추후 ioException 수정 필요
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    @GetMapping()
    @PreAuthorize("permitAll()")
    // 모든 유저가 /study에 접근시 study 내용 반환
    public ResponseEntity<ApiResponse<List<ProjectList>>> getStudies(){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, projectService.getProjects()));
    }

    @GetMapping("/{projectNo}")
    @PreAuthorize("permitAll()")
    //상세페이지에 들어왔을떄 해당 페이지의 상세 페이지 반환
    public ResponseEntity<ApiResponse<Project>> getStudy(@PathVariable("projectNo") int projectNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                true, projectService.getProjectById(projectNo)
        ));
    }

    @PostMapping()
    // project 추가 생성
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody Project project) {
        projectService.createProject(project);
        // 추후 ioException 수정 필요
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true));
    }

    @PatchMapping("/{projectNo}")
    public ResponseEntity<ApiResponse<Void>> editAttachedPics(@PathVariable("projectNo") int projectNo,
                                                              @RequestPart(required = false) MultipartFile thumbnailFile,
                                                              @RequestPart(required = false) MultipartFile projectFile) throws IOException {
        projectService.editAttachedPics(projectNo, thumbnailFile, projectFile);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    @DeleteMapping("/{projectNo}")
    //admin 유저가 삭제
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable("projectNo") int projectNo) {
        projectService.removeProject(projectNo);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }
//-----------------------부원 부분-----------------------------
    //특정 project 부원등 불러오기
    @GetMapping("/{projectNo}/participants")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<ProjectParticipants>>> getParticipants(@PathVariable("projectNo") int projectNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                true,projectService.getParticipants(projectNo)
        ));
    }

    //특정 project에서 부원 추가하기
    @PostMapping("/{projectNo}/participants")
    public ResponseEntity<ApiResponse<Void>> addParticipants(@PathVariable("projectNo") int projectNo,@ModelAttribute ProjectParticipants projectParticipants) {//@ModelAttribute Role role
        projectParticipants.setProjectNo(projectNo);
        projectService.addParticipant(projectParticipants);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true));
    }

    //부원 삭제하기
    @DeleteMapping("/{projectNo}/participants/{participantNo}")
    public ResponseEntity<ApiResponse<Void>> removeParticipants(@PathVariable("projectNo") int projectNo,@PathVariable("participantNo") int participantNo) {
        projectService.removeParticipant(projectNo, participantNo);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }
}
