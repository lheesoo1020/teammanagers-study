package kr.teammanagers.schedule.dto.response;

import kr.teammanagers.schedule.dto.ScheduleDto;
import lombok.Builder;

import java.util.List;

@Builder
public record GetTeamSchedule(
        List<Long> participants,
        ScheduleDto scheduleDto,
        Boolean isScheduled
) {
    public static GetTeamSchedule of(List<Long> participants, ScheduleDto scheduleDto, Boolean isScheduled) {
        return GetTeamSchedule.builder()
                .participants(participants)
                .scheduleDto(scheduleDto)
                .isScheduled(isScheduled)
                .build();
    }
}
