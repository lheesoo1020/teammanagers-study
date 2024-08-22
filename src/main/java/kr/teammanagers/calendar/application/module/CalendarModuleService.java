package kr.teammanagers.calendar.application.module;

import kr.teammanagers.calendar.domain.Calendar;
import kr.teammanagers.calendar.domain.TeamCalendar;

import java.util.List;

public interface CalendarModuleService {
    <T> T save(T entity, Class<T> clazz);

    <T> T findById(Long entityId, Class<T> clazz);

    <T> void delete(T entity, Class<T> clazz);

    List<Calendar> findAllByTeamId(Long teamId);

    void deleteAllTeamCalendarByCalendarId(Long calendarId);

    TeamCalendar findTeamCalendarByCalendarIdAndTeamId(Long calendarId, Long teamId);

    List<TeamCalendar> findAllTeamCalendarByCalendarId(Long calendarId);

    List<TeamCalendar> findAllTeamCalendarByTeamManageId(Long teamManageId);

    void deleteAllTeamCalendarByTeamManageId(Long teamManageId);
}
