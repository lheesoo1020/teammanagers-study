package kr.teammanagers.calendar.repository;

import kr.teammanagers.calendar.domain.TeamCalendar;
import kr.teammanagers.team.domain.TeamManage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamCalendarRepository extends JpaRepository<TeamCalendar, Long>, TeamCalendarQueryDsl {
    List<TeamCalendar> findAllByCalendarId(Long calendarId);
    Optional<TeamCalendar> findByCalendarIdAndTeamManageId(Long calendarId, Long teamManageId);
    List<TeamCalendar> findAllByTeamManageId(Long teamManageId);
    void deleteAllByCalendarId(Long calendarId);
}
