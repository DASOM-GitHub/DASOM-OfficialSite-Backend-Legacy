package dmu.dasom.dasom_homepage.service.admin;

import dmu.dasom.dasom_homepage.domain.admin.*;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
import dmu.dasom.dasom_homepage.repository.StudyRepository;
import dmu.dasom.dasom_homepage.service.s3.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class StudyService {
    private final StudyRepository projectRepository;
    @Autowired
    private S3UploadService s3UploadService;

    @Autowired
    public StudyService(StudyRepository studyRepository){
        this.projectRepository = studyRepository;
    }

    //모든 유저가 접근 : 모든 프로젝트 반환 select
    public List<StudyList> getProjects(){
        return projectRepository.getProjects();
    }

    // 한개의 상세페이지 반환
    public Study getProjectById(int project_no){
        //존재 확인
        if(isProjectById(project_no)){
            //반환
            return projectRepository.findProjectById(project_no);
        }else{ throw new DataNotFoundException(); }
    }

    // admin 접근 : create new Project | edit project
    public Study createProject(Study project, MultipartFile projectFile) throws  IOException {
        //category 유무 확인
        addCategory(project.getStudyCategory());

        try {
            String fileUrl = s3UploadService.saveFile(projectFile);
            project.setStudyPic(fileUrl);
        } catch (IOException e) {
            throw new IOException();
        }
        //같은 프로젝트 존재 여부 확인
        if (isProjectById(project.getStudyNo())) {
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

    //admin유저 접근 : 삭제 : delete
    public void removeProject(int projectNo){
        if(isProjectById(projectNo)){
            //category 삭제
            String category_name = getProjectById(projectNo).getStudyCategory();
            //삭제 진행
            projectRepository.removeProject(projectNo);
            //category삭제
            removeCategory(category_name);
        }else{
            //삭제할 project를 찾을 수 없음
            throw new DataNotFoundException();
        }
    }

    //유저 접근 X  수정,생성 : 존재 유무 확인 select
    boolean isProjectById(int project_no){
        Optional<Study> pro =  Optional.ofNullable(projectRepository.findProjectById(project_no));
        return pro.isPresent();
    }

    //---------------------참가자 부분--------------------

    public List<StudyParticipants> getParticipants(int project_no) {
        //해당 프로젝트가 있는지 여부 확인
        if (isProjectById(project_no)) {
            return projectRepository.getParticipants(project_no);
        }else{
            throw new DataNotFoundException();
        }
    }

    public boolean addParticipant(StudyParticipants projectParticipants) {
        // role 여부 확인
        addRole(projectParticipants.getParticipantRole());

        if (!isProjectById(projectParticipants.getStudyNo())) {
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

    public boolean removeParticipant(StudyParticipants projectParticipants){
        String role_name = projectParticipants.getParticipantRole();

        if (!isProjectById(projectParticipants.getStudyNo())) {
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
