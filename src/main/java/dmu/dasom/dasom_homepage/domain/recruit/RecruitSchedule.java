package dmu.dasom.dasom_homepage.domain.recruit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class RecruitSchedule {
    private int recNo;
    private String applyStart;
    private String applyEnd;
    private String firstAnnounce;
    private String interviewStart;
    private String interviewEnd;
    private String secondAnnounce;
}
