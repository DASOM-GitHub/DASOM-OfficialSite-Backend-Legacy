package dmu.dasom.dasom_homepage.repository;

import dmu.dasom.dasom_homepage.domain.member.DasomMember;
import dmu.dasom.dasom_homepage.domain.member.DasomMemberIndex;
import dmu.dasom.dasom_homepage.domain.member.MyPageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MemberRepository {

    DasomMember getMemberByEmail(String memEmail);

    MyPageDTO getMyPageInfoByEmail(String memEmail);

    void updateMyPageInfo(MyPageDTO myPageDTO);

    void updateMyProfilePic(String profilePicUrl, String memEmail);

    void updateMyPw(String newPw, String memEmail);

    String getMyPw(String memEmail);

    List<DasomMemberIndex> getMemberIndexList();

}
