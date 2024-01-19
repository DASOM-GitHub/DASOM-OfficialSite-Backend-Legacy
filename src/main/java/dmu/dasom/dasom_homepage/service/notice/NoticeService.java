package dmu.dasom.dasom_homepage.service.notice;

import dmu.dasom.dasom_homepage.domain.notice.NoticeDetailList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeList;
import dmu.dasom.dasom_homepage.repository.NoticeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) { this.noticeRepository = noticeRepository; }

    // notice 조회
    public List<NoticeList> findNoticeDateDesc() {
        return noticeRepository.findNoticeDateDesc();
    }
    // 제목 기반 검색
    public List<NoticeList> findNoticeTitle(String noticeTitle) {
        return noticeRepository.findNoticeTitle(noticeTitle);
    }
    // 상세 페이지
    public NoticeDetailList detailNoticePage(int noticeNo) {
        return noticeRepository.detailNoticePage(noticeNo);
    }
}
