package dmu.dasom.dasom_homepage.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DasomMember {
    private String memEmail;
    private String memGrade;
    private String memPassword;
    private String major;
    private String recno = null ;
}
