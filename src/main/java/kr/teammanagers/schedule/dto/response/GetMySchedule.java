package kr.teammanagers.schedule.dto.response;

import kr.teammanagers.schedule.dto.ScheduleDto;
import lombok.Builder;

@Builder
public record GetMySchedule(
        ScheduleDto scheduleDto
) {
    public static GetMySchedule from(ScheduleDto scheduleDto) {
        return GetMySchedule.builder()
                .scheduleDto(scheduleDto)
                .build();
    }
}
