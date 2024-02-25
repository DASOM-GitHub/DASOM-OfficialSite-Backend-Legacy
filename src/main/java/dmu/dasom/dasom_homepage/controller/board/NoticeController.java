package dmu.dasom.dasom_homepage.controller.board;

import dmu.dasom.dasom_homepage.domain.board.notice.NoticeDetailList;
import dmu.dasom.dasom_homepage.domain.board.notice.NoticeList;
import dmu.dasom.dasom_homepage.domain.board.notice.NoticeTable;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import dmu.dasom.dasom_homepage.service.board.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/board/notice")
@PreAuthorize("hasRole('MANAGER')")
public class NoticeController {
    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) { this.noticeService = noticeService; }

    // notice 전체 조회(목록)
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<NoticeList>>> findNoticeAll() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, noticeService.findNoticeDateDesc()));
    }

    // notice 제목 기반 조회
    @PostMapping("/title")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<NoticeList>>> findNoticeTitle(@RequestBody Map<String, String> noticeTitle) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, noticeService.findNoticeTitle(noticeTitle.get("noticeTitle"))));
    }
    // notice 상세 페이지
    @GetMapping("/{noticeNo}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<NoticeDetailList>> detailPage(@PathVariable("noticeNo") int noticeNo) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, noticeService.detailNoticePage(noticeNo)));
    }

    // notice 등록
    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> createNotice(@RequestBody NoticeTable noticeTable) {
        noticeService.createNotice(noticeTable);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true));
    }

    // notice 수정
    @PutMapping("/{noticeNo}")
    public ResponseEntity<ApiResponse<Void>> updateNotice(@PathVariable("noticeNo") int noticeNo, @RequestBody NoticeTable noticeTable) {
        noticeTable.setNoticeNo(noticeNo);
        noticeService.updateNotice(noticeTable);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    // notice 사진 수정
    @PatchMapping("/{noticeNo}")
    public ResponseEntity<ApiResponse<Void>> updateNoticePic(@PathVariable("noticeNo") int noticeNo,
                                                             @RequestPart(value = "noticeFile", required = false) MultipartFile noticeFile) throws IOException {
        noticeService.updateNoticePic(noticeNo, noticeFile);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }

    // notice 삭제
    @DeleteMapping("/{noticeNo}")
    public ResponseEntity<ApiResponse<String>> deleteNotice(@PathVariable("noticeNo") int noticeNo){
        noticeService.deleteNotice(noticeNo);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true));
    }


}


