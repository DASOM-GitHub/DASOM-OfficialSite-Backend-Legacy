package dmu.dasom.dasom_homepage.service.recruit;

import dmu.dasom.dasom_homepage.domain.recruit.*;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
import dmu.dasom.dasom_homepage.exception.InsertConflictException;
import dmu.dasom.dasom_homepage.repository.RecruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecruitService {

    private final RecruitRepository recruitRepository;

    @Autowired
    public RecruitService(RecruitRepository recruitRepository) {
        this.recruitRepository = recruitRepository;
    }

    // 부원 모집 스케줄 추가 메소드
    public void createNewRecruitSchedule(RecruitSchedule recruitSchedule) {
        // 데이터 삽입 시 모집 기수 번호 (PK)가 중복 될 경우 예외 발생, 예외 발생 시 응답 예외 throw
        try {
            recruitRepository.createNewRecruitSchedule(recruitSchedule);
        } catch (DuplicateKeyException e) {
            throw new InsertConflictException("모집 일정 중복 됨");
        }
    }

    // 부원 모집 스케줄 리스트 반환 메소드
    public List<RecruitScheduleIndex> getRecruitScheduleList() {
        List<RecruitScheduleIndex> recruitScheduleList = recruitRepository.getRecruitScheduleList();
        if (recruitScheduleList.isEmpty()) {
            throw new DataNotFoundException("모집 일정 목록 없음");
        } else {
            return recruitScheduleList;
        }
    }

    // 부원 모집 스케줄 상세 정보 반환 메소드
    public RecruitSchedule getRecruitScheduleDetails(int recNo) {
        RecruitSchedule recruitSchedule = recruitRepository.getRecruitScheduleDetails(recNo);
        if (recruitSchedule == null) {
            throw new DataNotFoundException("검색 정보에 해당하는 모집 일정 없음");
        } else {
            return recruitSchedule;
        }
    }

    // 부원 모집 스케줄 정보 업데이트 메소드
    public void updateRecruitSchedule(int recNo, RecruitSchedule recruitSchedule) {
        if (recruitRepository.isRecruitScheduleExistByRecNo(recNo)) {
            recruitSchedule.setRecNo(recNo);
            recruitRepository.updateRecruitSchedule(recruitSchedule);
        } else {
            throw new DataNotFoundException("업데이트 정보에 해당하는 모집 일정 없음");
        }
    }

    // 지원자 정보 저장 메소드
    public void saveNewApplicant(int recNo, DasomApplicant dasomApplicant) {
        if (recruitRepository.isRecruitScheduleExistByRecNo(recNo)) {
            if (!isApplicantValid(recNo, dasomApplicant.getAcStudentNo())) {
                dasomApplicant.setRecNo(recNo);
                recruitRepository.saveApplicant(dasomApplicant);
            } else {
                // 지원자 정보 중복 시 409 코드 반환
                throw new InsertConflictException("지원자 중복 됨");
            }
        } else {
            // 입력 정보에 해당하는 모집 일정이 없을 경우 404 코드 반환
            throw new DataNotFoundException("입력 정보에 해당하는 모집 일정 없음");
        }
    }

    // 모집 기수에서 해당 학번을 가진 지원자 존재 여부 검증 메소드
    public boolean isApplicantValid(int recNo, int studentNo) {
        return recruitRepository.isApplicantExistByRecNoAndStudentNo(recNo, studentNo);
    }

    // 지원자 리스트 반환 메소드
    public List<DasomApplicantIndex> getApplicantList(int recNo) {
        List<DasomApplicantIndex> applicantList = recruitRepository.getApplicantList(recNo);
        if (applicantList.isEmpty()) throw new DataNotFoundException("지원자 데이터 없음");
        return applicantList;
    }

    // 지원자 상세 정보 반환 메소드
    public DasomApplicant getApplicant(int recNo, int acStudentNo) {
        DasomApplicant applicant = recruitRepository.getApplicantByStudentNo(recNo, acStudentNo);
        if (applicant == null) throw new DataNotFoundException("해당 정보에 해당하는 지원자 없음");
        return applicant;
    }

    // 지원자 정보 업데이트 메소드
    public void updateApplicantInfo(int originRecNo, int originAcStudentNo, DasomApplicantUpdate dasomApplicant) {
        if (isApplicantValid(originRecNo, originAcStudentNo)) {
            dasomApplicant.setOriginRecNo(originRecNo);
            dasomApplicant.setOriginAcStudentNo(originAcStudentNo);
            recruitRepository.updateApplicantInfo(dasomApplicant);
        } else {
            throw new DataNotFoundException("해당 정보에 해당하는 지원자 없음");
        }
    }

    // 지원자 정보 삭제 메소드
    public void deleteApplicant(int recNo, int acStudentNo) {
        if (isApplicantValid(recNo, acStudentNo)) {
            recruitRepository.deleteApplicantByStudentNo(recNo, acStudentNo);
        } else {
            throw new DataNotFoundException("해당 정보에 해당하는 지원자 없음");
        }
    }

}
