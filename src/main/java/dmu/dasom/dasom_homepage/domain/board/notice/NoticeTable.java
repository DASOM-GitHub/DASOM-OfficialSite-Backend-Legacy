package dmu.dasom.dasom_homepage.domain.board.notice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
public class NoticeTable {

    private int noticeNo;

    private int writerNo;

    private String noticeTitle;

    private String noticeContent;

    private String noticePic;



}
