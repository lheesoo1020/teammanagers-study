package kr.teammanagers.memo.presentation;

import jakarta.validation.Valid;
import kr.teammanagers.auth.dto.PrincipalDetails;
import kr.teammanagers.common.payload.code.ApiPayload;
import kr.teammanagers.memo.application.MemoCommandService;
import kr.teammanagers.memo.application.MemoQueryService;
import kr.teammanagers.memo.dto.request.CreateMemo;
import kr.teammanagers.memo.dto.request.UpdateMemo;
import kr.teammanagers.memo.dto.response.GetMemoList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemoController {

    private final MemoQueryService memoQueryService;
    private final MemoCommandService memoCommandService;

    @PostMapping("/team/{teamId}/memo")
    public ApiPayload<Void> create(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("teamId") final Long teamId,
            @RequestBody @Valid final CreateMemo createMemo
    ) {
        memoCommandService.createMemo(teamId, createMemo);
        return ApiPayload.onSuccess();
    }

    @GetMapping("/team/{teamId}/memo")
    public ApiPayload<GetMemoList> get(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("teamId") final Long teamId
    ) {
        GetMemoList result = memoQueryService.getMemoList(teamId);
        return ApiPayload.onSuccess(result);
    }

    @PatchMapping("/memo/{memoId}")
    public ApiPayload<Void> update(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("memoId") final Long memoId,
            @RequestBody @Valid final UpdateMemo updateMemo
    ) {
        memoCommandService.updateMemo(memoId, updateMemo);
        return ApiPayload.onSuccess();
    }

    @DeleteMapping("/memo/{memoId}")
    public ApiPayload<Void> delete(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("memoId") final Long memoId
    ) {
        memoCommandService.deleteMemo(memoId);
        return ApiPayload.onSuccess();
    }
}
