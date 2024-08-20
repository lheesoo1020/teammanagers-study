package kr.teammanagers.schedule.application.query;

import kr.teammanagers.schedule.dto.response.GetTeamSchedule;

public interface ScheduleQueryService {
    GetTeamSchedule getTeamSchedule(long teamId);
}
