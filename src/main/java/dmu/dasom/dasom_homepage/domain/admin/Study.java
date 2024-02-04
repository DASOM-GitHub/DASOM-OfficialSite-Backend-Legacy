package dmu.dasom.dasom_homepage.domain.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Study {
    private int studyNo;
    private int writerNo;
    private int organizerNo;
    private String studyTitle;
    private String studyContent;
    private String studyCategory;
    private String studyPic;
    private String startDate;
    private String endDate;
}
