package kr.teammanagers.team.application.query;

import kr.teammanagers.team.dto.response.GetTeam;
import kr.teammanagers.team.dto.response.GetTeamMember;

public interface TeamQueryService {
    GetTeam getTeamById(Long teamId);

    GetTeam getTeamByTeamCode(String teamCode);

    GetTeamMember getTeamMember(Long teamId);

    Long countTeamMembersByTeamManageId(Long teamManageId);
}
