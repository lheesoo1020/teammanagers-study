package kr.teammanagers.schedule.application.query;

import kr.teammanagers.schedule.dto.response.GetMySchedule;
import kr.teammanagers.schedule.dto.response.GetTeamSchedule;

public interface ScheduleQueryService {
    GetTeamSchedule getTeamSchedule(Long teamId);
    GetMySchedule getMySchedule(Long memberId, Long teamId);
}
