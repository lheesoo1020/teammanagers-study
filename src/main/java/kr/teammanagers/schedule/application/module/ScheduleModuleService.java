package kr.teammanagers.schedule.application.module;

import kr.teammanagers.schedule.domain.Schedule;

import java.util.Optional;

public interface ScheduleModuleService {
    Optional<Schedule> getScheduleByTeamManageId(Long teamManageId);
    void saveSchedule(Schedule schedule);
    void deleteScheduleByTeamManageId(Long teamManageId);
}
