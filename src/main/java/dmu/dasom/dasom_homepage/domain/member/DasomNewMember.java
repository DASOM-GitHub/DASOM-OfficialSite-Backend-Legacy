package dmu.dasom.dasom_homepage.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class DasomNewMember {
    private String acStudentNo;
    private String uniqueCode;
    private boolean isOriginMember;
    private boolean isCodeExpired;
}
