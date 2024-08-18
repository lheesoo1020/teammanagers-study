package kr.teammanagers.team.application.command;

import kr.teammanagers.common.Status;
import kr.teammanagers.global.config.AmazonConfig;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.global.provider.AmazonS3Provider;
import kr.teammanagers.member.domain.Comment;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.member.repository.CommentRepository;
import kr.teammanagers.member.repository.MemberRepository;
import kr.teammanagers.tag.application.module.TagCommandModuleService;
import kr.teammanagers.tag.domain.Tag;
import kr.teammanagers.tag.domain.TagTeam;
import kr.teammanagers.tag.domain.TeamRole;
import kr.teammanagers.tag.repository.TagTeamRepository;
import kr.teammanagers.tag.repository.TeamRoleRepository;
import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.team.domain.Team;
import kr.teammanagers.team.domain.TeamManage;
import kr.teammanagers.team.dto.TeamMemberDto;
import kr.teammanagers.team.dto.request.*;
import kr.teammanagers.team.dto.response.CreateTeamResult;
import kr.teammanagers.team.dto.response.UpdateTeamEndResult;
import kr.teammanagers.team.repository.TeamManageRepository;
import kr.teammanagers.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static kr.teammanagers.common.payload.code.status.ErrorStatus.*;
import static kr.teammanagers.team.constant.TeamConstant.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamCommandServiceImpl implements TeamCommandService {

    private final TeamModuleService teamModuleService;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TagTeamRepository tagTeamRepository;
    private final TeamManageRepository teamManageRepository;
    private final TeamRoleRepository teamRoleRepository;
    private final CommentRepository commentRepository;

    private final TagCommandModuleService tagCommandModuleService;
    private final AmazonS3Provider amazonS3Provider;
    private final AmazonConfig amazonConfig;

    @Override
    public CreateTeamResult createTeam(final Long authId, final CreateTeam request, final MultipartFile imageFile) {
        Member member = memberRepository.getReferenceById(authId);
        Team team = teamRepository.save(request.toTeam());
        if (imageFile != null) {
            amazonS3Provider.uploadImage(amazonConfig.getTeamProfilePath(), team.getId(), imageFile);
        }
        team.updateTeamCode(encodeNumberToChars(team.getId()));

        request.teamTagList().stream()
                .map(tagCommandModuleService::findOrCreateTag)
                .forEach(tag -> tagTeamRepository.save(
                        TagTeam.builder()
                                .tag(tag)
                                .team(team)
                                .build()
                ));

        TeamManage admin = TeamManage.builder()
                .isDeleted(false)
                .build();
        admin.setTeam(team);
        admin.setMember(member);
        teamManageRepository.save(admin);
        return CreateTeamResult.from(team);
    }

    @Override
    public void updateTeam(final Long teamId, final UpdateTeam request, final MultipartFile imageFile) {
        Team team = teamModuleService.findById(teamId);

        updateTeamTitle(request.title(), team);
        updateProfileImageIfPresent(imageFile, team);
    }

    private void updateProfileImageIfPresent(final MultipartFile imageFile, final Team team) {
        if (imageFile != null && !imageFile.isEmpty()) {
            updateProfileImage(imageFile, team);
        }
    }

    private void updateProfileImage(final MultipartFile imageFile, final Team team) {
        String teamProfilePath = amazonConfig.getTeamProfilePath();
        if (amazonS3Provider.isFileExist(teamProfilePath, team.getId())) {
            amazonS3Provider.deleteFile(teamProfilePath, team.getId());
        }
        amazonS3Provider.uploadImage(teamProfilePath, team.getId(), imageFile);
    }

    private void updateTeamTitle(final String title, final Team team) {
        if (title != null) {
            team.updateTitle(title);
        }
    }


    @Override
    public void createTeamPassword(final Long teamId, final CreateTeamPassword request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GeneralException(TEAM_NOT_FOUND));
        team.updatePassword(request.password());
    }

    @Override
    public void joinTeam(final Member auth, final Long teamId, final ValidatePassword request) {
        if (teamModuleService.findById(teamId).getPassword().equals(request.password())) {
            throw new GeneralException(TEAM_PASSWORD_NOT_FOUND);
        }
        if (teamManageRepository.existsByMemberIdAndTeamId(auth.getId(), teamId)) {
            throw new GeneralException(TEAM_CONFLICT);
        }

        teamManageRepository.save(TeamManage.builder()
                .isDeleted(false)
                .member(auth)
                .team(teamModuleService.findById(teamId))
                .build()
        );
    }

    @Override
    public UpdateTeamEndResult updateTeamState(final Long authId, final Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GeneralException(TEAM_NOT_FOUND));
        team.updateStatus(Status.COMPLETED);
        List<TeamMemberDto> teamMemberList = teamManageRepository.findAllByTeamId(teamId).stream()
                .filter(teamManage -> !teamManage.getMember().getId().equals(authId))
                .map(teamManage -> {
                    List<Tag> tagList = teamRoleRepository.findAllByTeamManageId(teamManage.getId()).stream()
                            .map(TeamRole::getTag).toList();
                    return TeamMemberDto.of(teamManage, tagList,
                            amazonS3Provider.generateUrl(amazonConfig.getMemberProfilePath(), teamManage.getMember().getId()));
                }).toList();

        return UpdateTeamEndResult.from(teamMemberList);
    }

    @Override
    public void createComment(final CreateTeamComment request) {
        request.commentList()
                .forEach(registerCommentDto -> {
                    Long memberId = teamManageRepository.findById(registerCommentDto.teamManageId())
                            .orElseThrow(() -> new GeneralException(TEAM_MANAGE_NOT_FOUND))
                            .getMember().getId();
                    Comment comment = Comment.builder()
                            .content(registerCommentDto.content())
                            .isHidden(false)
                            .build();
                    comment.setMember(memberRepository.getReferenceById(memberId));
                    commentRepository.save(comment);
                });
    }

    private String encodeNumberToChars(final Long teamId) {
        StringBuilder sb = new StringBuilder();
        Long number = teamId;
        while (number > 0) {
            sb.append(BASE62.charAt((int) (number % BASE)));
            number /= BASE;
        }

        while (sb.length() < PADDING) {
            sb.append(BASE62.charAt(RANDOM.nextInt(BASE)));
        }

        return sb.reverse().toString();
    }
}
