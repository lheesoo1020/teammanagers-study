package kr.teammanagers.schedule.application.module;

import kr.teammanagers.schedule.domain.Schedule;
import kr.teammanagers.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleModuleServiceImpl implements ScheduleModuleService {

    private final ScheduleRepository scheduleRepository;

    @Override
    public Optional<Schedule> getScheduleByTeamManageId(Long teamManageId) {
        return scheduleRepository.findByTeamManageId(teamManageId);
    }

    @Override
    public void saveSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    @Override
    public void deleteScheduleByTeamManageId(Long teamManageId) {
        scheduleRepository.deleteByTeamManageId(teamManageId);
    }
}
