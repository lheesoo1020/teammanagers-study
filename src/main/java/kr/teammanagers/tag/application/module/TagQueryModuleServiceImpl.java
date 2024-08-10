package kr.teammanagers.tag.application.module;

import kr.teammanagers.tag.domain.ConfidentRole;
import kr.teammanagers.tag.domain.Tag;
import kr.teammanagers.tag.domain.TagTeam;
import kr.teammanagers.tag.domain.TeamRole;
import kr.teammanagers.tag.repository.ConfidentRoleRepository;
import kr.teammanagers.tag.repository.TagTeamRepository;
import kr.teammanagers.tag.repository.TeamRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagQueryModuleServiceImpl implements TagQueryModuleService {

    private final ConfidentRoleRepository confidentRoleRepository;
    private final TagTeamRepository tagTeamRepository;
    private final TeamRoleRepository teamRoleRepository;

    @Override
    public List<Tag> getAllConfidentRole(final Long authId) {
        return confidentRoleRepository.findAllByMemberId(authId).stream()
                .map(ConfidentRole::getTag)
                .toList();
    }

    @Override
    public List<Tag> getAllTeamTag(final Long teamId) {
        return tagTeamRepository.findAllByTeamId(teamId).stream()
                .map(TagTeam::getTag)
                .toList();
    }

    @Override
    public List<Tag> getAllTeamRoleTag(final Long teamManageId) {
        return teamRoleRepository.findAllByTeamManageId(teamManageId).stream()
                .map(TeamRole::getTag)
                .toList();
    }
}
