package dmu.dasom.dasom_homepage.service.recruit;

import dmu.dasom.dasom_homepage.domain.recruit.*;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
import dmu.dasom.dasom_homepage.exception.InsertConflictException;
import dmu.dasom.dasom_homepage.repository.RecruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ApplicantService {

    private final RecruitRepository recruitRepository;

    @Autowired
    public ApplicantService(RecruitRepository recruitRepository) {
        this.recruitRepository = recruitRepository;
    }

    // 지원자 정보 저장 메소드
    public void saveNewApplicant(int recNo, DasomApplicant dasomApplicant) {
        if (recruitRepository.isRecruitScheduleExistByRecNo(recNo)) {
            if (!isApplicantValid(recNo, dasomApplicant.getAcStudentNo())) {
                dasomApplicant.setRecNo(recNo);
                recruitRepository.saveApplicant(dasomApplicant);
            } else {
                // 지원자 정보 중복 시 409 코드 반환
                throw new InsertConflictException();
            }
        } else {
            // 입력 정보에 해당하는 모집 일정이 없을 경우 404 코드 반환
            throw new DataNotFoundException();
        }
    }

    // 모집 기수에서 해당 학번을 가진 지원자 존재 여부 검증 메소드
    public boolean isApplicantValid(int recNo, int studentNo) {
        return recruitRepository.isApplicantExistByRecNoAndStudentNo(recNo, studentNo);
    }

    // 지원자 리스트 반환 메소드
    public List<DasomApplicantIndex> getApplicantList(int recNo) {
        List<DasomApplicantIndex> applicantList = recruitRepository.getApplicantList(recNo);
        if (applicantList.isEmpty()) throw new DataNotFoundException();
        return applicantList;
    }

    // 지원자 상세 정보 반환 메소드
    public DasomApplicant getApplicant(int recNo, int acStudentNo) {
        DasomApplicant applicant = recruitRepository.getApplicantByStudentNo(recNo, acStudentNo);
        if (applicant == null) throw new DataNotFoundException();
        return applicant;
    }

    // 지원자 정보 업데이트 메소드
    public void updateApplicantInfo(int originRecNo, int originAcStudentNo, DasomApplicantUpdate dasomApplicant) {
        if (isApplicantValid(originRecNo, originAcStudentNo)) {
            dasomApplicant.setOriginRecNo(originRecNo);
            dasomApplicant.setOriginAcStudentNo(originAcStudentNo);
            recruitRepository.updateApplicantInfo(dasomApplicant);
        } else {
            throw new DataNotFoundException();
        }
    }
    //지원자 합격/불합격 업데이트
    public void updateApplicantIsAccepted(int recNo, int acStudentNo, DasomApplicantUpdate dasomApplicant){
        if(isApplicantValid(recNo,acStudentNo)){
            dasomApplicant.setAcStudentNo(acStudentNo);
            recruitRepository.updateApplicatnIsAccepted(dasomApplicant);
        }else{
            throw new DataNotFoundException();
        }
    }

    // 지원자 정보 삭제 메소드
    public void deleteApplicant(int recNo, int acStudentNo) {
        if (isApplicantValid(recNo, acStudentNo)) {
            recruitRepository.deleteApplicantByStudentNo(recNo, acStudentNo);
        } else {
            throw new DataNotFoundException();
        }
    }

    // 합격자 인증코드 생성 및 회원가입 직전 단계 이동 메소드
    public void doAcceptProcess(int recNo) {
        if (!recruitRepository.isRecruitScheduleExistByRecNo(recNo))
            throw new DataNotFoundException();
        recruitRepository.doAcceptProcess(recNo);
    }

}
