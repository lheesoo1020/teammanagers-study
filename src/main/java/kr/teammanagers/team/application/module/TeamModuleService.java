package kr.teammanagers.team.application.module;

import kr.teammanagers.team.domain.Team;

public interface TeamModuleService {
    Team getTeamById(Long id);
}
