package dmu.dasom.dasom_homepage.repository;

import dmu.dasom.dasom_homepage.domain.admin.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StudyRepository {
    //모든 유저가 접근 : 모든 프로젝트 반환 select
    List<StudyList> getProjects();

    //admin유저 접근 : 생성
    void createProject(Study study);

    //admin유저 접근 : 수정 : apdate
    void editProject(Study study);

    //admin유저 접근 : 삭제 : delete
    void removeProject(int project_no);

    //admin유저 접근 : 수정 : 존재 유무 확인 select
    Study findProjectById(int project_no);

    // ---------------부원 부분---------------

    //특정 project에서 부원 list 반환 | 해당 프로젝트의 참가자들 반환
    List<StudyParticipants> getParticipants(int project_no);
    void removeParticipant(StudyParticipants studyParcitipants);
    void addParticipant(StudyParticipants studyParcitipants);
    //해당 부원의 존재를 확인하기 위한 부원 반환
    StudyParticipants getParticipant(StudyParticipants studyParcitipants);
    //---------category | role ----------

    void addCategory(Category category);
    void removeCategory(String categoryName);
    //category 존재 유무 확인
    Category getCategoryByName(String categoryName);
    int getProjectsNumByCategory(String categoryName);

    void addRole(Role role);
    void removeRole(String role_name);
    Role getRoleByName(String role_name);
    int getParticipantsNumByRole(String role_name);
}
