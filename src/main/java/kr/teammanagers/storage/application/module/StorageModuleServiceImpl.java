package kr.teammanagers.storage.application.module;

import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.storage.domain.TeamData;
import kr.teammanagers.storage.repository.TeamDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static kr.teammanagers.common.payload.code.status.ErrorStatus.TEAM_DATA_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StorageModuleServiceImpl implements StorageModuleService {

    private final TeamDataRepository teamDataRepository;

    @Override
    public TeamData save(final TeamData teamData) {
        return teamDataRepository.save(teamData);
    }

    @Override
    public List<TeamData> findAllByTeamManageId(final Long teamManageId) {
        return teamDataRepository.findAllByTeamManageId(teamManageId);
    }

    @Override
    public TeamData findById(final Long teamDataId) {
        return teamDataRepository.findById(teamDataId)
                .orElseThrow(() -> new GeneralException(TEAM_DATA_NOT_FOUND));
    }

    @Override
    public boolean existsById(final Long teamDataId) {
        return teamDataRepository.existsById(teamDataId);
    }
}
