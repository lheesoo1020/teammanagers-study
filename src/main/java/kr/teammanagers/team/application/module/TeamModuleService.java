package kr.teammanagers.team.application.module;

import kr.teammanagers.team.domain.Team;
import kr.teammanagers.team.domain.TeamManage;

import java.util.List;

public interface TeamModuleService {
    <T> T save(T entity, Class<T> clazz);

    <T> T findById(Long id, Class<T> clazz);

    <T> void delete(T entity, Class<T> clazz);

    TeamManage findTeamManageByMemberIdAndTeamId(Long memberId, Long teamId);

    List<TeamManage> findTeamManageAllByTeamId(Long teamId);

    Team findTeamByTeamCode(String teamCode);

    boolean existsByMemberIdAndTeamId(Long id, Long teamId);

    boolean existsTeamManageByMemberIdAndTeamId(Long memberId, Long teamId);

    Long countTeamManageByTeamId(Long teamId);
}
