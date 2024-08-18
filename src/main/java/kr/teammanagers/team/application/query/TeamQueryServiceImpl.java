package kr.teammanagers.team.application.query;

import kr.teammanagers.global.config.AmazonConfig;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.global.provider.AmazonS3Provider;
import kr.teammanagers.tag.domain.Tag;
import kr.teammanagers.tag.domain.TagTeam;
import kr.teammanagers.tag.repository.TagTeamRepository;
import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.team.domain.Team;
import kr.teammanagers.team.dto.SimpleTeamMemberDto;
import kr.teammanagers.team.dto.response.GetTeam;
import kr.teammanagers.team.dto.response.GetTeamMember;
import kr.teammanagers.team.repository.TeamManageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.teammanagers.common.payload.code.status.ErrorStatus.TEAM_MANAGE_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamQueryServiceImpl implements TeamQueryService {

    private final TeamModuleService teamModuleService;
    private final TagTeamRepository tagTeamRepository;
    private final TeamManageRepository teamManageRepository;

    private final AmazonS3Provider amazonS3Provider;
    private final AmazonConfig amazonConfig;

    @Override
    public GetTeam getTeamById(final Long teamId) {
        Team team = teamModuleService.findById(teamId);
        List<Tag> tagList = tagTeamRepository.findAllByTeamId(team.getId()).stream()
                .map(TagTeam::getTag).toList();
        return GetTeam.from(team, tagList,
                amazonS3Provider.generateUrl(amazonConfig.getTeamProfilePath(), team.getId()));
    }

    @Override
    public GetTeam getTeamByTeamCode(final String teamCode) {
        Team team = teamModuleService.findByTeamCode(teamCode);

        List<Tag> tagList = tagTeamRepository.findAllByTeamId(team.getId()).stream()
                .map(TagTeam::getTag).toList();
        return GetTeam.from(team, tagList,
                amazonS3Provider.generateUrl(amazonConfig.getTeamProfilePath(), team.getId()));
    }

    @Override
    public GetTeamMember getTeamMember(final Long teamId) {
        List<SimpleTeamMemberDto> memberList = teamManageRepository.findAllByTeamId(teamId).stream()
                .map(SimpleTeamMemberDto::from)
                .toList();

        return GetTeamMember.from(memberList);
    }

    @Override
    public Long countTeamMembersByTeamManageId(Long teamManageId) {
        Long teamId = teamManageRepository.findById(teamManageId)
                .orElseThrow(() -> new GeneralException(TEAM_MANAGE_NOT_FOUND))
                .getTeam().getId();
        return teamManageRepository.countByTeamId(teamId);
    }
}
