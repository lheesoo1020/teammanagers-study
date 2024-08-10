package kr.teammanagers.feedback.application;

import kr.teammanagers.feedback.dto.response.FeedbackResponse;
import kr.teammanagers.member.domain.Member;

import java.util.List;

public interface FeedbackQueryService {
    List<FeedbackResponse> getFeedbacksByTeamData(Long storageId, Member member);
}
