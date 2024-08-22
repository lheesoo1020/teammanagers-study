package kr.teammanagers.schedule.dto.response;

import kr.teammanagers.schedule.dto.ScheduleDto;
import lombok.Builder;

@Builder
public record GetPortionScheduleResult(
        ScheduleDto scheduleDto
) {
    public static GetPortionScheduleResult of(ScheduleDto scheduleDto) {
        return GetPortionScheduleResult.builder()
                .scheduleDto(scheduleDto)
                .build();
    }
}
