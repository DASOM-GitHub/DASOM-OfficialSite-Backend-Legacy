package dmu.dasom.dasom_homepage.domain.notice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter @Setter
@NoArgsConstructor
public class NoticeTable {

    private String noticeNo;

    private String writerNo;

    private String noticeTitle;

    private String noticeContent;

    private String noticePic;


}
