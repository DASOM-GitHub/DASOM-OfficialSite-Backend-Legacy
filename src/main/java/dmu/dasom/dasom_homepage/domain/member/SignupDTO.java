package dmu.dasom.dasom_homepage.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SignupDTO {
    private int memStudentNo;
    private String memEmail;
    private String memPassword;
    private String memName;
    private int memGrade;
    private String memDepartment;
    private int memRecNo;
}
