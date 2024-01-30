package dmu.dasom.dasom_homepage.controller.notice;

import dmu.dasom.dasom_homepage.domain.notice.NoticeDetailList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeTable;
import dmu.dasom.dasom_homepage.service.notice.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping("/create")
    public String createNoticeTest(@ModelAttribute NoticeTable noticeTable, @RequestParam(value = "noticeFile") MultipartFile noticeFile) throws Exception {

        return noticeService.createNotice(noticeTable, noticeFile);
    }

    // notice 수정
    @PostMapping("/update")
    public String updateNotice(@ModelAttribute NoticeTable noticeTable, @RequestParam(value = "noticeFile") MultipartFile noticeFile) throws Exception {

        return noticeService.updateNotice(noticeTable, noticeFile);
    }
    // notice 삭제
    @DeleteMapping("/delete")
    public String deleteNotice(@RequestParam(value="pageNo") int noticeNo){
        return noticeService.deleteNotice(noticeNo);
    }


}

