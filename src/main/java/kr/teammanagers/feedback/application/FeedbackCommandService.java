package kr.teammanagers.feedback.application;

import kr.teammanagers.feedback.dto.request.CreateFeedbackRequest;
import kr.teammanagers.member.domain.Member;

public interface FeedbackCommandService {
    void createFeedback(CreateFeedbackRequest request, Member member);
}
