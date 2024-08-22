package kr.teammanagers.notice.application.module;

import kr.teammanagers.notice.domain.Notice;
import kr.teammanagers.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeModuleServiceImpl implements NoticeModuleService {

    private final NoticeRepository noticeRepository;

    @Override
    public List<Notice> getAllNoticeByTeamId(final Long teamId) {
        return noticeRepository.findAllByTeamId(teamId);
    }

    @Override
    public Notice getFirstNoticeByTeamId(final Long teamId) {
        return noticeRepository.findLastByTeamId(teamId).orElse(null);
    }

    @Override
    public Notice saveNotice(final Notice notice) {
        return noticeRepository.save(notice);
    }

}
