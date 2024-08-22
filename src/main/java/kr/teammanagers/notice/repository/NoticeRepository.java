package kr.teammanagers.notice.repository;

import kr.teammanagers.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAllByTeamId(Long teamId);

    Optional<Notice> findFirstByTeamIdOrderByCreatedAtDesc(Long teamId);
}
