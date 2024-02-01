package dmu.dasom.dasom_homepage.service.notice;

import dmu.dasom.dasom_homepage.domain.notice.NoticeDetailList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeTable;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
import dmu.dasom.dasom_homepage.exception.InsertConflictException;
import dmu.dasom.dasom_homepage.repository.NoticeRepository;
import dmu.dasom.dasom_homepage.service.s3.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) { this.noticeRepository = noticeRepository; }

    @Autowired
    private S3UploadService s3UploadService;

    // notice 조회
    public List<NoticeList> findNoticeDateDesc() {
        List<NoticeList> noticeList = noticeRepository.findNoticeDateDesc();
        if (noticeList.isEmpty()) throw new DataNotFoundException();
        return noticeList;
    }

    // 제목 기반 검색
    public List<NoticeList> findNoticeTitle(String noticeTitle) {
        return noticeRepository.findNoticeTitle(noticeTitle);
    }

    // 상세 페이지
    public NoticeDetailList detailNoticePage(int noticeNo) {
        NoticeDetailList noticeList = noticeRepository.detailNoticePage(noticeNo);
        if (noticeList == null) throw new DataNotFoundException();
        return noticeList;
    }

    // notice 등록
    public String createNotice(NoticeTable noticeTable, MultipartFile noticeFile) throws Exception {
        if(isExistsNotice(noticeTable.getNoticeNo()))
            throw new InsertConflictException();

        String fileName = s3UploadService.saveFile(noticeFile);

        noticeTable.setNoticePic(fileName);

        noticeRepository.createNotice(noticeTable);
        return "등록 완료";
    }


    // notice 수정
    public String updateNotice(NoticeTable noticeTable, MultipartFile noticeFile) throws Exception{
        if(!isExistsNotice(noticeTable.getNoticeNo()))
            throw new DataNotFoundException();

        NoticeDetailList noticeList = noticeRepository.detailNoticePage(noticeTable.getNoticeNo());

        String noticePic = noticeList.getNoticePic();

        s3UploadService.deleteFile(noticePic);

        String fileName = s3UploadService.saveFile(noticeFile);

        noticeTable.setNoticePic(fileName);

        noticeRepository.updateNotice(noticeTable);
        return "게시물 수정이 완료되었습니다";
    }


    // notice 삭제
    public String deleteNotice(int noticeNo){
        if (noticeRepository.deleteNotice(noticeNo)){
            return "삭제 되었습니다.";
        }
        throw new DataNotFoundException();
    }


    // 해당 게시물이 존재하는지 무결성 검사
    public Boolean isExistsNotice(int noticeNo){
        String strNoticeNo = Integer.toString(noticeNo);
        Optional<NoticeTable> notice = Optional.ofNullable(noticeRepository.isExistsNotice(strNoticeNo));
        return notice.isPresent();
    }

}
