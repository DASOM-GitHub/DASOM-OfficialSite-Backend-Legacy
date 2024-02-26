package dmu.dasom.dasom_homepage.domain.board.project_study;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectList {
    private int projectNo;
    private String ProjectTitle;
    private String projectContent;
    private String thumbnailPic;
}