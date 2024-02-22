package dmu.dasom.dasom_homepage.repository;

import dmu.dasom.dasom_homepage.domain.board.notice.NoticeDetailList;
import dmu.dasom.dasom_homepage.domain.board.notice.NoticeList;
import dmu.dasom.dasom_homepage.domain.board.notice.NoticeTable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NoticeRepository{
    // notice 전체 조회(목록)
    List<NoticeList> findNoticeDateDesc();
    // notice 제목 기반 검색
    List<NoticeList> findNoticeTitle(String noticeTitle);
    // notice 상세 페이지
    NoticeDetailList detailNoticePage(int noticeNo);
    // notice 등록
    void createNotice(NoticeTable noticeTable);
    // notice 수정
    void updateNotice(NoticeTable noticeTable);
    void updateNoticePic(NoticeDetailList noticeDetail);
    NoticeTable isExistsNotice(String strNoticeNo);
    // notice 삭제
    boolean deleteNotice(int noticeNo);
}
