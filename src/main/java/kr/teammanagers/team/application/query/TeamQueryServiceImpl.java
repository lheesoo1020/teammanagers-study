package kr.teammanagers.team.application.query;

import kr.teammanagers.global.config.AmazonConfig;
import kr.teammanagers.global.provider.AmazonS3Provider;
import kr.teammanagers.tag.application.module.TagModuleService;
import kr.teammanagers.tag.domain.Tag;
import kr.teammanagers.tag.domain.TagTeam;
import kr.teammanagers.tag.domain.TeamRole;
import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.team.domain.Team;
import kr.teammanagers.team.domain.TeamManage;
import kr.teammanagers.team.dto.SimpleTeamMemberDto;
import kr.teammanagers.team.dto.TeamMemberDto;
import kr.teammanagers.team.dto.response.GetSimpleTeamMember;
import kr.teammanagers.team.dto.response.GetTeam;
import kr.teammanagers.team.dto.response.GetTeamMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamQueryServiceImpl implements TeamQueryService {

    private final TeamModuleService teamModuleService;
    private final TagModuleService tagModuleService;

    private final AmazonS3Provider amazonS3Provider;
    private final AmazonConfig amazonConfig;

    @Override
    public GetTeam getTeamById(final Long teamId) {
        Team team = teamModuleService.findById(teamId, Team.class);
        List<Tag> tagList = tagModuleService.findAllTagTeamByTeamId(team.getId()).stream()
                .map(TagTeam::getTag).toList();
        return GetTeam.from(team, tagList,
                amazonS3Provider.generateUrl(amazonConfig.getTeamProfilePath(), team.getId()));
    }

    @Override
    public GetTeam getTeamByTeamCode(final String teamCode) {
        Team team = teamModuleService.findTeamByTeamCode(teamCode);

        List<Tag> tagList = tagModuleService.findAllTagTeamByTeamId(team.getId()).stream()
                .map(TagTeam::getTag).toList();
        return GetTeam.from(team, tagList,
                amazonS3Provider.generateUrl(amazonConfig.getTeamProfilePath(), team.getId()));
    }

    @Override
    public GetTeamMember getTeamMember(final Long teamId) {
        List<TeamMemberDto> memberList = teamModuleService.findTeamManageAllByTeamId(teamId).stream()
                .map(teamManage -> {
                    List<Tag> tagList = tagModuleService.findAllTeamRoleByTeamManageId(teamManage.getId()).stream()
                            .map(TeamRole::getTag).toList();
                    String url = amazonS3Provider.generateUrl(amazonConfig.getTeamProfilePath(), teamManage.getMember().getId());
                    return TeamMemberDto.of(teamManage, tagList, url);
                })
                .toList();
        return GetTeamMember.from(memberList);
    }

    @Override
    public GetSimpleTeamMember getSimpleTeamMember(final Long teamId) {
        List<SimpleTeamMemberDto> memberList = teamModuleService.findTeamManageAllByTeamId(teamId).stream()
                .map(SimpleTeamMemberDto::from)
                .toList();

        return GetSimpleTeamMember.from(memberList);
    }

    @Override
    public Long countTeamMembersByTeamManageId(final Long teamManageId) {
        Long teamId = teamModuleService.findById(teamManageId, TeamManage.class)
                .getTeam().getId();
        return teamModuleService.countTeamManageByTeamId(teamId);
    }
}
