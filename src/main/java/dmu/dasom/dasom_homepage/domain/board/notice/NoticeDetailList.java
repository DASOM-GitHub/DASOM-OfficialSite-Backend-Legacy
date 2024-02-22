package dmu.dasom.dasom_homepage.domain.board.notice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class NoticeDetailList {
    private int noticeNo;
    private String noticeTitle;
    private String memName;
    private String noticeContent;
    private String noticePic;
}
