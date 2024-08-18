package kr.teammanagers.alarm.application;

import kr.teammanagers.alarm.domain.Alarm;
import kr.teammanagers.alarm.domain.AlarmType;
import kr.teammanagers.alarm.repository.AlarmRepository;
import kr.teammanagers.calendar.domain.Calendar;
import kr.teammanagers.calendar.domain.TeamCalendar;
import kr.teammanagers.calendar.repository.TeamCalendarRepository;
import kr.teammanagers.team.domain.TeamManage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class AlarmScheduler {

    private final AlarmRepository alarmRepository;
    private final TeamCalendarRepository teamCalendarRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void createAlarmCalendarRemind() {
        List<TeamCalendar> teamCalendarList = teamCalendarRepository.findAll();
        teamCalendarList.stream()
                .filter(teamCalendar -> Period.between(LocalDate.now(), teamCalendar.getCalendar().getDate().toLocalDate())
                        .getDays() == 1)
                .forEach(teamCalendar -> {
                    Calendar calendar = teamCalendar.getCalendar();
                    TeamManage teamManage = teamCalendar.getTeamManage();
                    Alarm newAlarm = Alarm.builder()
                            .type(AlarmType.CALENDAR_REMIND)
                            .date(LocalDateTime.now())
                            .isRead(false)
                            .referenceId(calendar.getId())
                            .build();
                    newAlarm.setTeamManage(teamManage);
                    alarmRepository.save(newAlarm);
                });
    }
}
