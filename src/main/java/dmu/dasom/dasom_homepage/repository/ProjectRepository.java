package dmu.dasom.dasom_homepage.repository;

import dmu.dasom.dasom_homepage.domain.board.project_study.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProjectRepository {
    //모든 유저가 접근 : 모든 프로젝트 반환 select
    List<ProjectList> getProjects();

    // 수정 : 존재 유무 확인 select | 해당 project 상세페이지
    Project findProjectById(int projectNo);

    // 프로젝트 존재 여부 확인
    Boolean isProjectExistById(int projectNo);

    // 생성
    void createProject(Project project);

    // 수정 : update
    void editProject(Project project);

    void editAttachedPics(Project project);

    // 삭제 : delete
    void removeProject(int project_no);

    // ---------------부원 부분---------------

    //특정 project에서 부원 list 반환 | 해당 프로젝트의 참가자들 반환
    List<ProjectParticipants> getParticipants(int project_no);

    void removeParticipant(@Param("projectNo")int projectNo, @Param("participantNo")int participantNo);
    void addParticipant(ProjectParticipants projectParcitipants);
    //해당 부원의 존재를 확인하기 위한 부원 반환
    Boolean isParticipant(@Param("projectNo")int projectNo, @Param("participantNo")int participantNo);

}
