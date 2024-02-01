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
public class RecruitScheduleService {

    private final RecruitRepository recruitRepository;

    @Autowired
    public RecruitScheduleService(RecruitRepository recruitRepository) {
        this.recruitRepository = recruitRepository;
    }

    // 부원 모집 스케줄 추가 메소드
    public void createNewRecruitSchedule(RecruitSchedule recruitSchedule) {
        // 데이터 삽입 시 모집 기수 번호 (PK)가 중복 될 경우 예외 발생, 예외 발생 시 응답 예외 throw
        try {
            recruitRepository.createNewRecruitSchedule(recruitSchedule);
        } catch (DuplicateKeyException e) {
            throw new InsertConflictException();
        }
    }

    // 부원 모집 스케줄 리스트 반환 메소드
    public List<RecruitScheduleIndex> getRecruitScheduleList() {
        List<RecruitScheduleIndex> recruitScheduleList = recruitRepository.getRecruitScheduleList();
        if (recruitScheduleList.isEmpty()) {
            throw new DataNotFoundException();
        } else {
            return recruitScheduleList;
        }
    }

    // 부원 모집 스케줄 상세 정보 반환 메소드
    public RecruitSchedule getRecruitScheduleDetails(int recNo) {
        RecruitSchedule recruitSchedule = recruitRepository.getRecruitScheduleDetails(recNo);
        if (recruitSchedule == null) {
            throw new DataNotFoundException();
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
            throw new DataNotFoundException();
        }
    }

    // 부원 모집 스케줄 삭제 메소드
    public void deleteRecruitSchedule(int recNo) {
        if (recruitRepository.isRecruitScheduleExistByRecNo(recNo)) {
            recruitRepository.deleteRecruitSchedule(recNo);
        } else {
            throw new DataNotFoundException();
        }
    }

}
