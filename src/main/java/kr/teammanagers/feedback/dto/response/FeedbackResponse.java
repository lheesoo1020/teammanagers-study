package kr.teammanagers.feedback.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.teammanagers.feedback.domain.Feedback;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class FeedbackResponse {

    private Long id;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
    private Long parentId;  // 부모 댓글의 ID (0이면 부모 댓글)
    private List<FeedbackResponse> replies; // 자식 댓글(대댓글)을 포함

    public static FeedbackResponse from(final Feedback feedback, final List<Feedback> allFeedbacks) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .content(feedback.getContent())
                .writer(feedback.getTeamData().getTeamManage().getMember().getName())
                .createdAt(feedback.getCreatedAt())
                .parentId(feedback.getParent() != null ? feedback.getParent().getId() : 0L) // parent가 null이면 0으로 설정
                .replies(allFeedbacks.stream()
                        .filter(f -> f.getParent() != null && f.getParent().getId().equals(feedback.getId()))
                        .map(f -> FeedbackResponse.from(f, allFeedbacks))
                        .collect(Collectors.toList()))
                .build();
    }

    public static List<FeedbackResponse> fromList(final List<Feedback> feedbackList) {
        return feedbackList.stream()
                .filter(feedback -> feedback.getParent() == null) // 부모 댓글만 필터링
                .map(feedback -> FeedbackResponse.from(feedback, feedbackList))
                .collect(Collectors.toList());
    }
}