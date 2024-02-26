package dmu.dasom.dasom_homepage.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@NoArgsConstructor
public class DasomNewMember {
    private String acStudentNo;
    private String uniqueCode;
    private boolean isCodeExpired;
}
