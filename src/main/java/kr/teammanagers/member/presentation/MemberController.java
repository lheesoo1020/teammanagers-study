package kr.teammanagers.member.presentation;

import kr.teammanagers.auth.dto.PrincipalDetails;
import kr.teammanagers.common.payload.code.ApiPayload;
import kr.teammanagers.member.application.command.MemberCommandService;
import kr.teammanagers.member.application.query.MemberQueryService;
import kr.teammanagers.member.dto.request.UpdateProfile;
import kr.teammanagers.member.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @GetMapping("/member")
    public ApiPayload<GetProfile> getProfile(
            @AuthenticationPrincipal final PrincipalDetails auth
    ) {
        GetProfile result = memberQueryService.getProfile(auth.member().getId());
        return ApiPayload.onSuccess(result);
    }

    @GetMapping("/member/todo")
    public ApiPayload<GetMyTodoList> getMyTodoList(
            @AuthenticationPrincipal final PrincipalDetails auth
    ) {
        GetMyTodoList result = memberQueryService.getMyTodoList(auth.member().getId());
        return ApiPayload.onSuccess(result);
    }

    @PatchMapping(value = "/member", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiPayload<Void> updateProfile(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @RequestPart(name = "updateProfile") final UpdateProfile updateProfile,
            @RequestPart(name = "imageFile", required = false) final MultipartFile imageFile
    ) {
        memberCommandService.updateProfile(auth.member().getId(), updateProfile, imageFile);
        return ApiPayload.onSuccess();
    }

    @PatchMapping("/comment/{commentId}/state")
    public ApiPayload<Void> updateCommentState(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable(name = "commentId") final Long commentId
    ) {
        memberCommandService.updateCommentState(commentId);
        return ApiPayload.onSuccess();
    }

    @GetMapping("/member/portfolio")
    public ApiPayload<GetSimplePortfolioList> getSimplePortfolioList(
            @AuthenticationPrincipal final PrincipalDetails auth
    ) {
        GetSimplePortfolioList result = memberQueryService.getSimplePortfolioList(auth.member().getId());
        return ApiPayload.onSuccess(result);
    }

    @GetMapping("/member/portfolio/{teamId}")
    public ApiPayload<GetPortfolio> getPortfolio(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable(name = "teamId") final Long teamId
    ) {
        GetPortfolio result = memberQueryService.getPortfolio(auth.member().getId(), teamId);
        return ApiPayload.onSuccess(result);
    }

    @GetMapping("/member/team")
    public ApiPayload<GetMemberTeam> getTeam(
            @AuthenticationPrincipal final PrincipalDetails auth
    ) {
        GetMemberTeam result = memberQueryService.getMemberTeam(auth.member().getId());
        return ApiPayload.onSuccess(result);
    }
}
