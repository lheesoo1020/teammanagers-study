package kr.teammanagers.schedule.application.query;

import kr.teammanagers.common.payload.code.status.ErrorStatus;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.schedule.application.module.ScheduleModuleService;
import kr.teammanagers.schedule.domain.Schedule;
import kr.teammanagers.schedule.domain.TimeTable;
import kr.teammanagers.schedule.dto.ScheduleDto;
import kr.teammanagers.schedule.dto.response.GetMySchedule;
import kr.teammanagers.schedule.dto.response.GetTeamSchedule;
import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.team.domain.TeamManage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleQueryServiceImpl implements ScheduleQueryService {

    private final ScheduleModuleService scheduleModuleService;
    private final TeamModuleService teamModuleService;

    @Override
    public GetTeamSchedule getTeamSchedule(Long teamId) {

        List<Schedule> teamScheduleList = teamModuleService.getTeamManageListByTeamId(teamId).stream()
                .map(teamManage -> scheduleModuleService.getScheduleByTeamManageId(teamManage.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        List<Long> scheduledTeamManageIdList = teamScheduleList.stream()
                .map(schedule -> schedule.getTeamManage().getId())
                .toList();

        return GetTeamSchedule.of(scheduledTeamManageIdList,
                ScheduleDto.of(
                        calculateIntersection(teamScheduleList, schedule -> schedule.getMonday().getValue()),
                        calculateIntersection(teamScheduleList, schedule -> schedule.getTuesday().getValue()),
                        calculateIntersection(teamScheduleList, schedule -> schedule.getWednesday().getValue()),
                        calculateIntersection(teamScheduleList, schedule -> schedule.getThursday().getValue()),
                        calculateIntersection(teamScheduleList, schedule -> schedule.getFriday().getValue()),
                        calculateIntersection(teamScheduleList, schedule -> schedule.getSaturday().getValue()),
                        calculateIntersection(teamScheduleList, schedule -> schedule.getSunday().getValue())
                )
        );
    }

    @Override
    public GetMySchedule getMySchedule(Long memberId, Long teamId) {
        TeamManage teamManage = teamModuleService.getTeamManageByMemberIdAndTeamId(memberId, teamId);

        Schedule schedule = scheduleModuleService.getScheduleByTeamManageId(teamManage.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.SCHEDULE_NOT_FOUND));

        return GetMySchedule.from(ScheduleDto.from(schedule));
    }

    public TimeTable calculateIntersection(List<Schedule> scheduleList, Function<Schedule, Character[]> function) {
        return TimeTable.from(IntStream.range(0, 48)
                .mapToObj(i -> scheduleList.stream()
                        .map(function)
                        .allMatch(value -> value[i] == '1') ? '1' : '0')
                .toArray(Character[]::new));
    }
}
