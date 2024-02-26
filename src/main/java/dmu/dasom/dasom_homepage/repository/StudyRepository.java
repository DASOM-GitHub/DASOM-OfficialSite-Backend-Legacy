package dmu.dasom.dasom_homepage.repository;

import dmu.dasom.dasom_homepage.domain.board.project_study.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StudyRepository {
    //모든 유저가 접근 : 모든 프로젝트 반환 select
    List<StudyList> getStudys();

    // 수정 : 존재 유무 확인 select | 해당 study 상세페이지
    Study findStudyById(int studyNo);

    // 프로젝트 존재 여부 확인
    Boolean isStudyExistById(int studyNo);

    // 생성
    void createStudy(Study study);

    // 수정 : update
    void editStudy(Study study);

    void editAttachedPics(Study study);

    // 삭제 : delete
    void removeStudy(int project_no);

    // ---------------부원 부분---------------

    //특정 study에서 부원 list 반환 | 해당 프로젝트의 참가자들 반환
    List<StudyParticipants> getParticipants(int project_no);

    void removeParticipant(@Param("studyNo")int studyNo, @Param("participantNo")int participantNo);
    void addParticipant(StudyParticipants studyParcitipants);
    //해당 부원의 존재를 확인하기 위한 부원 반환
    Boolean isParticipant(@Param("studyNo")int studyNo, @Param("participantNo")int participantNo);


    List<StudyProgress> getStudyProgresses(int studyNo);

    void addStudyProgress(StudyProgress studyProgress);

    void editStudyProgress(StudyProgressUpdate studyProgressUpdate);

    void deleteStudyProgress(int studyNo, int studyWeek);

    boolean isStudyProgressExists(int studyNo, int studyWeek);

}
