package kr.teammanagers.notice.application;

import kr.teammanagers.notice.domain.Notice;
import kr.teammanagers.notice.dto.request.CreateNotice;
import kr.teammanagers.notice.repository.NoticeRepository;
import kr.teammanagers.team.domain.Team;
import kr.teammanagers.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeCommandServiceImpl implements NoticeCommandService {

    private final NoticeRepository noticeRepository;
    private final TeamRepository teamRepository;

    @Override
    public void createNotice(final Long teamId, final CreateNotice request) {
        Team team = teamRepository.findById(teamId).orElseThrow(RuntimeException::new);
        Notice notice = request.toNotice();
        notice.setTeam(team);

        noticeRepository.save(notice);
    }
}
