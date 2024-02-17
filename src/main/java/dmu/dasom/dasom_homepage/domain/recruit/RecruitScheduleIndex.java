package dmu.dasom.dasom_homepage.domain.recruit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class RecruitScheduleIndex {
    private int recNo;
    private String applyStart;
    private String firstAnnounce;
    private String secondAnnounce;
}
