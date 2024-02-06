package dmu.dasom.dasom_homepage.service.admin;

import dmu.dasom.dasom_homepage.domain.admin.*;
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
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private S3UploadService s3UploadService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, S3UploadService s3UploadService){
        this.projectRepository = projectRepository;
        this.s3UploadService = s3UploadService;
    }

    //모든 유저가 접근 : 모든 project 반환 select
    public List<ProjectList> getProjects(){
        List<ProjectList> projectLists = projectRepository.getProjects();
        if(projectLists.size() < 0){
            throw new DataNotFoundException();
        }else{
            return projectLists;
        }
    }

    // 한개의 상세 project 반환
    public Project getProjectById(int project_no){
        //존재 확인
        if(!isProjectById(project_no)){
            // 해당 project없음
            throw new DataNotFoundException();
        }else{ return projectRepository.findProjectById(project_no); }
    }

    // admin 접근 : create new Project | edit project
    public void createProject(Project project, MultipartFile projectFile) throws  IOException {
        //파일 저장
        String fileUrl = s3UploadService.saveFile(projectFile);
        project.setProjectPic(fileUrl);

        if(!isCategory(project.getProjectCategory())){
            // category가 존재하지 않음
            throw new ProjectException();
        } else {
            //Project 생성
            projectRepository.createProject(project);
        }
    }

    //admin유저 접근 : 수정 : update
    public Project editProject(Project project, MultipartFile projectFile) throws IOException{
        if(!isCategory(project.getProjectCategory())){
            // category를 찾을수 없음
            throw new ProjectException();
        } else if(!isProjectById(project.getProjectNo())){
            //수정할 project를 찾을수 없음
            throw new DataNotFoundException();
        }else{
            // 수정
            String fileUrl = s3UploadService.saveFile(projectFile);
            project.setProjectPic(fileUrl);

            projectRepository.editProject(project);
            return project;
        }
    }

    //admin유저 접근 : 삭제 : delete project
    public void removeProject(int projectNo){
        if(!isProjectById(projectNo)){
            //삭제할 project를 찾을 수 없음
            throw new DataNotFoundException();
//        }else if(studyRepository.getParticipants(studyNo).size() > 0) {
//            // 해당 study의 부원이 아직 존재할 경우
//
//            throw new ProjectException(); //bad_request 반환
        }else{
            //project 삭제
            projectRepository.removeProject(projectNo);
        }
    }

    //존재 유무 확인 select , 사용 함수 : 수정, 삭제
    private boolean isProjectById(int project_no){
        Optional<Project> pro =  Optional.ofNullable(projectRepository.findProjectById(project_no));
        return pro.isPresent();
    }

    //---------------------부원 부분--------------------

    public List<ProjectParticipants> getParticipants(int project_no) {
        //해당 프로젝트가 있는지 여부 확인
        if (!isProjectById(project_no)) {
            throw new DataNotFoundException();
//        } else if (){
//            // 참가자가 없을때
//            throw new E();
        } else{
            return projectRepository.getParticipants(project_no);
        }
    }

    public void addParticipant(ProjectParticipants projectParticipants) {

        if (!isProjectById(projectParticipants.getProjectNo())) {
            // project가 없을시
            throw new DataNotFoundException();
        } else if (isParticipant(projectParticipants.getProjectNo(), projectParticipants.getParticipantNo())) {
            // 이미 해당 참가자가 있는경우
            throw new InsertConflictException();
        }else if(!isRole(projectParticipants.getParticipantRole())) {
            //알맞지 않은 role
            throw new ProjectException(); //bad_request반환
        }else {
            projectRepository.addParticipant(projectParticipants);
        }
    }

    public void removeParticipant(int projectNo, int participantNo){
        if (!isProjectById(projectNo)) {
            // project가 없을시
            throw new DataNotFoundException();
        } else if (!isParticipant(projectNo, participantNo)) {
            // 이미 해당 참가자를 찾을수 없는 경우
            throw new ProjectException(); // bad_request반환
        }else {
            projectRepository.removeParticipant(projectNo,participantNo);
        }
    }

    private boolean isParticipant(int projectNo, int participantNo){
        return Optional.ofNullable(projectRepository.getParticipant(projectNo, participantNo)).isPresent();
    }

    //-------------------------category|role-------------------------

    private boolean isCategory(String categoryName){
        Optional<Category> ca = Optional.ofNullable(projectRepository.getCategoryByName(categoryName));
        return ca.isPresent();
    }

    private boolean isRole(String role_name){
        Optional<Role> role = Optional.ofNullable(projectRepository.getRoleByName(role_name));
        return role.isPresent();
    }
}
