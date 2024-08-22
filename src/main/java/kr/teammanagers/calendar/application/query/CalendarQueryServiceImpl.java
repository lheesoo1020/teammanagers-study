package kr.teammanagers.calendar.application.query;

import kr.teammanagers.calendar.application.module.CalendarModuleService;
import kr.teammanagers.calendar.domain.Calendar;
import kr.teammanagers.calendar.dto.CalendarDetailDto;
import kr.teammanagers.calendar.dto.CalendarDto;
import kr.teammanagers.calendar.dto.response.GetCalendar;
import kr.teammanagers.calendar.dto.response.GetComingCalendarList;
import kr.teammanagers.calendar.dto.response.GetSimpleCalendarList;
import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.team.domain.TeamManage;
import kr.teammanagers.team.dto.SimpleTeamManageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarQueryServiceImpl implements CalendarQueryService {

    private final CalendarModuleService calendarModuleService;
    private final TeamModuleService teamModuleService;

    @Override
    public GetSimpleCalendarList getCalendarListOfMonth(Long memberId, Long teamId, Integer month) {
        List<CalendarDto> teamCalendarList = calendarModuleService.findAllByTeamId(teamId).stream()
                .filter(calendar -> calendar.getDate().getMonthValue() == month)
                .map(calendar -> CalendarDto.of(calendar, null))
                .toList();

        return GetSimpleCalendarList.from(teamCalendarList);
    }

    @Override
    public GetComingCalendarList getComingCalendarList(Long memberId, Long teamId) {

        TeamManage teamManage = teamModuleService.findTeamManageByMemberIdAndTeamId(memberId, teamId);
        List<CalendarDto> comingCalendarList = calendarModuleService.findAllTeamCalendarByTeamManageId(teamManage.getId())
                .stream()
                .filter(teamCalendar -> teamCalendar.getCalendar().getDate().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(teamCalendar -> Duration.between(LocalDateTime.now(), teamCalendar.getCalendar().getDate()).toSeconds()))
                .limit(5)
                .map(teamCalendar -> CalendarDto.of(teamCalendar.getCalendar(), teamCalendar.getIsAlarmed()))
                .toList();

        return GetComingCalendarList.from(comingCalendarList);
    }

    @Override
    public GetCalendar getCalendarDetail(Long calendarId) {
        Calendar calendar = calendarModuleService.findById(calendarId, Calendar.class);
        List<SimpleTeamManageDto> participantList = calendarModuleService.findAllTeamCalendarByCalendarId(calendar.getId())
                .stream().map(teamCalendar -> SimpleTeamManageDto.from(teamCalendar.getTeamManage())).toList();

        return GetCalendar.from(CalendarDetailDto.of(calendar, participantList));
    }
}
