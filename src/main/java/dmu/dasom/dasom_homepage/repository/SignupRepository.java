package dmu.dasom.dasom_homepage.repository;

import dmu.dasom.dasom_homepage.domain.member.SignupDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SignupRepository {
    Boolean isNewMemberExistByCode(String uniqueCode);

    Boolean isNewMemberExistByStudentNo(int recNo,int studentNo);

    Boolean isUniqueCodeExpired(String uniqueCode);

    Boolean existByEmail(String memEmail);

    void saveNewMember(SignupDTO dasomMember);

    void expireUniqueCode(int recNo, int studentNo);
}
