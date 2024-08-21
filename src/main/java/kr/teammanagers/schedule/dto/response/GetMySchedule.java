package kr.teammanagers.schedule.dto.response;

import kr.teammanagers.schedule.dto.ScheduleDto;
import lombok.Builder;

@Builder
public record GetMySchedule(
        ScheduleDto schedule
) {
    public static GetMySchedule from(ScheduleDto scheduleDto) {
        return GetMySchedule.builder()
                .schedule(scheduleDto)
                .build();
    }
}
