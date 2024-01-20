package dmu.dasom.dasom_homepage.repository;

import dmu.dasom.dasom_homepage.domain.member.DasomMember;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberRepository {
    Boolean existByEmail(String memEmail);

    void saveNewMember(DasomMember dasomMember);

    DasomMember getMemberByEmail(String memEmail);

}
