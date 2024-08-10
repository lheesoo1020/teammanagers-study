package kr.teammanagers.feedback.presentation;

import kr.teammanagers.auth.dto.PrincipalDetails;
import kr.teammanagers.feedback.application.FeedbackCommandService;
import kr.teammanagers.feedback.application.FeedbackQueryService;
import kr.teammanagers.feedback.dto.request.CreateFeedbackRequest;
import kr.teammanagers.feedback.dto.response.FeedbackResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/team/storage/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackCommandService feedbackCommandService;
    private final FeedbackQueryService feedbackQueryService;

    @PostMapping
    public ResponseEntity<Void> createFeedback(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam Long teamId,
            @RequestParam Long storageId,
            @ModelAttribute CreateFeedbackRequest request) {



        request.setTeamId(teamId);
        request.setStorageId(storageId);

        log.info(String.valueOf(request.getTeamId()));
        log.info(String.valueOf(request.getStorageId()));

        feedbackCommandService.createFeedback(request, principalDetails.member());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getFeedbacks(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long storageId) {
        List<FeedbackResponse> responses = feedbackQueryService.getFeedbacksByTeamData(storageId, principalDetails.member());
        return ResponseEntity.ok(responses);
    }
}
