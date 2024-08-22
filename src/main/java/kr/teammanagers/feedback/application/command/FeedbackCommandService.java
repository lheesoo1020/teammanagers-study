package kr.teammanagers.feedback.application.command;

import kr.teammanagers.feedback.dto.request.CreateFeedbackRequest;
import kr.teammanagers.feedback.dto.request.UpdateFeedbackRequest;
import kr.teammanagers.member.domain.Member;

public interface FeedbackCommandService {
    void createFeedback(CreateFeedbackRequest request, Member member);
    void updateFeedback(UpdateFeedbackRequest request, Member member);

    void deleteFeedback(Long feedbackId, Member member);
}
