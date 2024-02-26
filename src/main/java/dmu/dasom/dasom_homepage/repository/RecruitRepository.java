package dmu.dasom.dasom_homepage.repository;

import dmu.dasom.dasom_homepage.domain.recruit.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface RecruitRepository {
    void createNewRecruitSchedule(RecruitSchedule recruitSchedule);

    List<RecruitScheduleIndex> getRecruitScheduleList();

    RecruitSchedule getRecruitScheduleDetails(int recNo);

    void updateRecruitSchedule(RecruitSchedule recruitSchedule);

    void deleteRecruitSchedule(int recNo);

    Boolean isRecruitScheduleExistByRecNo(int RecNo);

    void saveApplicant(DasomApplicant dasomApplicant);

    List<DasomApplicantIndex> getApplicantList(int recNo);

    DasomApplicant getApplicantByStudentNo(int recNo, int acStudentNo);

    void updateApplicantInfo(DasomApplicantUpdate dasomApplicant);
    void updateApplicatnIsAccepted(DasomApplicantUpdate dasomApplicant);
    void deleteApplicantByStudentNo(int recNo, int acStudentNo);

    Boolean isApplicantExistByRecNoAndStudentNo(int recNo, int acStudentNo);

    void doAcceptProcess(int recNo);
}
