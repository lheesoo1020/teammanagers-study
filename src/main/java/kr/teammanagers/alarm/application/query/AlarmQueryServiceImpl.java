package kr.teammanagers.alarm.application.query;

import kr.teammanagers.alarm.application.module.AlarmModuleService;
import kr.teammanagers.alarm.dto.AlarmDto;
import kr.teammanagers.alarm.dto.response.GetAlarm;
import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.team.domain.TeamManage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmQueryServiceImpl implements AlarmQueryService {

    private final AlarmModuleService alarmModuleService;
    private final TeamModuleService teamModuleService;

    @Override
    public GetAlarm get(Long memberId, Long teamId) {
        TeamManage teamManage = teamModuleService.findTeamManageByMemberIdAndTeamId(memberId, teamId);
        List<AlarmDto> alarmDtoList = alarmModuleService.findAllByTeamManageId(teamManage.getId()).stream()
                .map(AlarmDto::from)
                .toList();

        return GetAlarm.from(alarmDtoList);
    }
}
