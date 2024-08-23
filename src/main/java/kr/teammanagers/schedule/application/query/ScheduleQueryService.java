package kr.teammanagers.schedule.application.query;

import kr.teammanagers.schedule.dto.request.GetPortionSchedule;
import kr.teammanagers.schedule.dto.response.GetMySchedule;
import kr.teammanagers.schedule.dto.response.GetPortionScheduleResult;
import kr.teammanagers.schedule.dto.response.GetTeamSchedule;

public interface ScheduleQueryService {
    GetTeamSchedule getTeamSchedule(Long memberId, Long teamId);

    GetPortionScheduleResult getPortionSchedule(GetPortionSchedule request);

    GetMySchedule getMySchedule(Long memberId, Long teamId);
}
