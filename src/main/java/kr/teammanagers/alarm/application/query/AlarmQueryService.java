package kr.teammanagers.alarm.application.query;

import kr.teammanagers.alarm.dto.response.GetAlarm;

public interface AlarmQueryService {
    GetAlarm get(Long memberId, Long teamId);
}
