package kr.teammanagers.notice.application;

import kr.teammanagers.notice.dto.response.GetNoticeList;
import kr.teammanagers.notice.dto.response.GetNoticeRecent;

public interface NoticeQueryService {
    GetNoticeList getNoticeList(Long teamId);

    GetNoticeRecent getNoticeRecent(Long teamId);
}
