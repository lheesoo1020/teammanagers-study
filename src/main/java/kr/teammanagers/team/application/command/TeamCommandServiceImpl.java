package kr.teammanagers.team.application.command;

import kr.teammanagers.alarm.application.command.AlarmCommandService;
import kr.teammanagers.alarm.application.module.AlarmModuleService;
import kr.teammanagers.alarm.domain.AlarmType;
import kr.teammanagers.calendar.application.module.CalendarModuleService;
import kr.teammanagers.common.Status;
import kr.teammanagers.global.config.AmazonConfig;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.global.provider.AmazonS3Provider;
import kr.teammanagers.member.application.module.MemberModuleService;
import kr.teammanagers.member.domain.Comment;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.schedule.application.module.ScheduleModuleService;
import kr.teammanagers.tag.application.module.TagModuleService;
import kr.teammanagers.tag.domain.Tag;
import kr.teammanagers.tag.domain.TagTeam;
import kr.teammanagers.tag.domain.TeamRole;
import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.team.domain.Team;
import kr.teammanagers.team.domain.TeamManage;
import kr.teammanagers.team.dto.TeamMemberDto;
import kr.teammanagers.team.dto.request.*;
import kr.teammanagers.team.dto.response.CreateTeamResult;
import kr.teammanagers.team.dto.response.UpdateTeamEndResult;
import kr.teammanagers.todo.application.module.TodoModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static kr.teammanagers.common.payload.code.status.ErrorStatus.TEAM_CONFLICT;
import static kr.teammanagers.common.payload.code.status.ErrorStatus.TEAM_PASSWORD_NOT_FOUND;
import static kr.teammanagers.team.constant.TeamConstant.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamCommandServiceImpl implements TeamCommandService {

    private final MemberModuleService memberModuleService;
    private final TagModuleService tagModuleService;
    private final TeamModuleService teamModuleService;
    private final ScheduleModuleService scheduleModuleService;
    private final TodoModuleService todoModuleService;
    private final AlarmModuleService alarmModuleService;
    private final CalendarModuleService calendarModuleService;
    private final AlarmCommandService alarmCommandService;

    private final AmazonConfig amazonConfig;
    private final AmazonS3Provider amazonS3Provider;

    @Override
    public CreateTeamResult createTeam(final Long authId, final CreateTeam request, final MultipartFile imageFile) {
        Member member = memberModuleService.findMemberById(authId);
        Team team = teamModuleService.save(request.toTeam(), Team.class);
        if (imageFile != null) {
            amazonS3Provider.uploadImage(amazonConfig.getTeamProfilePath(), team.getId(), imageFile);
        }
        team.updateTeamCode(encodeNumberToChars(team.getId()));

        request.teamTagList().stream()
                .map(tagModuleService::findOrCreateTag)
                .forEach(tag -> tagModuleService.save(
                        TagTeam.builder()
                                .tag(tag)
                                .team(team)
                                .build(),
                        TagTeam.class
                ));

        TeamManage admin = TeamManage.builder()
                .isDeleted(false)
                .build();
        admin.setTeam(team);
        admin.setMember(member);
        teamModuleService.save(admin, TeamManage.class);
        return CreateTeamResult.from(team);
    }

    @Override
    public void updateTeam(final Long teamId, final UpdateTeam request, final MultipartFile imageFile) {
        Team team = teamModuleService.findById(teamId, Team.class);

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
        Team team = teamModuleService.findById(teamId, Team.class);
        team.updatePassword(request.password());
    }

    @Override
    public void joinTeam(final Member auth, final Long teamId, final ValidatePassword request) {
        if (!teamModuleService.findById(teamId, Team.class).getPassword().equals(request.password())) {
            throw new GeneralException(TEAM_PASSWORD_NOT_FOUND);
        }
        if (teamModuleService.existsByMemberIdAndTeamId(auth.getId(), teamId)) {
            throw new GeneralException(TEAM_CONFLICT);
        }

        teamModuleService.save(TeamManage.builder()
                        .isDeleted(false)
                        .member(auth)
                        .team(teamModuleService.findById(teamId, Team.class))
                        .build(),
                TeamManage.class
        );
    }

    @Override
    public UpdateTeamEndResult updateTeamState(final Long authId, final Long teamId) {
        Team team = teamModuleService.findById(teamId, Team.class);
        TeamManage myTeamManage = teamModuleService.findTeamManageByMemberIdAndTeamId(authId, teamId);
        List<TeamManage> teamManageList = teamModuleService.findTeamManageAllByTeamId(teamId);
        team.updateStatus(Status.COMPLETED);
        List<TeamMemberDto> teamMemberList = teamManageList.stream()
                .filter(teamManage -> !teamManage.getMember().getId().equals(authId))
                .map(teamManage -> {
                    List<Tag> tagList = tagModuleService.findAllTeamRoleByTeamManageId(teamManage.getId()).stream()
                            .map(TeamRole::getTag).toList();
                    return TeamMemberDto.of(teamManage, tagList,
                            amazonS3Provider.generateUrl(amazonConfig.getMemberProfilePath(), teamManage.getMember().getId()));
                }).toList();

        alarmCommandService.createCustomAlarm(AlarmType.TEAM_FINISH, team.getTitle(), myTeamManage.getId(), teamId);

        return UpdateTeamEndResult.from(teamMemberList);
    }

    @Override
    public void createComment(final CreateTeamComment request) {
        request.commentList()
                .forEach(registerCommentDto -> {
                    Long memberId = teamModuleService.findById(registerCommentDto.teamManageId(), TeamManage.class)
                            .getMember().getId();
                    Comment comment = Comment.builder()
                            .content(registerCommentDto.content())
                            .isHidden(false)
                            .build();
                    comment.setMember(memberModuleService.findMemberById(memberId));
                    memberModuleService.save(comment, Comment.class);
                });
    }

    @Override
    public void exitTeam(final Long authId, final Long teamId) {
        TeamManage teamManage = teamModuleService.findTeamManageByMemberIdAndTeamId(authId, teamId);
        List<TeamRole> teamRoleList = tagModuleService.findAllTeamRoleByTeamManageId(teamManage.getId());
        List<Tag> tagList = teamRoleList.stream().map(TeamRole::getTag).toList();
        teamModuleService.delete(teamManage, TeamManage.class);
        alarmModuleService.deleteAllByTeamManageId(teamManage.getId());
        calendarModuleService.deleteAllTeamCalendarByTeamManageId(teamManage.getId());
        scheduleModuleService.deleteScheduleByTeamManageId(teamManage.getId());
        todoModuleService.deleteAllByTeamManageId(teamManage.getId());
        tagModuleService.deleteAllTeamRole(teamRoleList);
        tagList.forEach(tag -> tagModuleService.validateAndDeleteTagByTagId(tag.getId()));
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
