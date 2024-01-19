package dmu.dasom.dasom_homepage.service.signup;

import dmu.dasom.dasom_homepage.domain.member.DasomMember;
import dmu.dasom.dasom_homepage.domain.recruit.DasomApplicant;
import dmu.dasom.dasom_homepage.repository.RecruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SignupMemberService {

    private final RecruitRepository recruitRepository;
    private dmu.dasom.dasom_homepage.domain.member.DasomMember DasomMember;

    public SignupMemberService(RecruitRepository recruitRepository) {
        this.recruitRepository = recruitRepository;
    }

    //신입부원 정보 저장 메소드
    public String saveSignupMember(DasomMember paramMap) {
        recruitRepository.insertUser(DasomMember);
        return "** 신입부원 저장 성공**";
    }

}
