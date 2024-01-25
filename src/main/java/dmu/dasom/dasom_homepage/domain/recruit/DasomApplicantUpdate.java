package dmu.dasom.dasom_homepage.domain.recruit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class DasomApplicantUpdate {
    private int originRecNo;
    private int originAcStudentNo;

    private int acNo;
    private int acStudentNo;
    private String acName;
    private String acContact;
    private String acEmail;
    private String acDepartment;
    private int acGrade;
    private String reasonForApply;
    private int recNo;
    private String applyDate;
}
