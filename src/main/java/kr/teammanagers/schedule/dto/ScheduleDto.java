package kr.teammanagers.schedule.dto;

import kr.teammanagers.schedule.domain.Schedule;
import kr.teammanagers.schedule.domain.TimeTable;
import lombok.Builder;

@Builder
public record ScheduleDto(
        TimeTable monday,
        TimeTable tuesday,
        TimeTable wednesday,
        TimeTable thursday,
        TimeTable friday,
        TimeTable saturday,
        TimeTable sunday
) {
    public static ScheduleDto of(TimeTable monday,
                                 TimeTable tuesday,
                                 TimeTable wednesday,
                                 TimeTable thursday,
                                 TimeTable friday,
                                 TimeTable saturday,
                                 TimeTable sunday
                                   ) {
        return ScheduleDto.builder()
                .monday(monday)
                .tuesday(tuesday)
                .wednesday(wednesday)
                .thursday(thursday)
                .friday(friday)
                .saturday(saturday)
                .sunday(sunday)
                .build();
    }

    public static ScheduleDto from(Schedule schedule) {
        return ScheduleDto.builder()
                .monday(schedule.getMonday())
                .tuesday(schedule.getTuesday())
                .wednesday(schedule.getWednesday())
                .thursday(schedule.getThursday())
                .friday(schedule.getFriday())
                .saturday(schedule.getSaturday())
                .sunday(schedule.getSunday())
                .build();
    }
}
