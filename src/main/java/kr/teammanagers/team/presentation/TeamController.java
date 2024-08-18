package kr.teammanagers.team.presentation;

import jakarta.validation.Valid;
import kr.teammanagers.auth.dto.PrincipalDetails;
import kr.teammanagers.common.payload.code.ApiPayload;
import kr.teammanagers.team.application.command.TeamCommandService;
import kr.teammanagers.team.application.query.TeamQueryService;
import kr.teammanagers.team.dto.request.CreateTeam;
import kr.teammanagers.team.dto.request.CreateTeamComment;
import kr.teammanagers.team.dto.request.CreateTeamPassword;
import kr.teammanagers.team.dto.request.ValidatePassword;
import kr.teammanagers.team.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeamController {

    private final TeamQueryService teamQueryService;
    private final TeamCommandService teamCommandService;

    @PostMapping("/team")
    public ApiPayload<CreateTeamResult> create(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @RequestPart(name = "createTeam") @Valid final CreateTeam createTeam,
            @RequestPart(name = "imageFile", required = false) final MultipartFile imageFile
    ) {
        CreateTeamResult result = teamCommandService.createTeam(auth.member().getId(), createTeam, imageFile);
        return ApiPayload.onSuccess(result);
    }

    @PatchMapping("/team/{teamId}/password")
    public ApiPayload<Void> createPassword(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("teamId") final Long teamId,
            @RequestBody @Valid final CreateTeamPassword createTeamPassword
    ) {
        teamCommandService.createTeamPassword(teamId, createTeamPassword);
        return ApiPayload.onSuccess();
    }

    @PostMapping("/team/{teamId}")
    public ApiPayload<Void> join(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("teamId") final Long teamId,
            @RequestBody @Valid final ValidatePassword validatePassword
    ) {
        teamCommandService.joinTeam(auth.member(), teamId, validatePassword);
        return ApiPayload.onSuccess();
    }

    @GetMapping("/team/{teamId}")
    public ApiPayload<GetTeam> getById(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("teamId") final Long teamId
    ) {
        GetTeam result = teamQueryService.getTeamById(teamId);
        return ApiPayload.onSuccess(result);
    }

    @GetMapping("/team")
    public ApiPayload<GetTeam> getByTeamCode(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @RequestParam("teamCode") final String teamCode
    ) {
        GetTeam result = teamQueryService.getTeamByTeamCode(teamCode);
        return ApiPayload.onSuccess(result);
    }

    @GetMapping("/team/{teamId}/member")
    public ApiPayload<GetTeamMember> getTeamMember(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("teamId") final Long teamId
    ) {
        GetTeamMember result = teamQueryService.getTeamMember(teamId);
        return ApiPayload.onSuccess(result);
    }

    @PatchMapping("/team/{teamId}/state")
    public ApiPayload<UpdateTeamEndResult> updateTeamState(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("teamId") final Long teamId
    ) {
        UpdateTeamEndResult result = teamCommandService.updateTeamState(auth.member().getId(), teamId);
        return ApiPayload.onSuccess(result);
    }

    @PostMapping("/team/comment")
    public ApiPayload<Void> createComment(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @RequestBody @Valid final CreateTeamComment createTeamComment
    ) {
        teamCommandService.createComment(createTeamComment);
        return ApiPayload.onSuccess();
    }
}
