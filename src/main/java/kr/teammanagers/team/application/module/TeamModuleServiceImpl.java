package kr.teammanagers.team.application.module;

import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.team.domain.Team;
import kr.teammanagers.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static kr.teammanagers.common.payload.code.status.ErrorStatus.TEAM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TeamModuleServiceImpl implements TeamModuleService {

    private final TeamRepository teamRepository;

    @Override
    public Team getTeamById(final Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new GeneralException(TEAM_NOT_FOUND));
    }
}
