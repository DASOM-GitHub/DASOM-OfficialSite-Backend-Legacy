package dmu.dasom.dasom_homepage.service.admin;

import dmu.dasom.dasom_homepage.domain.admin.*;
import dmu.dasom.dasom_homepage.exception.DataAlreadyExistException;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
import dmu.dasom.dasom_homepage.repository.ProjectRepository;
import dmu.dasom.dasom_homepage.service.s3.S3UploadService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    @Autowired
    private S3UploadService s3UploadService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    //모든 유저가 접근 : 모든 project 반환 select
    public List<ProjectList> getProjects(){
        return projectRepository.getProjects();
    }

    // 한개의 상세 project 반환
    public Project getProjectById(int project_no){
        //존재 확인
        if(isProjectById(project_no)){
            //반환
            return projectRepository.findProjectById(project_no);
        }else{ throw new DataNotFoundException(); }
    }

    // admin 접근 : create new Project | edit project
    public Project createProject(Project project, MultipartFile projectFile) throws  IOException {
        //category 유무 확인 : 존재하지 않으면 새로운 category 생성 (category_description : auto_created)
        addCategory(project.getProjectCategory());

        try {
            //파일 저장
            String fileUrl = s3UploadService.saveFile(projectFile);
            project.setProjectPic(fileUrl);
        } catch (IOException e) {
            //파일 저장 실패
            throw new IOException();
        }
        //같은 프로젝트 존재 여부 확인
        if (isProjectById(project.getProjectNo())) {
            //프로젝트 이미 존재함 => 수정
            projectRepository.editProject(project);
        }else {
            //Project 생성
            projectRepository.createProject(project);
        }
        return project;
    }

    //admin유저 접근 : 수정 : update
//    public Project editProject(Project project, MultipartFile projectFile) throws IOException{
//        System.out.println("edit > "+isProjectById(project.getProjectNo())+","+project.getProjectNo());
//        if(isProjectById(project.getProjectNo())){
//            // 수정
//            String fileUrl = saveFile(projectFile);
//            project.setProjectPic(fileUrl);
//
//            projectRepository.editProject(project);
//            return project;
//        }else{
//            //수정할 project를 찾을수 없음
//            throw new DataNotFoundException();
//        }
//    }

    //admin유저 접근 : 삭제 : delete project
    public void removeProject(int projectNo){
        if(isProjectById(projectNo)){
            //get category_name by projectNo
            String category_name = getProjectById(projectNo).getProjectCategory();
            //project 삭제
            projectRepository.removeProject(projectNo);
            //category 삭제 :해당 category를 가진 project가 존재하지 않을때 category 삭제
            removeCategory(category_name);
        }else{
            //삭제할 project를 찾을 수 없음
            throw new DataNotFoundException();
        }
    }

    //존재 유무 확인 select , 사용 함수 : 수정, 삭제
    boolean isProjectById(int project_no){
        Optional<Project> pro =  Optional.ofNullable(projectRepository.findProjectById(project_no));
        return pro.isPresent();
    }

    //---------------------부원 부분--------------------

    public List<ProjectParticipants> getParticipants(int project_no) {
        //해당 프로젝트가 있는지 여부 확인
        if (isProjectById(project_no)) {
            return projectRepository.getParticipants(project_no);
        }else{
            throw new DataNotFoundException();
        }
    }

    public boolean addParticipant(ProjectParticipants projectParticipants) {
        // role 여부 확인
        addRole(projectParticipants.getParticipantRole());

        if (!isProjectById(projectParticipants.getProjectNo())) {
            // project가 없을시
            throw new DataNotFoundException();
        } else if (Optional.ofNullable(projectRepository.getParticipant(projectParticipants)).isPresent()) {
            // 이미 해당 참가자가 있는경우
            return false;
        } else {
            projectRepository.addParticipant(projectParticipants);
            return true;
        }
    }

    public boolean removeParticipant(ProjectParticipants projectParticipants){
        String role_name = projectParticipants.getParticipantRole();

        if (!isProjectById(projectParticipants.getProjectNo())) {
            // project가 없을시
            throw new DataNotFoundException();
        } else if (!Optional.ofNullable(projectRepository.getParticipant(projectParticipants)).isPresent()) {
            // 이미 해당 참가자가 없는경우
            return false;
        }else {
            projectRepository.removeParticipant(projectParticipants);
            removeRole(role_name);
            return true;
        }
    }

    //-------------------------category|role-------------------------
    // 존재하는지 확인후 없다면 생성
    void addCategory(String categoryName){
        if(!isCategory(categoryName)){
            // 존재하지 않으면
            Category category = new Category();
            category.setCategoryName(categoryName);
            category.setCategoryDescription("created auto");
            projectRepository.addCategory(category);
        }
    }
    boolean isCategory(String categoryName){
        Optional<Category> ca = Optional.ofNullable(projectRepository.getCategoryByName(categoryName));
        return ca.isPresent();
    }
    // 해당 category를 사용하는 project가 더 이상 없으면 삭제
    void removeCategory(String categoryName){
        // 존재하는지 확인 ?
        if(projectRepository.getProjectsNumByCategory(categoryName) == 0){
            //현재 Category를 가진 project가 0개 인 경우
            projectRepository.removeCategory(categoryName);
        }
    }


    void addRole(String role_name){
        if(!isRole(role_name)){
            // 존재하지 않으면
            Role role = new Role();
            role.setRoleName(role_name);
            role.setRoleDescription("auto_created");

            projectRepository.addRole(role);
        }
    }

    boolean isRole(String role_name){
        Optional<Role> role = Optional.ofNullable(projectRepository.getRoleByName(role_name));
        return role.isPresent();
    }

    void removeRole(String role_name){
        if(projectRepository.getParticipantsNumByRole(role_name) == 0){
            //현재 Category를 가진 project가 0개 인 경우
            projectRepository.removeRole(role_name);
        }
    }
}
