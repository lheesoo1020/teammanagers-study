package kr.teammanagers.feedback.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFeedbackRequest {
    private Long teamId;
    private Long storageId;
    private Long parentId;
    private String content;
}
