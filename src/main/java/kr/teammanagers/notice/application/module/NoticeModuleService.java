package kr.teammanagers.notice.application.module;

import kr.teammanagers.notice.domain.Notice;

import java.util.List;

public interface NoticeModuleService {
    List<Notice> getAllNoticeByTeamId(Long teamId);

    Notice getFirstNoticeByTeamId(Long teamId);

    Notice saveNotice(Notice notice);
}
