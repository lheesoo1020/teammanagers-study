package kr.teammanagers.calendar.application.module;

import kr.teammanagers.calendar.domain.Calendar;
import kr.teammanagers.calendar.domain.TeamCalendar;
import kr.teammanagers.calendar.repository.CalendarRepository;
import kr.teammanagers.calendar.repository.TeamCalendarRepository;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.team.domain.TeamManage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static kr.teammanagers.common.payload.code.status.ErrorStatus.*;

@Service
@RequiredArgsConstructor
public class CalendarModuleServiceImpl implements CalendarModuleService {

    private final CalendarRepository calendarRepository;
    private final TeamCalendarRepository teamCalendarRepository;

    @Override
    public <T> T save(final T entity, final Class<T> clazz) {
        if (clazz.equals(Calendar.class)) {
            return clazz.cast(calendarRepository.save((Calendar) entity));
        } else if (clazz.equals(TeamCalendar.class)) {
            return clazz.cast(teamCalendarRepository.save((TeamCalendar) entity));
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + clazz);
        }
    }

    @Override
    public <T> T findById(final Long entityId, final Class<T> clazz) {
        if (clazz.equals(Calendar.class)) {
            return clazz.cast(calendarRepository.findById(entityId)
                    .orElseThrow(() -> new GeneralException(CALENDAR_NOT_FOUND)));
        } else if (clazz.equals(TeamManage.class)) {
            return clazz.cast(teamCalendarRepository.findById(entityId)
                    .orElseThrow(() -> new GeneralException(TEAM_CALENDAR_NOT_FOUND)));
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + clazz);
        }
    }

    @Override
    public <T> void delete(final T entity, final Class<T> clazz) {
        if (clazz.equals(Calendar.class)) {
            calendarRepository.delete((Calendar) entity);
        } else if (clazz.equals(TeamManage.class)) {
            teamCalendarRepository.delete((TeamCalendar) entity);
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + clazz);
        }
    }

    @Override
    public List<Calendar> findAllByTeamId(final Long teamId) {
        return calendarRepository.findAllByTeamId(teamId);
    }

    @Override
    public void deleteAllTeamCalendarByCalendarId(final Long calendarId) {
        teamCalendarRepository.deleteAllByCalendarId(calendarId);
    }

    @Override
    public TeamCalendar findTeamCalendarByCalendarIdAndTeamId(final Long calendarId, final Long teamId) {
        return teamCalendarRepository.findByCalendarIdAndTeamManageId(calendarId, teamId)
                .orElseThrow(()-> new GeneralException(TEAM_CALENDAR_NOT_FOUND));
    }

    @Override
    public List<TeamCalendar> findAllTeamCalendarByCalendarId(final Long calendarId) {
        return teamCalendarRepository.findAllByCalendarId(calendarId);
    }

    @Override
    public List<TeamCalendar> findAllTeamCalendarByTeamManageId(final Long teamManageId) {
        return teamCalendarRepository.findAllByTeamManageId(teamManageId);
    }

    @Override
    public void deleteAllTeamCalendarByTeamManageId(final Long teamManageId) {
        teamCalendarRepository.deleteAllByTeamManageId(teamManageId);
    }
}
