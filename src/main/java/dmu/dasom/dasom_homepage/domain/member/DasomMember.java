package dmu.dasom.dasom_homepage.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class DasomMember {
    private int memNo;
    private String memEmail;
    private String memPassword;
    private String memName;
    private int memGrade;
    private String memDepartment;
    private int memRecNo;
    private String memSocialId;
    private String memSocialEmail;
    private String memProfilePic;
    private String memRole;
    private String memState;
    private String signupDate;
    private String leaveDate;
    private String stateUpdate;
}
