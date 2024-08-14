package kr.teammanagers.feedback.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFeedbackRequest {
    private Long feedbackId;
    private String content; // 수정할 내용
}
