package dmu.dasom.dasom_homepage.repository;

import dmu.dasom.dasom_homepage.domain.notice.NoticeCreate;
import dmu.dasom.dasom_homepage.domain.notice.NoticeDetailList;
import dmu.dasom.dasom_homepage.domain.notice.NoticeList;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NoticeRepository{
    List<NoticeList> findNoticeDateDesc();
    List<NoticeList> findNoticeTitle(String noticeTitle);
    NoticeDetailList detailNoticePage(int noticeNo);
    NoticeCreate noticeCreate(NoticeCreate noticeCreate);
}
