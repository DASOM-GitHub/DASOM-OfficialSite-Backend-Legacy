package dmu.dasom.dasom_homepage.domain.board.notice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class NoticeList {
    private int noticeNo;
    private String memName;
    private String noticeTitle;
    private LocalDateTime noticeRegisterDate;
}
