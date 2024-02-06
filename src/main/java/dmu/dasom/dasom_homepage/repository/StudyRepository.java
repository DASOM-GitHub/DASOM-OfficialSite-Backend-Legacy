package dmu.dasom.dasom_homepage.repository;

import dmu.dasom.dasom_homepage.domain.admin.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface StudyRepository {
    //모든 유저가 접근 : 모든 프로젝트 반환 select
    List<StudyList> getStudys();

    // 수정 : 존재 유무 확인 select | 해당 study 상세페이지
    Study findStudyById(int study_no);

    // 생성
    void createStudy(Study study);

    // 수정 : update
    void editStudy(Study study);

    // 삭제 : delete
    void removeStudy(int study_no);

    // ---------------부원 부분---------------

    //특정 study에서 부원 list 반환 | 해당 프로젝트의 참가자들 반환
    List<StudyParticipants> getParticipants(int study_no);
    void removeParticipant(@Param("studyNo")int studyNo, @Param("participantNo")int participantNo);
    void addParticipant(StudyParticipants studyParcitipants);
    //해당 부원의 존재를 확인하기 위한 부원 반환
    StudyParticipants getParticipant(@Param("studyNo")int studyNo, @Param("participantNo")int participantNo);

    //---------category | role ----------
    Category getCategoryByName(String categoryName);
    Role getRoleByName(String role_name);
}
