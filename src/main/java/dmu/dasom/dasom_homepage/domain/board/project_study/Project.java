package dmu.dasom.dasom_homepage.domain.board.project_study;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Project {
    private int projectNo;
    private String projectTitle;
    private String projectContent;
    private String gitUrl;
    private String notionUrl;
    private String thumbnailPic;
    private String projectPic;
    private String startDate;
    private String endDate;
}
