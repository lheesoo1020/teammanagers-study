package kr.teammanagers.notice.presentation;

import kr.teammanagers.auth.dto.PrincipalDetails;
import kr.teammanagers.common.payload.code.ApiPayload;
import kr.teammanagers.notice.application.NoticeCommandService;
import kr.teammanagers.notice.application.NoticeQueryService;
import kr.teammanagers.notice.dto.request.CreateNotice;
import kr.teammanagers.notice.dto.response.GetNoticeList;
import kr.teammanagers.notice.dto.response.GetNoticeRecent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeQueryService noticeQueryService;
    private final NoticeCommandService noticeCommandService;

    @PostMapping("/team/{teamId}/notice")
    public ApiPayload<Void> create(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("teamId") final Long teamId,
            @RequestBody final CreateNotice createNotice
    ) {
        noticeCommandService.createNotice(teamId, createNotice);
        return ApiPayload.onSuccess();
    }

    @GetMapping("/team/{teamId}/notice")
    public ApiPayload<GetNoticeList> get(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("teamId") final Long teamId
    ) {
        GetNoticeList result = noticeQueryService.getNoticeList(teamId);
        return ApiPayload.onSuccess(result);
    }

    @GetMapping("/team/{teamId}/notice/recent")
    public ApiPayload<GetNoticeRecent> getRecent(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("teamId") final Long teamId
    ) {
        GetNoticeRecent result = noticeQueryService.getNoticeRecent(teamId);
        return ApiPayload.onSuccess(result);
    }
}
