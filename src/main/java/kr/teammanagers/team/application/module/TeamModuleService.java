package kr.teammanagers.team.application.module;

import kr.teammanagers.team.domain.Team;
import kr.teammanagers.team.domain.TeamManage;

import java.util.List;

public interface TeamModuleService {
    TeamManage getTeamManageById(Long id);
    TeamManage getTeamManageByMemberIdAndTeamId(Long memberId, Long teamId);
    List<TeamManage> getTeamManageListByMemberId(Long memberId);
    List<TeamManage> getTeamManageListByTeamId(Long teamId);
    Team findById(Long id);
    Team findByTeamCode(String teamCode);
}
