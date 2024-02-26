package dmu.dasom.dasom_homepage.service.signup;

import dmu.dasom.dasom_homepage.domain.member.SignupDTO;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
import dmu.dasom.dasom_homepage.exception.InsertConflictException;
import dmu.dasom.dasom_homepage.exception.UniqueCodeExpiredException;
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
            throw new DataNotFoundException();

        // 인증코드가 만료되었을 경우
        if (signupRepository.isUniqueCodeExpired(uniqueCode))
            throw new UniqueCodeExpiredException();
    }

    // 부원 인증 후 가입 로직. 가입에 성공하면 인증코드를 만료시킴
    public void saveNewMember(SignupDTO newMember) {
        if (!signupRepository.isNewMemberExistByStudentNo(newMember.getMemRecNo(), newMember.getMemStudentNo()))
            throw new DataNotFoundException();

        // 이미 가입 된 부원인지 검증
        if (signupRepository.existByEmail(newMember.getMemEmail()))
            throw new InsertConflictException();

        // 비밀번호 암호화 후 DB 저장 수행
        newMember.setMemPassword(bCrypt.encode(newMember.getMemPassword()));
        signupRepository.saveNewMember(newMember);
        signupRepository.expireUniqueCode(newMember.getMemRecNo(), newMember.getMemStudentNo());
    }

}
