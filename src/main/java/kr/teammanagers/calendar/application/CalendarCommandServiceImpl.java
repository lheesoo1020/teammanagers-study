package kr.teammanagers.calendar.application;

import kr.teammanagers.calendar.domain.Calendar;
import kr.teammanagers.calendar.domain.TeamCalendar;
import kr.teammanagers.calendar.dto.request.CreateCalendar;
import kr.teammanagers.calendar.dto.request.UpdateCalendar;
import kr.teammanagers.calendar.repository.CalendarRepository;
import kr.teammanagers.calendar.repository.TeamCalendarRepository;
import kr.teammanagers.common.payload.code.status.ErrorStatus;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.team.domain.Team;
import kr.teammanagers.team.domain.TeamManage;
import kr.teammanagers.team.repository.TeamManageRepository;
import kr.teammanagers.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CalendarCommandServiceImpl implements CalendarCommandService {

    private final CalendarRepository calendarRepository;
    private final TeamCalendarRepository teamCalendarRepository;
    private final TeamManageRepository teamManageRepository;
    private final TeamRepository teamRepository;

    @Override
    public void createCalendar(final CreateCalendar request, final Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));
        Calendar newCalendar = request.toCalendar();
        newCalendar.setTeam(team);
        calendarRepository.save(newCalendar);

        request.participants()
                .forEach(teamManageId -> {
                    TeamManage teamManage = teamManageRepository.findById(teamManageId)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_MANAGE_NOT_FOUND));
                    TeamCalendar newTeamCalendar = TeamCalendar.builder()
                            .isAlarmed(false)
                            .build();
                    newTeamCalendar.setCalendar(newCalendar);
                    newTeamCalendar.setTeamManage(teamManage);
                    teamCalendarRepository.save(newTeamCalendar);
                });
    }

    @Override
    public void update(UpdateCalendar request, Long calendarId) {

        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CALENDAR_NOT_FOUND));

        calendar.updateTitle(request.title());
        calendar.updateContent(request.content());

        List<Long> teamManageIdList = teamCalendarRepository.findAllByCalendarId(calendarId)
                .stream().map(teamCalendar -> {
                    return teamCalendar.getTeamManage().getId();
                })
                .toList();

        List<Long> newTeamManageIdList = request.participants();

        teamManageIdList.stream().filter(teamManageId -> !newTeamManageIdList.contains(teamManageId))
                .forEach(teamManageId -> {
                    teamCalendarRepository.delete(teamCalendarRepository.findByCalendarIdAndTeamManageId(calendarId, teamManageId)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_MANAGE_NOT_FOUND)));
                });

        newTeamManageIdList.stream().filter(newTeamManageId -> !teamManageIdList.contains(newTeamManageId))
                .forEach(newTeamManageId -> {
                    TeamCalendar newTeamCalendar = TeamCalendar.builder().isAlarmed(false).build();
                    newTeamCalendar.setCalendar(calendar);
                    newTeamCalendar.setTeamManage(teamManageRepository.findById(newTeamManageId)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_MANAGE_NOT_FOUND)));
                    teamCalendarRepository.save(newTeamCalendar);
                });
    }

    @Override
    public void updateState(Long calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CALENDAR_NOT_FOUND));
        calendar.switchStatus();
    }

    @Override
    public void delete(Long calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CALENDAR_NOT_FOUND));

        teamCalendarRepository.deleteAllByCalendarId(calendarId);
        calendarRepository.delete(calendar);
    }


}
