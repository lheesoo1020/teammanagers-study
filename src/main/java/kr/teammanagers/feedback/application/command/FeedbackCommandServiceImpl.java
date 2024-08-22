package kr.teammanagers.feedback.application.command;

import kr.teammanagers.feedback.application.module.FeedbackModuleService;
import kr.teammanagers.feedback.domain.Feedback;
import kr.teammanagers.feedback.dto.request.CreateFeedbackRequest;
import kr.teammanagers.feedback.dto.request.UpdateFeedbackRequest;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.storage.application.module.StorageModuleService;
import kr.teammanagers.storage.domain.TeamData;
import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.team.domain.TeamManage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackCommandServiceImpl implements FeedbackCommandService {

    private final FeedbackModuleService feedbackModuleService;
    private final TeamModuleService teamModuleService;
    private final StorageModuleService storageModuleService;

    @Override
    public void createFeedback(CreateFeedbackRequest request, Member member) {

        TeamData teamData = storageModuleService.findById(request.getStorageId());

        TeamManage teamManage = teamModuleService
                .findTeamManageByMemberIdAndTeamId(member.getId(), teamData.getTeamManage().getTeam().getId());

        Feedback parentFeedback = null;
        if (request.getParentId() != 0) {
            parentFeedback = feedbackModuleService.findById(request.getParentId());
        }

        Feedback feedback = Feedback.builder()
                .content(request.getContent())
                .build();
        feedback.setTeamManage(teamManage);
        feedback.setTeamData(teamData);
        feedback.setParent(parentFeedback);

        feedbackModuleService.save(feedback);
    }

    public void updateFeedback(UpdateFeedbackRequest request, Member member) {
        Feedback feedback = feedbackModuleService.findById(request.getFeedbackId());
        feedback.setContent(request.getContent());
        feedbackModuleService.save(feedback);
    }

    // 피드백 삭제 메서드
    public void deleteFeedback(Long feedbackId, Member member) {
        Feedback feedback = feedbackModuleService.findById(feedbackId);

        // 피드백 작성자와 요청자가 동일한지 확인
        if (!feedback.getTeamManage().getMember().getId().equals(member.getId())) {
            throw new IllegalStateException("You are not authorized to delete this feedback");
        }

        feedbackModuleService.delete(feedback);
    }

}