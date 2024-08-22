package kr.teammanagers.alarm.application.module;

import kr.teammanagers.alarm.domain.Alarm;
import kr.teammanagers.alarm.repository.AlarmRepository;
import kr.teammanagers.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static kr.teammanagers.common.payload.code.status.ErrorStatus.*;

@Service
@RequiredArgsConstructor
public class AlarmModuleServiceImpl implements AlarmModuleService {

    private final AlarmRepository alarmRepository;

    @Override
    public Alarm save(final Alarm alarm) {
        return alarmRepository.save(alarm);
    }

    @Override
    public Alarm findById(final Long alarmId) {
        return alarmRepository.findById(alarmId)
                .orElseThrow(() -> new GeneralException(ALARM_NOT_FOUND));
    }

    @Override
    public List<Alarm> findAllByTeamManageId(final Long teamManageId) {
        return alarmRepository.findAllByTeamManageId(teamManageId);
    }

    @Override
    public void deleteById(final Long alarmId) {
        alarmRepository.deleteById(alarmId);
    }

    @Override
    public void deleteAllByTeamManageId(final Long teamManageId) {
        alarmRepository.deleteAllByTeamManageId(teamManageId);
    }
}
