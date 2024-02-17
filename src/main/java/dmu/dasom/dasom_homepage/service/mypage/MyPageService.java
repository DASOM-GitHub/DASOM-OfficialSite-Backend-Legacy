package dmu.dasom.dasom_homepage.service.mypage;

import dmu.dasom.dasom_homepage.auth.jwt.JwtUtil;
import dmu.dasom.dasom_homepage.domain.member.MyPageDTO;
import dmu.dasom.dasom_homepage.exception.PwUpdateErrorException;
import dmu.dasom.dasom_homepage.repository.MemberRepository;
import dmu.dasom.dasom_homepage.service.s3.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Transactional
public class MyPageService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final S3UploadService s3UploadService;
    private final BCryptPasswordEncoder bCrypt;

    @Autowired
    public MyPageService(MemberRepository memberRepository, JwtUtil jwtUtil, S3UploadService s3UploadService, BCryptPasswordEncoder bCrypt) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.s3UploadService = s3UploadService;
        this.bCrypt = bCrypt;
    }

    public MyPageDTO getMemInfo(String authorization) {
        // 토큰을 기반으로 요청받은 사용자를 판단
        return memberRepository.getMyPageInfoByEmail(jwtUtil.getUsername(jwtUtil.parseToken(authorization)));
    }

    public void updateMemInfo(MyPageDTO updateInfo, String authorization) {
        // 토큰을 기반으로 업데이트 하고자 하는 사용자를 판단
        updateInfo.setMemEmail(jwtUtil.getUsername(jwtUtil.parseToken(authorization)));
        memberRepository.updateMyPageInfo(updateInfo);
    }

    public void updateMemProfilePic(MultipartFile profilePic, String authorization) throws IOException {
        // 요청 된 사진 파일이 없을 경우, 기존 프로필 사진을 삭제 함
        if (profilePic == null)
            s3UploadService.deleteFile(getMemInfo(authorization).getMemProfilePic());

        // 토큰을 기반으로 프로필 사진을 업데이트 하고자 하는 사용자를 판단
        // 클라우드 스토리지에 사진을 저장하고 그 사진의 주소를 받아 DB에 저장함
        memberRepository.updateMyProfilePic(s3UploadService.saveFile(profilePic), jwtUtil.getUsername(jwtUtil.parseToken(authorization)));
    }

    public void updateMemPw(Map<String, String> reqBody, String authorization) {
        // 토큰을 기반으로 비밀번호를 변경하고자 하는 사용자를 판단
        String username = jwtUtil.getUsername(jwtUtil.parseToken(authorization));
        // 클라이언트로부터 온 현재 비밀번호가 DB에 있는 비밀번호와 일치하는지 확인
        if (!bCrypt.matches(reqBody.get("originPw"), memberRepository.getMyPw(username)))
            throw new PwUpdateErrorException();
        // 새로운 비밀번호를 암호화 시켜 DB에 업데이트
        memberRepository.updateMyPw(bCrypt.encode(reqBody.get("newPw")), username);
    }

}
