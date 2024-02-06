package dmu.dasom.dasom_homepage.service.board;

import dmu.dasom.dasom_homepage.domain.board.project_study.Project;
import dmu.dasom.dasom_homepage.domain.board.project_study.ProjectList;
import dmu.dasom.dasom_homepage.domain.board.project_study.ProjectParticipants;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
import dmu.dasom.dasom_homepage.exception.InsertConflictException;
import dmu.dasom.dasom_homepage.exception.ProjectException;
import dmu.dasom.dasom_homepage.repository.ProjectRepository;
import dmu.dasom.dasom_homepage.service.s3.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final S3UploadService s3UploadService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, S3UploadService s3UploadService){
        this.projectRepository = projectRepository;
        this.s3UploadService = s3UploadService;
    }

    //모든 유저가 접근 : 모든 project 반환 select
    public List<ProjectList> getProjects(){
        List<ProjectList> projectLists = projectRepository.getProjects();
        if (projectLists.isEmpty())
            throw new DataNotFoundException();

        return projectLists;
    }

    // 한개의 상세 project 반환
    public Project getProjectById(int project_no){
        //존재 확인
        if (!isProjectExistById(project_no))
            // 해당 project없음
            throw new DataNotFoundException();

        return projectRepository.findProjectById(project_no);
    }

    // admin 접근 : create new Project | edit project
    public void createProject(Project project, MultipartFile projectFile) throws IOException {
        //파일 저장
        String fileUrl = s3UploadService.saveFile(projectFile);
        project.setProjectPic(fileUrl);

        projectRepository.createProject(project);
    }

    //admin유저 접근 : 수정 : update
    public void editProject(Project project, MultipartFile projectFile) throws IOException{
        if (!isProjectExistById(project.getProjectNo()))
            //수정할 project를 찾을수 없음
            throw new DataNotFoundException();

        // 수정
        String fileUrl = s3UploadService.saveFile(projectFile);
        project.setProjectPic(fileUrl);

        projectRepository.editProject(project);
    }

    //admin유저 접근 : 삭제 : delete project
    public void removeProject(int projectNo){
        if (!isProjectExistById(projectNo))
            //삭제할 project를 찾을 수 없음
            throw new DataNotFoundException();

        projectRepository.removeProject(projectNo);
    }

    //존재 유무 확인 select , 사용 함수 : 수정, 삭제
    private boolean isProjectExistById(int project_no){
        return projectRepository.isProjectExistById(project_no);
    }

    //---------------------부원 부분--------------------

    public List<ProjectParticipants> getParticipants(int project_no) {
        //해당 프로젝트가 있는지 여부 확인
        if (!isProjectExistById(project_no)) {
            throw new DataNotFoundException();
        } else{
            return projectRepository.getParticipants(project_no);
        }
    }

    public void addParticipant(ProjectParticipants projectParticipants) {
        if (!isProjectExistById(projectParticipants.getProjectNo()))
            // project가 없을시
            throw new DataNotFoundException();

        if (isParticipant(projectParticipants.getProjectNo(), projectParticipants.getParticipantNo()))
            // 이미 해당 참가자가 있는경우
            throw new InsertConflictException();

        projectRepository.addParticipant(projectParticipants);
    }

    public void removeParticipant(int projectNo, int participantNo){
        if (!isProjectExistById(projectNo))
            // project가 없을시
            throw new DataNotFoundException();

        if (!isParticipant(projectNo, participantNo))
            // 이미 해당 참가자를 찾을수 없는 경우
            throw new ProjectException(); // bad_request반환

        projectRepository.removeParticipant(projectNo,participantNo);
    }

    private boolean isParticipant(int projectNo, int participantNo){
        return projectRepository.isParticipant(projectNo, participantNo);
    }

    //-------------------------category|role-------------------------

//    private boolean isCategory(String categoryName){
//        Optional<Category> ca = Optional.ofNullable(projectRepository.getCategoryByName(categoryName));
//        return ca.isPresent();
//    }
//
//    private boolean isRole(String role_name){
//        Optional<Role> role = Optional.ofNullable(projectRepository.getRoleByName(role_name));
//        return role.isPresent();
//    }
}
