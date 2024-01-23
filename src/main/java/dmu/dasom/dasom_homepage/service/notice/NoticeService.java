package dmu.dasom.dasom_homepage.service.notice;

import dmu.dasom.dasom_homepage.domain.notice.NoticeDetailList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeTable;
import dmu.dasom.dasom_homepage.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    // notice 등록
    public String createNotice(NoticeTable noticeTable) {
        noticeRepository.createNotice(noticeTable);
        return "등록 완료";
    }
    // notice 수정
    public String updateNotice(NoticeTable noticeTable){
        if(!isExistsNotice(noticeTable.getNoticeNo()))
            return "해당 게시물은 존재하지 않습니다";

        noticeRepository.updateNotice(noticeTable);
        return "게시물 수정이 완료되었습니다";
    }

    public Boolean isExistsNotice(String noticeNo){
        Optional<NoticeTable> notice = Optional.ofNullable(noticeRepository.isExistsNotice(noticeNo));
        return notice.isPresent();
    }
    // notice 삭제
    public String deleteNotice(int noticeNo){
        if (noticeRepository.deleteNotice(noticeNo)){
            return "삭제 되었습니다.";
        }
        return "삭제에 실패했습니다.";
    }

    public String createNoticeTest(NoticeTable noticeTable, MultipartFile noticeFile) throws Exception {

        // 파일 저장 경로(디렉토리) 지정
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        // 파일명 앞에 식별자 추가
        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + noticeFile.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);

        noticeFile.transferTo(saveFile);

        noticeTable.setNoticePic("/files/" + fileName);

        noticeRepository.createNotice(noticeTable);
        return "등록 완료";
    }
}
