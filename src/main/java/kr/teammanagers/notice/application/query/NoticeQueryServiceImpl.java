package kr.teammanagers.notice.application.query;

import kr.teammanagers.notice.application.module.NoticeModuleService;
import kr.teammanagers.notice.domain.Notice;
import kr.teammanagers.notice.dto.NoticeDto;
import kr.teammanagers.notice.dto.response.GetNoticeList;
import kr.teammanagers.notice.dto.response.GetNoticeRecent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeQueryServiceImpl implements NoticeQueryService {

    private final NoticeModuleService noticeModuleService;

    @Override
    public GetNoticeList getNoticeList(final Long teamId) {
        List<NoticeDto> noticeDtoList = noticeModuleService.getAllNoticeByTeamId(teamId).stream()
                .map(NoticeDto::from)
                .toList();
        return GetNoticeList.from(noticeDtoList);
    }

    @Override
    public GetNoticeRecent getNoticeRecent(final Long teamId) {
        Notice notice = noticeModuleService.getFirstNoticeByTeamId(teamId);
        if (notice != null) {
            return GetNoticeRecent.from(NoticeDto.from(notice));
        }
        return null;
    }
}
