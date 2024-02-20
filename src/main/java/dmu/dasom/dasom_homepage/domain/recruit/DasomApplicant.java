package dmu.dasom.dasom_homepage.domain.recruit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class DasomApplicant {
    private int acNo;
    private int recNo;
    private int acStudentNo;
    private String acName;
    private String acContact;
    private String acEmail;
    private String acDepartment;
    private int acGrade;
    private String reasonForApply;
    private String applyDate;
    private Boolean isAccepted;
}
