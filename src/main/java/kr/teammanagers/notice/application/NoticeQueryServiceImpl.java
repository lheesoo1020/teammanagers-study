package kr.teammanagers.notice.application;

import kr.teammanagers.notice.dto.NoticeDto;
import kr.teammanagers.notice.dto.response.GetNoticeList;
import kr.teammanagers.notice.dto.response.GetNoticeRecent;
import kr.teammanagers.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeQueryServiceImpl implements NoticeQueryService {

    private final NoticeRepository noticeRepository;

    @Override
    public GetNoticeList getNoticeList(final Long teamId) {
        List<NoticeDto> noticeDtoList = noticeRepository.findAllByTeamId(teamId).stream()
                .map(NoticeDto::from)
                .toList();
        return GetNoticeList.from(noticeDtoList);
    }

    @Override
    public GetNoticeRecent getNoticeRecent(final Long teamId) {
        return noticeRepository.findFirstByTeamId(teamId)
                .map(NoticeDto::from)
                .map(GetNoticeRecent::from)
                .orElse(null);
    }
}
