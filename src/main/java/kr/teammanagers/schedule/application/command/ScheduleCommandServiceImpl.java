package kr.teammanagers.schedule.application.command;

import kr.teammanagers.common.payload.code.status.ErrorStatus;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.schedule.application.module.ScheduleModuleService;
import kr.teammanagers.schedule.domain.Schedule;
import kr.teammanagers.schedule.dto.request.CreateSchedule;
import kr.teammanagers.schedule.dto.request.UpdateSchedule;
import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.team.domain.TeamManage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleCommandServiceImpl implements ScheduleCommandService{

    private final ScheduleModuleService scheduleModuleService;
    private final TeamModuleService teamModuleService;

    @Override
    public void create(Long memberId, Long teamId, CreateSchedule request) {
        TeamManage teamManage = teamModuleService.getTeamManageByMemberIdAndTeamId(memberId, teamId);

        if (scheduleModuleService.getScheduleByTeamManageId(teamManage.getId()).isPresent()) {
            throw new GeneralException((ErrorStatus.SCHEDULE_ALREADY_EXIST));
        }

        Schedule newSchedule = request.toSchedule();
        newSchedule.setTeamManage(teamManage);

        scheduleModuleService.saveSchedule(newSchedule);
    }

    @Override
    public void update(Long memberId, Long teamId, UpdateSchedule request) {
        TeamManage teamManage = teamModuleService.getTeamManageByMemberIdAndTeamId(memberId, teamId);

        Schedule scheduleForUpdate = scheduleModuleService.getScheduleByTeamManageId(teamManage.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.SCHEDULE_NOT_FOUND));

        scheduleForUpdate.update(
                request.monday(),
                request.tuesday(),
                request.wednesday(),
                request.thursday(),
                request.friday(),
                request.saturday(),
                request.sunday()
        );
    }

    @Override
    public void delete(Long teamManageId) {
        TeamManage teamManage = teamModuleService.getTeamManageById(teamManageId);

        scheduleModuleService.deleteScheduleByTeamManageId(teamManage.getId());
    }
}
