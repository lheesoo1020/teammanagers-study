package kr.teammanagers.notice.application.query;

import kr.teammanagers.notice.dto.response.GetNoticeList;
import kr.teammanagers.notice.dto.response.GetNoticeRecent;

public interface NoticeQueryService {
    GetNoticeList getNoticeList(Long teamId);

    GetNoticeRecent getNoticeRecent(Long teamId);
}
