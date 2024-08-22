package kr.teammanagers.storage.application.module;

import kr.teammanagers.storage.domain.TeamData;

import java.util.List;

public interface StorageModuleService {
    TeamData save(TeamData teamData);

    List<TeamData> findAllByTeamManageId(Long teamManageId);

    TeamData findById(Long teamDataId);

    boolean existsById(Long teamDataId);
}
