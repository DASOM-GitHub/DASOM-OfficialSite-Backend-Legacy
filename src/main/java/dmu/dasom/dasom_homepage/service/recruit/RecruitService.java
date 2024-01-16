package dmu.dasom.dasom_homepage.service.recruit;

import dmu.dasom.dasom_homepage.domain.recruit.DasomApplicant;
import dmu.dasom.dasom_homepage.repository.RecruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // 지원자 정보 저장 메소드
    public String saveNewApplicant(DasomApplicant dasomApplicant) {
        // 지원자 학번이 중복 될 경우
        if (isApplicantValid(dasomApplicant.getAcStudentNo())) {
            return "!! 지원자 중복됨 !!";
        } else {
            // 기수 정보만 따로 넣어줌. 차후 모집일정 연동 예정
            dasomApplicant.setRecNo(32);
            recruitRepository.saveApplicant(dasomApplicant);
            return "** 지원자 저장 성공 **";
        }
    }

    // 지원자 중복 여부 검증 메소드
    public boolean isApplicantValid(int studentNo) {
        // 매개변수로 받아온 학번에 해당하는 지원자 여부를 판단하여 반환
        return recruitRepository.getApplicantByStudentNo(studentNo) != null;
    }

    // 지원자 리스트 반환 메소드
    public List<DasomApplicant> getApplicantList() {
        // 지원자 리스트를 DB에서 받아옴
        List<DasomApplicant> applicantList = recruitRepository.getApplicantList();
        return applicantList;
    }

    // 특정 지원자 정보 반환 메소드
    public DasomApplicant getApplicant(int acStudentNo) {
        // 특정 지원자의 정보를 학번을 기준으로 DB에서 찾아옴
        DasomApplicant applicant = recruitRepository.getApplicantByStudentNo(acStudentNo);
        return applicant;
    }

    // 지원자 정보 업데이트 메소드
    public String updateApplicantInfo(int acStudentNo, DasomApplicant dasomApplicant) {
        // 매개변수로 받아온 학번에 해당하는 지원자가 있는지 확인
        if (isApplicantValid(acStudentNo)) { // 해당하는 지원자가 있다면 업데이트 수행
            recruitRepository.updateApplicantInfo(dasomApplicant);
            return "지원자 정보 업데이트 성공";
        } else {
            return "해당 정보에 해당하는 지원자 없음";
        }
    }

    // 특정 지원자 삭제 메소드
    public String deleteApplicant(int acStudentNo) {
        // 매개변수로 받아온 학번에 해당하는 지원자가 있는지 확인
        if (isApplicantValid(acStudentNo)) { // 해당하는 지원자가 있다면 삭제 수행
            recruitRepository.deleteApplicantByStudentNo(acStudentNo);
            return "지원자 정보 삭제 성공";
        } else {
            return "해당 정보에 해당하는 지원자 없음";
        }
    }

}
