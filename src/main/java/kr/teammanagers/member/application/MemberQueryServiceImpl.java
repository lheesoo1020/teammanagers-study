package kr.teammanagers.member.application;

import kr.teammanagers.global.config.AmazonConfig;
import kr.teammanagers.global.provider.AmazonS3Provider;
import kr.teammanagers.member.domain.Comment;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.member.dto.response.GetMemberTeam;
import kr.teammanagers.member.dto.response.GetPortfolio;
import kr.teammanagers.member.dto.response.GetProfile;
import kr.teammanagers.member.dto.response.GetSimplePortfolioList;
import kr.teammanagers.member.repository.CommentRepository;
import kr.teammanagers.member.repository.MemberRepository;
import kr.teammanagers.storage.domain.TeamData;
import kr.teammanagers.storage.repository.TeamDataRepository;
import kr.teammanagers.tag.application.module.TagQueryModuleService;
import kr.teammanagers.tag.domain.Tag;
import kr.teammanagers.tag.domain.TagTeam;
import kr.teammanagers.tag.repository.TagTeamRepository;
import kr.teammanagers.team.domain.Team;
import kr.teammanagers.team.domain.TeamManage;
import kr.teammanagers.team.dto.TeamDto;
import kr.teammanagers.team.repository.TeamManageRepository;
import kr.teammanagers.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final TeamDataRepository teamDataRepository;
    private final TeamManageRepository teamManageRepository;
    private final TeamRepository teamRepository;

    private final TagQueryModuleService tagQueryModuleService;
    private final AmazonConfig amazonConfig;
    private final AmazonS3Provider amazonS3Provider;
    private final TagTeamRepository tagTeamRepository;

    @Override
    public GetProfile getProfile(final Long authId) {
        Member member = memberRepository.findById(authId).orElseThrow(RuntimeException::new);       // TODO : 예외 처리 필요
        List<Tag> tagList = tagQueryModuleService.getAllConfidentRole(authId);
        List<Comment> commentList = commentRepository.findAllByMemberId(authId);
        String imageUrl = amazonS3Provider.generateUrl(amazonConfig.getMemberProfilePath(), member.getId());
        return GetProfile.of(member, tagList, commentList, imageUrl);
    }

    @Override
    public GetSimplePortfolioList getSimplePortfolioList(final Long authId) {
        List<Team> teamList = teamManageRepository.findAllByMemberId(authId).stream()
                .map(TeamManage::getTeam)
                .toList();
        return GetSimplePortfolioList.from(teamList);
    }

    @Override
    public GetPortfolio getPortfolio(final Long authId, final Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(RuntimeException::new);     // TODO : 예외 처리 필요
        List<TeamManage> teamManageList = teamManageRepository.findAllByTeamId(teamId);

        List<Member> memberList = teamManageRepository.findAllByTeamId(teamId).stream()
                .map(TeamManage::getMember)
                .toList();

        List<Tag> teamRoleList = tagQueryModuleService.getAllTeamRoleTag(
                teamManageList.stream()
                        .filter(teamManage -> teamManage.getId().equals(authId))
                        .findFirst()
                        .orElseThrow(RuntimeException::new)
                        .getId());

        List<TeamData> teamDataList = teamManageList.stream()
                .flatMap(teamManage -> teamDataRepository.findAllByTeamManageId(teamManage.getId()).stream())
                .toList();

        return GetPortfolio.of(team, tagQueryModuleService.getAllTeamTag(teamId), memberList, teamRoleList, teamDataList);
    }

    @Override
    public GetMemberTeam getMemberTeam(final Long authId) {
        Member member = memberRepository.findById(authId).orElseThrow(RuntimeException::new);       // TODO : 예외 처리 필요
        List<TeamDto> teamList = teamManageRepository.findAllByMemberId(authId).stream()
                .map(TeamManage::getTeam)
                .map(team -> {
                    List<Tag> tagList = tagTeamRepository.findAllByTeamId(team.getId()).stream()
                            .map(TagTeam::getTag).toList();
                    return TeamDto.from(team, tagList,
                            amazonS3Provider.generateUrl(amazonConfig.getTeamProfilePath(), team.getId()));
                })
                .toList();
        return GetMemberTeam.of(member, teamList);
    }
}
