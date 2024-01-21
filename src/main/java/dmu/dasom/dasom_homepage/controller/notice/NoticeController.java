package dmu.dasom.dasom_homepage.controller.notice;

import dmu.dasom.dasom_homepage.domain.notice.NoticeDetailList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeTable;
import dmu.dasom.dasom_homepage.service.notice.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) { this.noticeService = noticeService; }

    @GetMapping("/list")
    public List<NoticeList> findNoticeAll() {
        return noticeService.findNoticeDateDesc();
    }

    @GetMapping("/search")
    public List<NoticeList> findNoticeTitle(@RequestParam(value = "title") String noticeTitle) {
        return noticeService.findNoticeTitle(noticeTitle);
    }

    @GetMapping("/list/detail")
    public NoticeDetailList detailPage(@RequestParam(value="pageNo") int noticeNo) {
        return noticeService.detailNoticePage(noticeNo);
    }

    @GetMapping("/create")
    public String createNotice(NoticeTable noticeTable){
        System.out.println("테스트 ");
        return noticeService.createNotice(noticeTable);
    }

    @GetMapping("/update")
    public String updateNotice(@ModelAttribute NoticeTable noticeTable){
        System.out.println(noticeTable.getNoticeTitle());
        return noticeService.updateNotice(noticeTable);
    }

}
