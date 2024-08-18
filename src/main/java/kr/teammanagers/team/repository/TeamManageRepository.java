package kr.teammanagers.team.repository;

import kr.teammanagers.team.domain.TeamManage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamManageRepository extends JpaRepository<TeamManage, Long> {

    List<TeamManage> findAllByMemberId(Long memberId);

    List<TeamManage> findAllByTeamId(Long teamId);

    Optional<TeamManage> findByMemberIdAndTeamId(Long memberId, Long teamId);

    Boolean existsByMemberIdAndTeamId(Long memberId, Long teamId);

    Long countByTeamId(Long teamId);
}
