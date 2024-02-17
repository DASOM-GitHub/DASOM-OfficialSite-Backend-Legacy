package dmu.dasom.dasom_homepage.domain.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberState {
    private int memNo;

    private String memName;

    private int memGrade;

    private String memState;

    private String leaveDate;

    private String stateUpdate;

    private String memDepartment;

}
