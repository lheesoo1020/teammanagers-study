package kr.teammanagers.schedule.application.query;

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
    public GetTeamSchedule getTeamSchedule(Long memberId, Long teamId) {

        TeamManage myTeamManage = teamModuleService.findTeamManageByMemberIdAndTeamId(memberId, teamId);

        List<Schedule> teamScheduleList = teamModuleService.findTeamManageAllByTeamId(teamId).stream()
                .map(teamManage -> scheduleModuleService.getScheduleByTeamManageId(teamManage.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        List<Long> scheduledTeamManageIdList = teamScheduleList.stream()
                .map(schedule -> schedule.getTeamManage().getId())
                .toList();

        Boolean isScheduled = scheduledTeamManageIdList.contains(myTeamManage.getId());

        return GetTeamSchedule.of(scheduledTeamManageIdList,
                ScheduleDto.of(
                        calculateIntersection(teamScheduleList, schedule -> schedule.getMonday().getValue()),
                        calculateIntersection(teamScheduleList, schedule -> schedule.getTuesday().getValue()),
                        calculateIntersection(teamScheduleList, schedule -> schedule.getWednesday().getValue()),
                        calculateIntersection(teamScheduleList, schedule -> schedule.getThursday().getValue()),
                        calculateIntersection(teamScheduleList, schedule -> schedule.getFriday().getValue()),
                        calculateIntersection(teamScheduleList, schedule -> schedule.getSaturday().getValue()),
                        calculateIntersection(teamScheduleList, schedule -> schedule.getSunday().getValue())
                ),
                isScheduled
        );
    }

    @Override
    public GetMySchedule getMySchedule(Long memberId, Long teamId) {
        TeamManage teamManage = teamModuleService.findTeamManageByMemberIdAndTeamId(memberId, teamId);

        return scheduleModuleService.getScheduleByTeamManageId(teamManage.getId())
                .map(schedule -> GetMySchedule.from(ScheduleDto.from(schedule)))
                .orElseGet(() -> {
                    TimeTable emptyTimeTable = TimeTable.from(IntStream.range(0, 48)
                            .mapToObj(i -> '0')
                            .toArray(Character[]::new));
                    return GetMySchedule.from(ScheduleDto.of(
                            emptyTimeTable,
                            emptyTimeTable,
                            emptyTimeTable,
                            emptyTimeTable,
                            emptyTimeTable,
                            emptyTimeTable,
                            emptyTimeTable));
                });
    }

    public TimeTable calculateIntersection(List<Schedule> scheduleList, Function<Schedule, Character[]> function) {
        if (scheduleList.isEmpty()) {
            return TimeTable.from(IntStream.range(0, 48)
                    .mapToObj(i -> '0')
                    .toArray(Character[]::new));
        }

        return TimeTable.from(IntStream.range(0, 48)
                .mapToObj(i -> scheduleList.stream()
                        .map(function)
                        .allMatch(value -> value[i] == '1') ? '1' : '0')
                .toArray(Character[]::new));
    }
}
