package kr.teammanagers.schedule.application.command;

import kr.teammanagers.schedule.dto.request.CreateSchedule;
import kr.teammanagers.schedule.dto.request.UpdateSchedule;

public interface ScheduleCommandService {
    void create(Long memberId, Long teamId, CreateSchedule request);
    void update(Long memberId, Long teamId, UpdateSchedule request);
    void delete(Long teamManageId);
}
