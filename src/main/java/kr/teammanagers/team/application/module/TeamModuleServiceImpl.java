package kr.teammanagers.team.application.module;

import kr.teammanagers.common.payload.code.status.ErrorStatus;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.team.domain.Team;
import kr.teammanagers.team.domain.TeamManage;
import kr.teammanagers.team.repository.TeamManageRepository;
import kr.teammanagers.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static kr.teammanagers.common.payload.code.status.ErrorStatus.TEAM_MANAGE_NOT_FOUND;
import static kr.teammanagers.common.payload.code.status.ErrorStatus.TEAM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TeamModuleServiceImpl implements TeamModuleService {

    private final TeamRepository teamRepository;
    private final TeamManageRepository teamManageRepository;

    @Override
    public Team findById(final Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new GeneralException(TEAM_NOT_FOUND));
    }

    @Override
    public TeamManage getTeamManageById(Long id) {
        return teamManageRepository.findById(id)
                .orElseThrow(() -> new GeneralException(TEAM_MANAGE_NOT_FOUND));
    }

    @Override
    public TeamManage getTeamManageByMemberIdAndTeamId(Long memberId, Long teamId) {
        return teamManageRepository.findByMemberIdAndTeamId(memberId, teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_MANAGE_NOT_FOUND));
    }

    @Override
    public List<TeamManage> getTeamManageListByMemberId(Long memberId) {
        return teamManageRepository.findAllByMemberId(memberId);
    }

    @Override
    public List<TeamManage> getTeamManageListByTeamId(Long teamId) {
        return teamManageRepository.findAllByTeamId(teamId);
    }

    public Team findByTeamCode(final String teamCode) {
        return teamRepository.findByTeamCode(teamCode)
                .orElseThrow(() -> new GeneralException(TEAM_NOT_FOUND));
    }
}
