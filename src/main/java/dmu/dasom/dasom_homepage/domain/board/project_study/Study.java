package dmu.dasom.dasom_homepage.domain.board.project_study;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
public class Study {
    private int studyNo;
    private String studyTitle;
    private String studyContent;
    private String referencesUrl;
    private String notionUrl;
    private String thumbnailPic;
    private String studyPic;
    private String startDate;
    private String endDate;
}
