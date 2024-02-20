package dmu.dasom.dasom_homepage.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@NoArgsConstructor
public class DasomMemberIndex {
    private int memNo;
    private String memName;
    private int memRecNo;
    private String memRole;
    private String memProfilePic;
}
