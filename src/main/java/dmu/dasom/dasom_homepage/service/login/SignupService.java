package dmu.dasom.dasom_homepage.service.login;

import dmu.dasom.dasom_homepage.domain.member.DasomMember;
import dmu.dasom.dasom_homepage.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignupService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCrypt;

    @Autowired
    public SignupService(MemberRepository memberRepository, BCryptPasswordEncoder bCrypt) {
        this.memberRepository = memberRepository;
        this.bCrypt = bCrypt;
    }

    public boolean saveNewMember(DasomMember newMember) {
        // 이미 가입 된 회원인지 검증
        if (memberRepository.existByEmail(newMember.getMemEmail())) {
            return false;
        } else {
            // 비밀번호 암호화 후 DB 저장 수행
            newMember.setMemPassword(bCrypt.encode(newMember.getMemPassword()));
            memberRepository.saveNewMember(newMember);
            return true;
        }
    }

}
