package dmu.dasom.dasom_homepage.controller.admin;

import dmu.dasom.dasom_homepage.domain.admin.*;
import dmu.dasom.dasom_homepage.exception.DataAlreadyExistException;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
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
@RequestMapping("/project")
public class ProjectController {
    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping("")
    // 모든 유저가 /study에 접근시 study 내용 반환
    public ResponseEntity<ApiResponse<List<ProjectList>>> getStudys(){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, projectService.getProjects()));
    }

    @GetMapping("{project_no}")
    //상세페이지에 들어왔을떄 해당 페이지의 상세 페이지 반환
    public ResponseEntity<ApiResponse<Project>> getStudy(@PathVariable("project_no") int project_no){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    true, projectService.getProjectById(project_no)
            ));
        }catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
                    false, null
            ));
        }
    }

    @PostMapping("")
    // admin 유저가 study 추가 생성 밑 수정
    public ResponseEntity<ApiResponse<Project>> create(@ModelAttribute Project project, @RequestParam(value = "projectFile") MultipartFile projectFile){
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            true, projectService.createProject(project, projectFile)
                    ));
//        } catch (DataAlreadyExistException e) {
//            // 이미 같은 primary key의 값이 존재
//            return ResponseEntity.status(HttpStatus.CONFLICT).
//                    body(new ApiResponse<>(
//                            false, null
//                    ));
        } catch (IOException e) {
            // 파일 저장중 문제 발생
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new ApiResponse<>(
                            false, null
                    ));
        }
    }

    //수정하기
//    @PostMapping("edit")
//    public ResponseEntity<ApiResponse<Project>> edit(@RequestBody Project project, @RequestParam(value = "projectFile") MultipartFile projectFile){
//        System.out.println("put!");
//        if(!projectService.isCategory(project.getProjectCategory())){
//            // 카테고리 있는지 여부 확인
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
//                    body(new ApiResponse<>(
//                            false, null
//                    ));
//        }
//        try {
//            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
//                    true, projectService.editProject(project, projectFile)
//            ));
//        }catch (DataNotFoundException e){
//            // 수정할 project를 찾을수 없음
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).
//                    body(new ApiResponse<>(
//                            false, null
//                    ));
//        }catch (IOException e){
//            // 파일 저장 중 문제 발생
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
//                    body(new ApiResponse<>(
//                            false, null
//                    ));
//        }
//    }

    @DeleteMapping("/{project_no}")
    //admin 유저가 삭제
    public ResponseEntity<ApiResponse> remove(@PathVariable("project_no") int project_no){
        try {
            projectService.removeProject(project_no);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    true, null
            ));
        }catch(DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new ApiResponse<>(
                            false, null
                    ));
        }
    }
//-----------------------부원 부분-----------------------------
    //특정 project 부원 불러오기
    @GetMapping("{project_no}/participants")
    public ResponseEntity<ApiResponse<List<ProjectParticipants>>> getParticipants(@PathVariable("project_no") int project_no) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    true, projectService.getParticipants(project_no)
            ));
        }catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new ApiResponse<>(
                            false, null
                    ));
        }
    }

    //특정 project에서 부원 추가하기
    @PostMapping("{project_no}/participants")
    public ResponseEntity<ApiResponse<ProjectParticipants>> addParticipants(@PathVariable("project_no") int project_no,@ModelAttribute ProjectParticipants projectParticipants) {//@ModelAttribute Role role
        try {
            if(projectService.addParticipant(projectParticipants)){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                        true, projectParticipants
                ));
            } else{
                //이미 부원이 존재하는경우
                return ResponseEntity.status(HttpStatus.CONFLICT).
                        body(new ApiResponse<>(
                                false, null
                        ));
            }
        }catch(DataNotFoundException e){
            // 해당 프로젝트가 없는경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new ApiResponse<>(
                            false, null
                    ));
        }
        // 참가자가 이미 존재하는 경우 : service에서 true반환으로 그냥 진행
    }

    //부원 삭제하기
    @DeleteMapping("{project_no}/participants")
    public ResponseEntity<ApiResponse<ProjectParticipants>> removeParticipants(@PathVariable("project_no") int project_no,@ModelAttribute ProjectParticipants projectParticipants) {
        try {
            if(projectService.removeParticipant(projectParticipants)) {
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                        true, null
                ));
            }else{
                // 부원이 이미 존재 하지 않을떄
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body(new ApiResponse<>(
                                false, null
                        ));
            }
        }catch(DataNotFoundException e){
            // project가 존재 하지 않을떄
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new ApiResponse<>(
                            false, null
                    ));
        }
    }
}
