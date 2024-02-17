package dmu.dasom.dasom_homepage.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MyPageDTO {
    private String memEmail; // 아이디 (이메일)
    private String memName; // 이름
    private int memGrade; // 학년
    private String memDepartment; // 학과
    private int memRecNo; // 기수
    private String memProfilePic; // 프로필 사진
}
