package dmu.dasom.dasom_homepage.domain.board.project_study;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudyList {
    private int studyNo;
    //private int writerNo;
    //private int organizerNo;
    private String studyTitle;
    private String studyContent;
    //private String projectCategory;
    private String studyPic;
    //private String startDate;
    //private String endDate;
}