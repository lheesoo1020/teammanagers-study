package kr.teammanagers.feedback.application;

import kr.teammanagers.feedback.dto.FeedbackDto;
import kr.teammanagers.member.domain.Member;

import java.util.List;

public interface FeedbackQueryService {
    List<FeedbackDto> getFeedbacksByTeamData(Long storageId, Member member);

}
