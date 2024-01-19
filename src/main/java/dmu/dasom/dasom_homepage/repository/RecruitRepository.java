package dmu.dasom.dasom_homepage.repository;

import dmu.dasom.dasom_homepage.domain.member.DasomMember;
import dmu.dasom.dasom_homepage.domain.member.DasomNewMember;
import dmu.dasom.dasom_homepage.domain.recruit.DasomApplicant;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface RecruitRepository {
    void saveApplicant(DasomApplicant dasomApplicant);

    List<DasomApplicant> getApplicantList();

    DasomApplicant getApplicantByStudentNo(int studentNo);

    void updateApplicantInfo(DasomApplicant dasomApplicant);

    void deleteApplicantByStudentNo(int studentNo);

    DasomNewMember getUserByUniqueCode(Map<String, Object> uniqueCode);

    void insertUser(DasomMember dasomMember);
}
