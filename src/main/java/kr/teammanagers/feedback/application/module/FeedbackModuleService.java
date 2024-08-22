package kr.teammanagers.feedback.application.module;

import kr.teammanagers.feedback.domain.Feedback;

import java.util.List;

public interface FeedbackModuleService {
    Feedback save(Feedback feedback);

    Feedback findById(Long feedbackId);

    void delete(Feedback feedback);

    List<Feedback> findAllByTeamDataId(Long teamDataId);
}
