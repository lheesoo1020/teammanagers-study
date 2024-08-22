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
    public <T> T save(final T entity, final Class<T> clazz) {
        if (clazz.equals(Team.class)) {
            return clazz.cast(teamRepository.save((Team) entity));
        } else if (clazz.equals(TeamManage.class)) {
            return clazz.cast(teamManageRepository.save((TeamManage) entity));
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + clazz);
        }
    }

    @Override
    public <T> T findById(final Long id, final Class<T> clazz) {
        if (clazz.equals(Team.class)) {
            return clazz.cast(teamRepository.findById(id)
                    .orElseThrow(() -> new GeneralException(TEAM_NOT_FOUND)));
        } else if (clazz.equals(TeamManage.class)) {
            return clazz.cast(teamManageRepository.findById(id)
                    .orElseThrow(() -> new GeneralException(TEAM_MANAGE_NOT_FOUND)));
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + clazz);
        }
    }

    @Override
    public <T> void delete(final T entity, final Class<T> clazz) {
        if (clazz.equals(Team.class)) {
            teamRepository.delete((Team) entity);
        } else if (clazz.equals(TeamManage.class)) {
            teamManageRepository.delete((TeamManage) entity);
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + clazz);
        }
    }

    @Override
    public TeamManage findTeamManageByMemberIdAndTeamId(Long memberId, Long teamId) {
        return teamManageRepository.findByMemberIdAndTeamId(memberId, teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_MANAGE_NOT_FOUND));
    }

    @Override
    public List<TeamManage> findTeamManageAllByTeamId(Long teamId) {
        return teamManageRepository.findAllByTeamId(teamId);
    }

    @Override
    public Team findTeamByTeamCode(final String teamCode) {
        return teamRepository.findByTeamCode(teamCode)
                .orElseThrow(() -> new GeneralException(TEAM_NOT_FOUND));
    }

    @Override
    public boolean existsByMemberIdAndTeamId(final Long memberId, final Long teamId) {
        return teamManageRepository.existsByMemberIdAndTeamId(memberId, teamId);
    }

    @Override
    public boolean existsTeamManageByMemberIdAndTeamId(final Long memberId, final Long teamId) {
        return teamManageRepository.existsByMemberIdAndTeamId(memberId, teamId);
    }

    @Override
    public Long countTeamManageByTeamId(final Long teamId) {
        return teamManageRepository.countByTeamId(teamId);
    }
}
