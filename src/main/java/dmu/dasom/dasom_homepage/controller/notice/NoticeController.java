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
    // notice 전체 조회(목록)
    @GetMapping("/list")
    public List<NoticeList> findNoticeAll() {
        return noticeService.findNoticeDateDesc();
    }
    // notice 제목 기반 조회
    @GetMapping("/search")
    public List<NoticeList> findNoticeTitle(@RequestParam(value = "title") String noticeTitle) {
        return noticeService.findNoticeTitle(noticeTitle);
    }
    // notice 상세 페이지
    @GetMapping("/list/detail")
    public NoticeDetailList detailPage(@RequestParam(value="pageNo") int noticeNo) {
        return noticeService.detailNoticePage(noticeNo);
    }
    // notice 등록
    @GetMapping("/create")
    public String createNotice(@ModelAttribute NoticeTable noticeTable){
        System.out.println("테스트 ");
        System.out.println(noticeTable.getNoticeTitle());
        return noticeService.createNotice(noticeTable);
    }
    // notice 수정
    @GetMapping("/update")
    public String updateNotice(@ModelAttribute NoticeTable noticeTable){
        System.out.println(noticeTable.getNoticeTitle());
        return noticeService.updateNotice(noticeTable);
    }
    // notice 삭제
    @GetMapping("/delete")
    public String deleteNotice(@RequestParam(value="pageNo") int noticeNo){
        return noticeService.deleteNotice(noticeNo);
    }
}
