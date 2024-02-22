package dmu.dasom.dasom_homepage.service.board;

import dmu.dasom.dasom_homepage.domain.board.notice.NoticeDetailList;
import dmu.dasom.dasom_homepage.domain.board.notice.NoticeList;
import dmu.dasom.dasom_homepage.domain.board.notice.NoticeTable;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
import dmu.dasom.dasom_homepage.repository.MemberRepository;
import dmu.dasom.dasom_homepage.repository.NoticeRepository;
import dmu.dasom.dasom_homepage.service.s3.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;
    private final S3UploadService s3UploadService;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository, MemberRepository memberRepository, S3UploadService s3UploadService) {
        this.noticeRepository = noticeRepository;
        this.memberRepository = memberRepository;
        this.s3UploadService = s3UploadService;
    }

    // notice 조회
    public List<NoticeList> findNoticeDateDesc() {
        List<NoticeList> noticeList = noticeRepository.findNoticeDateDesc();
        if (noticeList.isEmpty()) throw new DataNotFoundException();
        return noticeList;
    }

    // 제목 기반 검색
    public List<NoticeList> findNoticeTitle(String noticeTitle) {
        List<NoticeList> noticeList = noticeRepository.findNoticeTitle(noticeTitle);
        if(noticeList.isEmpty()) throw new DataNotFoundException();
        return noticeList;
    }

    // 상세 페이지
    public NoticeDetailList detailNoticePage(int noticeNo) {
        NoticeDetailList noticeList = noticeRepository.detailNoticePage(noticeNo);
        if (noticeList == null) throw new DataNotFoundException();
        return noticeList;
    }

    // notice 등록
    public void createNotice(NoticeTable noticeTable) {
        noticeTable.setWriterNo(memberRepository.getMemberByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getMemNo());
        noticeRepository.createNotice(noticeTable);
    }


    // notice 수정
    public void updateNotice(NoticeTable noticeTable) {
        if (!isExistsNotice(noticeTable.getNoticeNo()))
            throw new DataNotFoundException();
        noticeRepository.updateNotice(noticeTable);
    }

    // notice 사진 업데이트
    public void updateNoticePic(int noticeNo, MultipartFile noticeFile) throws IOException {
        NoticeDetailList notice = detailNoticePage(noticeNo);
        notice.setNoticeNo(noticeNo);
        s3UploadService.deleteFile(notice.getNoticePic());
        if (noticeFile != null)
            notice.setNoticePic(s3UploadService.saveFile(noticeFile));
        else
            notice.setNoticePic(null);

        noticeRepository.updateNoticePic(notice);
    }

    // notice 삭제
    public void deleteNotice(int noticeNo){
        if (!noticeRepository.deleteNotice(noticeNo)){
            throw new DataNotFoundException();
        }
    }


    // 해당 게시물이 존재하는지 무결성 검사
    public Boolean isExistsNotice(int noticeNo){
        String strNoticeNo = Integer.toString(noticeNo);
        Optional<NoticeTable> notice = Optional.ofNullable(noticeRepository.isExistsNotice(strNoticeNo));
        return notice.isPresent();
    }

}
