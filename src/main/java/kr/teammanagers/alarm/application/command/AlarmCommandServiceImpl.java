package kr.teammanagers.alarm.application.command;

import kr.teammanagers.alarm.application.module.AlarmModuleService;
import kr.teammanagers.alarm.domain.Alarm;
import kr.teammanagers.alarm.dto.request.CreateAlarm;
import kr.teammanagers.common.payload.code.status.ErrorStatus;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.team.domain.TeamManage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmCommandServiceImpl implements AlarmCommandService {

    private final AlarmModuleService alarmModuleService;
    private final TeamModuleService teamModuleService;

    @Override
    public void createAlarm(final CreateAlarm request, final Long memberId, final Long teamId, final Long referenceId) {
        TeamManage teamManage = teamModuleService.findTeamManageByMemberIdAndTeamId(memberId, teamId);
        Alarm alarm = Alarm.builder()
                .type(request.alarmType())
                .referenceId(referenceId)
                .date(LocalDateTime.now())
                .isRead(false)
                .build();

        alarm.setTeamManage(teamManage);
        alarmModuleService.save(alarm);
    }

    @Override
    public void delete(final Long alarmId) {
        alarmModuleService.deleteById(alarmId);
    }

    @Override
    public void read(final Long alarmId) {
        alarmModuleService.findById(alarmId).read();
    }
}
