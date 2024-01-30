package dmu.dasom.dasom_homepage.repository;

import dmu.dasom.dasom_homepage.domain.admin.MemberState;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AdminRepository {
    void deleteMember(MemberState memberState);

    void modifyMember(MemberState memberState);

    void stateChange(MemberState memberState);
    List<MemberState> getMemberList();

}
