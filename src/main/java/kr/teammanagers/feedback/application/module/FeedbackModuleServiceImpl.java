package kr.teammanagers.feedback.application.module;

import kr.teammanagers.feedback.domain.Feedback;
import kr.teammanagers.feedback.repository.FeedbackRepository;
import kr.teammanagers.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static kr.teammanagers.common.payload.code.status.ErrorStatus.*;

@Service
@RequiredArgsConstructor
public class FeedbackModuleServiceImpl implements FeedbackModuleService {

    private final FeedbackRepository feedbackRepository;

    @Override
    public Feedback save(final Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback findById(final Long feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new GeneralException(FEEDBACK_NOT_FOUND));
    }

    @Override
    public void delete(final Feedback feedback) {
        feedbackRepository.delete(feedback);
    }

    @Override
    public List<Feedback> findAllByTeamDataId(final Long teamDataId) {
        return feedbackRepository.findAllByTeamDataId(teamDataId);
    }
}
