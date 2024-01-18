package dmu.dasom.dasom_homepage.service.signup;

import dmu.dasom.dasom_homepage.domain.member.DasomNewMember;
import dmu.dasom.dasom_homepage.repository.RecruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class UserService {
    private final RecruitRepository recruitRepository;
    public UserService(RecruitRepository recruitRepository) {
        this.recruitRepository = recruitRepository;
    }


    public boolean authenticateUser(Map<String, Object> params) {
        // MyBatis를 사용하여 데이터베이스에서 유저 정보 조회
        DasomNewMember userByUniqueCode = recruitRepository.getUserByUniqueCode(params);

        // 유저가 존재하면 인증 성공
        return userByUniqueCode != null ;
    }
}
