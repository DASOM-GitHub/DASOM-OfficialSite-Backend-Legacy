package dmu.dasom.dasom_homepage.domain.recruit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class DasomApplicantIndex {
    private int acNo;
    private int recNo;
    private int acStudentNo;
    private String acName;
    private String acDepartment;
    private int acGrade;
    private int isAccepted;
}
