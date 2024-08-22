package kr.teammanagers.alarm.repository;

import kr.teammanagers.alarm.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findAllByTeamManageId(Long teamManageId);
    void deleteAllByTeamManageId(Long teamManageId);
}
