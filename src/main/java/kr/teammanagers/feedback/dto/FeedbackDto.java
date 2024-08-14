package kr.teammanagers.feedback.dto;

import kr.teammanagers.feedback.domain.Feedback;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record FeedbackDto(
        Long id,
        String content,
        String writer,
        LocalDateTime createdAt,
        Long parentId,  // 부모 댓글의 ID (0이면 부모 댓글)
        List<FeedbackDto> replies // 자식 댓글(대댓글)을 포함
) {

    public static FeedbackDto from(final Feedback feedback, final List<Feedback> allFeedbacks) {
        return FeedbackDto.builder()
                .id(feedback.getId())
                .content(feedback.getContent())
                .writer(feedback.getTeamData().getTeamManage().getMember().getName())
                .createdAt(feedback.getCreatedAt())
                .parentId(feedback.getParent() != null ? feedback.getParent().getId() : 0L) // parent가 null이면 0으로 설정
                .replies(allFeedbacks.stream()
                        .filter(f -> f.getParent() != null && f.getParent().getId().equals(feedback.getId()))
                        .map(f -> FeedbackDto.from(f, allFeedbacks))
                        .collect(Collectors.toList()))
                .build();
    }

    public static List<FeedbackDto> fromList(final List<Feedback> feedbackList) {
        return feedbackList.stream()
                .filter(feedback -> feedback.getParent() == null) // 부모 댓글만 필터링
                .map(feedback -> FeedbackDto.from(feedback, feedbackList))
                .collect(Collectors.toList());
    }
}