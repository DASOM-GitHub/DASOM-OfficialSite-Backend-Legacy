package dmu.dasom.dasom_homepage.service.signup;

import dmu.dasom.dasom_homepage.domain.member.DasomMember;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
import dmu.dasom.dasom_homepage.exception.InsertConflictException;
import dmu.dasom.dasom_homepage.repository.SignupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignupService {
    private final SignupRepository signupRepository;
    private final BCryptPasswordEncoder bCrypt;

    @Autowired
    public SignupService(SignupRepository signupRepository, BCryptPasswordEncoder bCrypt) {
        this.signupRepository = signupRepository;
        this.bCrypt = bCrypt;
    }

    // 인증코드 검증 로직
    public void verifyNewMember(String uniqueCode) {
        // DB에서 해당 인증코드를 가진 지원자 등록 여부 조회
        if (!signupRepository.isNewMemberExistByCode(uniqueCode))
            throw new DataNotFoundException("부원 인증 실패");
    }

    // 부원 인증 후 가입 로직
    public void saveNewMember(DasomMember newMember) {
        // 이미 가입 된 부원인지 검증
        if (signupRepository.existByEmail(newMember.getMemEmail())) {
            throw new InsertConflictException("이미 가입 한 부원입니다");
        } else {
            // 비밀번호 암호화 후 DB 저장 수행
            newMember.setMemPassword(bCrypt.encode(newMember.getMemPassword()));
            signupRepository.saveNewMember(newMember);
        }
    }

}
