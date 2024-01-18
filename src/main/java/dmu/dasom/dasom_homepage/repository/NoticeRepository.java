package dmu.dasom.dasom_homepage.repository;

import dmu.dasom.dasom_homepage.domain.notice.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NoticeRepository {
    List<Notice> findNoticeDateDesc();
    List<Notice> findNoticeTitle(String noticeTitle);
}
