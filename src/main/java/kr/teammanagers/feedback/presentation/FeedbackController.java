package kr.teammanagers.feedback.presentation;

import kr.teammanagers.auth.dto.PrincipalDetails;
import kr.teammanagers.common.payload.code.ApiPayload;
import kr.teammanagers.feedback.application.command.FeedbackCommandService;
import kr.teammanagers.feedback.application.query.FeedbackQueryService;
import kr.teammanagers.feedback.dto.FeedbackDto;
import kr.teammanagers.feedback.dto.request.CreateFeedbackRequest;
import kr.teammanagers.feedback.dto.request.UpdateFeedbackRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/storage/{storageId}/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackCommandService feedbackCommandService;
    private final FeedbackQueryService feedbackQueryService;

    // 피드백 생성
    @PostMapping
    public ResponseEntity<ApiPayload<Void>> createFeedback(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long storageId,
            @RequestBody CreateFeedbackRequest request) {

        request.setStorageId(storageId);

        Long parentId = request.getParentId() != null ? request.getParentId() : 0L;
        request.setParentId(parentId);

        feedbackCommandService.createFeedback(request, principalDetails.member());
        return ResponseEntity.ok(ApiPayload.onSuccess());
    }

    // 피드백 조회
    @GetMapping
    public ResponseEntity<ApiPayload<List<FeedbackDto>>> getFeedbacks(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long storageId) {
        List<FeedbackDto> responses = feedbackQueryService.getFeedbacksByTeamData(storageId, principalDetails.member());
        return ResponseEntity.ok(ApiPayload.onSuccess(responses));
    }

    // 피드백 수정
    @PutMapping("/{feedbackId}")
    public ResponseEntity<ApiPayload<Void>> updateFeedback(
            @AuthenticationPrincipal PrincipalDetails auth,
            @PathVariable Long feedbackId,
            @RequestBody UpdateFeedbackRequest request) {

        request.setFeedbackId(feedbackId);
        feedbackCommandService.updateFeedback(request, auth.member());
        return ResponseEntity.ok(ApiPayload.onSuccess());
    }

    // 피드백 삭제
    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<ApiPayload<Void>> deleteFeedback(
            @AuthenticationPrincipal PrincipalDetails auth,
            @PathVariable Long feedbackId) {

        feedbackCommandService.deleteFeedback(feedbackId, auth.member());
        return ResponseEntity.ok(ApiPayload.onSuccess());
    }
}