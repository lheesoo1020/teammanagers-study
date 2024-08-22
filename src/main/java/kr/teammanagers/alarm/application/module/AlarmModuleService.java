package kr.teammanagers.alarm.application.module;

import kr.teammanagers.alarm.domain.Alarm;

import java.util.List;

public interface AlarmModuleService {
    Alarm save(Alarm alarm);

    Alarm findById(Long alarmId);

    List<Alarm> findAllByTeamManageId(Long teamManageId);

    void deleteById(Long alarmId);

    void deleteAllByTeamManageId(Long teamManageId);
}
