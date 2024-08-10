package kr.teammanagers.feedback.application;

import kr.teammanagers.feedback.domain.Feedback;
import kr.teammanagers.feedback.dto.response.FeedbackResponse;
import kr.teammanagers.feedback.repository.FeedbackRepository;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.storage.domain.TeamData;
import kr.teammanagers.storage.repository.TeamDataRepository;
import kr.teammanagers.team.repository.TeamManageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedbackQueryServiceImpl implements FeedbackQueryService {

    private final FeedbackRepository feedbackRepository;
    private final TeamDataRepository teamDataRepository;

    @Override
    public List<FeedbackResponse> getFeedbacksByTeamData(Long storageId, Member member) {

        TeamData teamData = teamDataRepository.findById(storageId)
                .orElseThrow(() -> new IllegalArgumentException("Team Data not found"));

        List<Feedback> feedbackList = feedbackRepository.findAllByTeamDataId(storageId);
        return FeedbackResponse.fromList(feedbackList);
    }
}
