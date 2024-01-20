package dmu.dasom.dasom_homepage.controller.notice;

import dmu.dasom.dasom_homepage.domain.notice.NoticeCreate;
import dmu.dasom.dasom_homepage.domain.notice.NoticeDetailList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeList;
import dmu.dasom.dasom_homepage.repository.NoticeRepository;
import dmu.dasom.dasom_homepage.service.notice.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public String noticeCreate(NoticeCreate create){
        return noticeService.noticeCreate(create);
    }

}
