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
    public void saveNewApplicant(DasomApplicant dasomApplicant) {
        // 기수 정보만 따로 넣어줌. 차후 모집일정 연동 예정
        dasomApplicant.setRecNo(32);
        recruitRepository.saveApplicant(dasomApplicant);
    }

    // 지원자 리스트 반환 메소드
    public List<DasomApplicant> getApplicantList() {
        return recruitRepository.getApplicantList();
    }

    // 지원자 중복 여부 검증 메소드
    public boolean isApplicantValid(int studentNo) {
        return recruitRepository.getApplicantByStudentNo(studentNo) != null;
    }

}
