package kr.teammanagers.notice.application.command;

import kr.teammanagers.notice.dto.request.CreateNotice;

public interface NoticeCommandService {
    void createNotice(Long teamId, CreateNotice request);
}
