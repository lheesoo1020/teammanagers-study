package kr.teammanagers.calendar.application.query;

import kr.teammanagers.calendar.dto.response.GetCalendar;
import kr.teammanagers.calendar.dto.response.GetComingCalendarList;
import kr.teammanagers.calendar.dto.response.GetSimpleCalendarList;

public interface CalendarQueryService {
    GetSimpleCalendarList getCalendarListOfMonth(Long memberId, Long teamId, Integer month);
    GetComingCalendarList getComingCalendarList(Long memberId, Long teamId);
    GetCalendar getCalendarDetail(Long calendarId);
}
