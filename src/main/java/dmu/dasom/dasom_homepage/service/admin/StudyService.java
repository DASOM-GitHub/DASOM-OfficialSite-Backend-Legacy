package dmu.dasom.dasom_homepage.service.admin;

import dmu.dasom.dasom_homepage.domain.admin.*;
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
import java.util.Optional;

@Service
public class StudyService {
    private final StudyRepository studyRepository;
    private S3UploadService s3UploadService;

    @Autowired
    public StudyService(StudyRepository studyRepository, S3UploadService s3UploadService){
        this.studyRepository = studyRepository;
        this.s3UploadService = s3UploadService;
    }

    //모든 유저가 접근 : 모든 study 반환 select
    public List<StudyList> getStudys(){
        List<StudyList> studyLists = studyRepository.getStudys();
        if(studyLists.size() < 0){
            throw new DataNotFoundException();
        }else{
            return studyLists;
        }
    }

    // 한개의 상세 study 반환
    public Study getStudyById(int study_no){
        //존재 확인
        if(!isStudyById(study_no)){
            // 해당 study없음
            throw new DataNotFoundException();
        }else{ return studyRepository.findStudyById(study_no); }
    }

    // admin 접근 : create new Study | edit study
    public void createStudy(Study study, MultipartFile studyFile) throws  IOException {
        //파일 저장
        String fileUrl = s3UploadService.saveFile(studyFile);
        study.setStudyPic(fileUrl);

         if(!isCategory(study.getStudyCategory())){
            // category가 존재하지 않음
            throw new ProjectException();
        } else {
            //Study 생성
            studyRepository.createStudy(study);
        }
    }

    //admin유저 접근 : 수정 : update
    public Study editStudy(Study study, MultipartFile studyFile) throws IOException{
        if(!isCategory(study.getStudyCategory())){
            // category를 찾을수 없음
            throw new ProjectException();
        } else if(!isStudyById(study.getStudyNo())){
            //수정할 study를 찾을수 없음
            throw new DataNotFoundException();
        }else{
            // 수정
            String fileUrl = s3UploadService.saveFile(studyFile);
            study.setStudyPic(fileUrl);

            studyRepository.editStudy(study);
            return study;
        }
    }

    //admin유저 접근 : 삭제 : delete study
    public void removeStudy(int studyNo){
        if(!isStudyById(studyNo)){
            //삭제할 study를 찾을 수 없음
            throw new DataNotFoundException();
//        }else if(studyRepository.getParticipants(studyNo).size() > 0) {
//            // 해당 study의 부원이 아직 존재할 경우\
//
//            throw new ProjectException(); //bad_request 반환
        }else{
            //study 삭제
            studyRepository.removeStudy(studyNo);
        }
    }

    //존재 유무 확인 select , 사용 함수 : 수정, 삭제
    private boolean isStudyById(int study_no){
        Optional<Study> pro =  Optional.ofNullable(studyRepository.findStudyById(study_no));
        return pro.isPresent();
    }

    //---------------------부원 부분--------------------

    public List<StudyParticipants> getParticipants(int study_no) {
        //해당 프로젝트가 있는지 여부 확인
        if (!isStudyById(study_no)) {
            throw new DataNotFoundException();
//        } else if (){
//            // 참가자가 없을때
//            throw new E();
        } else{
            return studyRepository.getParticipants(study_no);
        }
    }

    public void addParticipant(StudyParticipants studyParticipants) {

        if (!isStudyById(studyParticipants.getStudyNo())) {
            // study가 없을시
            throw new DataNotFoundException();
        } else if (isParticipant(studyParticipants.getStudyNo(), studyParticipants.getParticipantNo())) {
            // 이미 해당 참가자가 있는경우
            throw new InsertConflictException();
        }else if(!isRole(studyParticipants.getParticipantRole())) {
            //알맞지 않은 role
            throw new ProjectException(); //bad_request반환
        }else {
            studyRepository.addParticipant(studyParticipants);
        }
    }

    public void removeParticipant(int studyNo, int participantNo){
        if (!isStudyById(studyNo)) {
            // study가 없을시
            throw new DataNotFoundException();
        } else if (!isParticipant(studyNo, participantNo)) {
            // 이미 해당 참가자를 찾을수 없는 경우
            throw new ProjectException(); // bad_request반환
        }else {
            studyRepository.removeParticipant(studyNo,participantNo);
        }
    }

    private boolean isParticipant(int studyNo, int participantNo){
        return Optional.ofNullable(studyRepository.getParticipant(studyNo, participantNo)).isPresent();
    }

    //-------------------------category|role-------------------------

    private boolean isCategory(String categoryName){
        Optional<Category> ca = Optional.ofNullable(studyRepository.getCategoryByName(categoryName));
        return ca.isPresent();
    }

    private boolean isRole(String role_name){
        Optional<Role> role = Optional.ofNullable(studyRepository.getRoleByName(role_name));
        return role.isPresent();
    }
}
