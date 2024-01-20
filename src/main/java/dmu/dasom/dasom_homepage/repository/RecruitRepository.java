package dmu.dasom.dasom_homepage.repository;

import dmu.dasom.dasom_homepage.domain.recruit.DasomApplicant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RecruitRepository {
    void saveApplicant(DasomApplicant dasomApplicant);

    List<DasomApplicant> getApplicantList();

    DasomApplicant getApplicantByStudentNo(int studentNo);
}
