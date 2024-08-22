package kr.teammanagers.calendar.application.command;

import kr.teammanagers.calendar.dto.request.CreateCalendar;
import kr.teammanagers.calendar.dto.request.UpdateCalendar;

public interface CalendarCommandService {
    void createCalendar(CreateCalendar request, Long teamId);
    void update(UpdateCalendar request, Long calendarId);
    void updateState(Long calendarId);
    void delete(Long calendarId);
}
