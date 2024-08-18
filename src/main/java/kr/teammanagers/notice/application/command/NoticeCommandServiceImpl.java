package kr.teammanagers.notice.application.command;

import kr.teammanagers.notice.application.module.NoticeModuleService;
import kr.teammanagers.notice.domain.Notice;
import kr.teammanagers.notice.dto.request.CreateNotice;
import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.team.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeCommandServiceImpl implements NoticeCommandService {

    private final NoticeModuleService noticeModuleService;
    private final TeamModuleService teamModuleService;

    @Override
    public void createNotice(final Long teamId, final CreateNotice request) {
        Team team = teamModuleService.findById(teamId);
        Notice notice = request.toNotice();
        notice.setTeam(team);
        noticeModuleService.saveNotice(notice);
    }
}
