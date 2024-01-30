package dmu.dasom.dasom_homepage.controller.notice;

import dmu.dasom.dasom_homepage.domain.notice.NoticeDetailList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeTable;
import dmu.dasom.dasom_homepage.domain.recruit.DasomApplicantIndex;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import dmu.dasom.dasom_homepage.service.notice.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<NoticeList>>> findNoticeAll() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, noticeService.findNoticeDateDesc()));
    }

    // notice 제목 기반 조회
    @GetMapping("/title")
    public ResponseEntity<ApiResponse<List<NoticeList>>> findNoticeTitle(@RequestParam(value = "title") String noticeTitle) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, noticeService.findNoticeTitle(noticeTitle)));
    }
    // notice 상세 페이지
    @GetMapping("/{noticeNo}")
    public ResponseEntity<ApiResponse<NoticeDetailList>> detailPage(@PathVariable("noticeNo") int noticeNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, noticeService.detailNoticePage(noticeNo)));
    }

    // notice 등록
    @PostMapping("/{noticeNo}")
    public ResponseEntity<ApiResponse<String>> createNoticeTest(@PathVariable("noticeNo") int noticeNo, @ModelAttribute NoticeTable noticeTable, @RequestParam(value = "noticeFile") MultipartFile noticeFile) throws Exception {

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,noticeService.createNotice(noticeTable, noticeFile)));
    }

    // notice 수정
    @PostMapping("/update")
    public String updateNotice(@ModelAttribute NoticeTable noticeTable, @RequestParam(value = "noticeFile") MultipartFile noticeFile) throws Exception {

        return noticeService.updateNotice(noticeTable, noticeFile);
    }
    // notice 삭제
    @DeleteMapping("/{noticeNo}")
    public ResponseEntity<ApiResponse<String>> deleteNotice(@PathVariable("noticeNo") int noticeNo){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, noticeService.deleteNotice(noticeNo)));
    }


}


