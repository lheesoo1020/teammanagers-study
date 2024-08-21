package kr.teammanagers.calendar.dto;

import kr.teammanagers.calendar.domain.Calendar;
import kr.teammanagers.team.dto.SimpleTeamManageDto;
import lombok.Builder;

import java.util.List;

@Builder
public record CalendarDetailDto(
        Long calendarId,
        String title,
        String content,
        List<SimpleTeamManageDto> participants
) {
    public static CalendarDetailDto of(Calendar calendar, List<SimpleTeamManageDto> participants) {
        return CalendarDetailDto.builder()
                .calendarId(calendar.getId())
                .title(calendar.getTitle())
                .content(calendar.getContent())
                .participants(participants)
                .build();
    }
}
