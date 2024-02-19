package dmu.dasom.dasom_homepage.domain.board.project_study;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudyProgressUpdate {
    private int originStudyNo;
    private int originStudyWeek;

    private int studyWeek;
    private String weekContent;
}
