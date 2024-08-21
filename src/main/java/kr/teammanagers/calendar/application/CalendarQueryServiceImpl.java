package kr.teammanagers.calendar.application;

import kr.teammanagers.calendar.domain.Calendar;
import kr.teammanagers.calendar.domain.TeamCalendar;
import kr.teammanagers.calendar.dto.CalendarDetailDto;
import kr.teammanagers.calendar.dto.CalendarDto;
import kr.teammanagers.calendar.dto.response.GetCalendar;
import kr.teammanagers.calendar.dto.response.GetComingCalendarList;
import kr.teammanagers.calendar.dto.response.GetSimpleCalendarList;
import kr.teammanagers.calendar.repository.CalendarRepository;
import kr.teammanagers.calendar.repository.TeamCalendarRepository;
import kr.teammanagers.common.payload.code.status.ErrorStatus;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.team.domain.TeamManage;
import kr.teammanagers.team.repository.TeamManageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarQueryServiceImpl implements CalendarQueryService {

    private final CalendarRepository calendarRepository;
    private final TeamCalendarRepository teamCalendarRepository;
    private final TeamManageRepository teamManageRepository;

    @Override
    public GetSimpleCalendarList getCalendarListOfMonth(Long memberId, Long teamId, Integer month) {
        List<CalendarDto> teamCalendarList = calendarRepository.findAllByTeamId(teamId).stream()
                .filter(calendar -> calendar.getDate().getMonthValue() == month)
                .map(calendar -> { return CalendarDto.of(calendar, null); })
                .toList();

        return GetSimpleCalendarList.from(teamCalendarList);
    }

    @Override
    public GetComingCalendarList getComingCalendarList(Long memberId, Long teamId) {

        TeamManage teamManage = teamManageRepository.findByMemberIdAndTeamId(memberId, teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_MANAGE_NOT_FOUND));

        List<CalendarDto> comingCalendarList = teamCalendarRepository.findAllByTeamManageId(teamManage.getId())
                .stream()
                .filter(teamCalendar -> teamCalendar.getCalendar().getDate().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(teamCalendar -> Duration.between(LocalDateTime.now(), teamCalendar.getCalendar().getDate()).toSeconds()))
                .limit(5)
                .map(teamCalendar -> { return CalendarDto.of(teamCalendar.getCalendar(), teamCalendar.getIsAlarmed()); })
                .toList();

        return GetComingCalendarList.from(comingCalendarList);
    }

    @Override
    public GetCalendar getCalendarDetail(Long calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CALENDAR_NOT_FOUND));

        List<String> participantList = teamCalendarRepository.findAllByCalendarId(calendar.getId())
                        .stream().map(teamCalendar -> { return teamCalendar.getTeamManage().getMember().getName(); })
                        .toList();

        return GetCalendar.from(CalendarDetailDto.of(calendar, participantList));
    }
}
