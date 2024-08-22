package kr.teammanagers.feedback.application.query;

import kr.teammanagers.common.payload.code.status.ErrorStatus;
import kr.teammanagers.feedback.application.module.FeedbackModuleService;
import kr.teammanagers.feedback.domain.Feedback;
import kr.teammanagers.feedback.dto.FeedbackDto;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.storage.application.module.StorageModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedbackQueryServiceImpl implements FeedbackQueryService {

    private final StorageModuleService storageModuleService;
    private final FeedbackModuleService feedbackModuleService;

    @Override
    public List<FeedbackDto> getFeedbacksByTeamData(Long storageId, Member member) {
        if (!storageModuleService.existsById(storageId)) {
            throw new GeneralException(ErrorStatus.TEAM_DATA_NOT_FOUND);
        }
        List<Feedback> feedbackList = feedbackModuleService.findAllByTeamDataId(storageId);
        return FeedbackDto.fromList(feedbackList);
    }


}
