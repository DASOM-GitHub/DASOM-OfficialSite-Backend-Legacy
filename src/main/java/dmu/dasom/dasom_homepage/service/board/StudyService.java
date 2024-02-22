package dmu.dasom.dasom_homepage.service.board;

import dmu.dasom.dasom_homepage.domain.board.project_study.*;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
import dmu.dasom.dasom_homepage.exception.InsertConflictException;
import dmu.dasom.dasom_homepage.exception.ProjectException;
import dmu.dasom.dasom_homepage.repository.StudyRepository;
import dmu.dasom.dasom_homepage.service.s3.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class StudyService {
    private final StudyRepository studyRepository;
    private final S3UploadService s3UploadService;

    @Autowired
    public StudyService(StudyRepository studyRepository, S3UploadService s3UploadService){
        this.studyRepository = studyRepository;
        this.s3UploadService = s3UploadService;
    }

    //모든 유저가 접근 : 모든 study 반환 select
    public List<StudyList> getStudys(){
        List<StudyList> studyLists = studyRepository.getStudys();
        if (studyLists.isEmpty())
            throw new DataNotFoundException();

        return studyLists;
    }

    // 한개의 상세 study 반환
    public Study getStudyById(int project_no){
        //존재 확인
        if (!isStudyExistById(project_no))
            // 해당 study없음
            throw new DataNotFoundException();

        return studyRepository.findStudyById(project_no);
    }

    // admin 접근 : create new Study | edit study
    public void createStudy(Study study) {
        studyRepository.createStudy(study);
    }

    //admin유저 접근 : 수정 : update
    public void editStudy(int studyNo, Study studyUpdate) {
        studyUpdate.setStudyNo(studyNo);
        if (!isStudyExistById(studyNo))
            //수정할 study를 찾을수 없음
            throw new DataNotFoundException();
        studyRepository.editStudy(studyUpdate);
    }

    // 첨부파일(사진) 업데이트
    public void editAttachedPics(int studyNo, MultipartFile thumbnailFile, MultipartFile studyFile) throws IOException {
        Study study = studyRepository.findStudyById(studyNo);
        s3UploadService.deleteFile(study.getThumbnailPic());
        s3UploadService.deleteFile(study.getStudyPic());

        if (thumbnailFile != null)
            study.setThumbnailPic(s3UploadService.saveFile(thumbnailFile));
        else study.setThumbnailPic(null);
        if (studyFile != null)
            study.setStudyPic(s3UploadService.saveFile(studyFile));
        else study.setStudyPic(null);

        studyRepository.editAttachedPics(study);
    }

    //admin유저 접근 : 삭제 : delete study
    public void removeStudy(int studyNo){
        if (!isStudyExistById(studyNo))
            //삭제할 study를 찾을 수 없음
            throw new DataNotFoundException();
        //이전 파일 삭제
        String oldFile = studyRepository.findStudyById(studyNo).getStudyPic();
        s3UploadService.deleteFile(oldFile);
        studyRepository.removeStudy(studyNo);
    }

    //존재 유무 확인 select , 사용 함수 : 수정, 삭제
    private boolean isStudyExistById(int project_no){
        return studyRepository.isStudyExistById(project_no);
    }

    //---------------------부원 부분--------------------

    public List<StudyParticipants> getParticipants(int project_no) {
        //해당 프로젝트가 있는지 여부 확인
        if (!isStudyExistById(project_no)) {
            throw new DataNotFoundException();
        } else{
            return studyRepository.getParticipants(project_no);
        }
    }

    public void addParticipant(StudyParticipants studyParticipants) {
        if (!isStudyExistById(studyParticipants.getStudyNo()))
            // study가 없을시
            throw new DataNotFoundException();

        if (isParticipant(studyParticipants.getStudyNo(), studyParticipants.getParticipantNo()))
            // 이미 해당 참가자가 있는경우
            throw new InsertConflictException();

        studyRepository.addParticipant(studyParticipants);
    }

    public void removeParticipant(int studyNo, int participantNo){
        if (!isStudyExistById(studyNo))
            // study가 없을시
            throw new DataNotFoundException();

        if (!isParticipant(studyNo, participantNo))
            // 이미 해당 참가자를 찾을수 없는 경우
            throw new ProjectException(); // bad_request반환

        studyRepository.removeParticipant(studyNo,participantNo);
    }

    private boolean isParticipant(int studyNo, int participantNo){
        return studyRepository.isParticipant(studyNo, participantNo);
    }



    public List<StudyProgress> getStudyProgresses(int studyNo) {
        List<StudyProgress> studyProgresses = studyRepository.getStudyProgresses(studyNo);
        if (studyProgresses.isEmpty())
            throw new DataNotFoundException();
        return studyProgresses;
    }

    public void addStudyProgress(int studyNo, StudyProgress studyProgress) {
        if (isStudyProgressExists(studyNo, studyProgress.getStudyWeek()))
            throw new InsertConflictException();
        studyProgress.setStudyNo(studyNo);
        studyRepository.addStudyProgress(studyProgress);
    }

    public void editStudyProgress(int studyNo, int studyWeek, StudyProgressUpdate studyProgressUpdate) {
        if (!isStudyProgressExists(studyNo, studyWeek))
            throw new DataNotFoundException();
        studyProgressUpdate.setOriginStudyNo(studyNo);
        studyProgressUpdate.setOriginStudyWeek(studyWeek);
        studyRepository.editStudyProgress(studyProgressUpdate);
    }

    public void deleteStudyProgress(int studyNo, int weekNo) {
        if (!isStudyProgressExists(studyNo, weekNo))
            throw new DataNotFoundException();
        studyRepository.deleteStudyProgress(studyNo, weekNo);
    }

    private boolean isStudyProgressExists(int studyNo, int weekNo) {
        return studyRepository.isStudyProgressExists(studyNo, weekNo);
    }

}
