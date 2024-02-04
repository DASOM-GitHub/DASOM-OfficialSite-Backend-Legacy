package dmu.dasom.dasom_homepage.domain.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Project {
    private int projectNo;
    private int writerNo;
    private int organizerNo;
    private String ProjectTitle;
    private String projectContent;
    private String projectCategory;
    private String projectPic;
    private String startDate;
    private String endDate;
}
