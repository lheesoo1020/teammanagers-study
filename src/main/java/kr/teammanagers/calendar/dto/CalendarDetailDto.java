package kr.teammanagers.calendar.dto;

import kr.teammanagers.calendar.domain.Calendar;
import lombok.Builder;

import java.util.List;

@Builder
public record CalendarDetailDto(
        Long calendarId,
        String title,
        String content,
        List<String> participants
) {
    public static CalendarDetailDto of(Calendar calendar, List<String> participants) {
        return CalendarDetailDto.builder()
                .calendarId(calendar.getId())
                .title(calendar.getTitle())
                .content(calendar.getContent())
                .participants(participants)
                .build();
    }
}
