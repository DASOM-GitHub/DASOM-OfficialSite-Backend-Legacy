package dmu.dasom.dasom_homepage.service.notice;

import dmu.dasom.dasom_homepage.domain.notice.NoticeDetailList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeTable;
import dmu.dasom.dasom_homepage.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
    public String createNotice(NoticeTable noticeTable, MultipartFile noticeFile) throws Exception {

        String fileName = makeFileName(noticeFile);

        noticeTable.setNoticePic("/files/" + fileName);

        noticeRepository.createNotice(noticeTable);
        return "등록 완료";
    }


    // notice 수정
    public String updateNotice(NoticeTable noticeTable, MultipartFile noticeFile) throws Exception{
        if(!isExistsNotice(noticeTable.getNoticeNo()))
            return "해당 게시물은 존재하지 않습니다";

        String fileName = makeFileName(noticeFile);

        noticeTable.setNoticePic("/files/" + fileName);

        noticeRepository.updateNotice(noticeTable);
        return "게시물 수정이 완료되었습니다";
    }


    // notice 삭제
    public String deleteNotice(int noticeNo){
        if (noticeRepository.deleteNotice(noticeNo)){
            return "삭제 되었습니다.";
        }
        return "삭제에 실패했습니다.";
    }

    // 파일 저장 및 저장된 파일 경로 반환
    public String makeFileName(MultipartFile noticeFile) throws Exception{
        // 파일 저장 경로(디렉토리) 지정
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        // 파일명 앞에 저장 시간 추가
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        Date now = new Date();

        String time = sdf.format(now);

        String fileName = time + "_" + noticeFile.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);

        noticeFile.transferTo(saveFile);

        return fileName;
    }

    // 해당 게시물이 존재하는지 무결성 검사
    public Boolean isExistsNotice(int noticeNo){
        String strNoticeNo = Integer.toString(noticeNo);
        Optional<NoticeTable> notice = Optional.ofNullable(noticeRepository.isExistsNotice(strNoticeNo));
        return notice.isPresent();
    }

}
