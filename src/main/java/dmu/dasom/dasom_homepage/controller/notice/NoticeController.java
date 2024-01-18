package dmu.dasom.dasom_homepage.controller.notice;

import ch.qos.logback.core.model.Model;
import dmu.dasom.dasom_homepage.domain.notice.Notice;
import dmu.dasom.dasom_homepage.service.notice.NoticeService;
import lombok.Getter;
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
    public List<Notice> findNoticeAll() {
        return noticeService.findNoticeDateDesc();
    }

    @GetMapping("/search")
    public List<Notice> findNoticeTitle(@RequestParam(value = "title") String noticeTitle) {
        return noticeService.findNoticeTitle(noticeTitle);
    }
}
