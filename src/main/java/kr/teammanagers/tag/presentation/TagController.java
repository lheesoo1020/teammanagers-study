package kr.teammanagers.tag.presentation;

import kr.teammanagers.auth.dto.PrincipalDetails;
import kr.teammanagers.common.payload.code.ApiPayload;
import kr.teammanagers.tag.application.TagCommandService;
import kr.teammanagers.tag.request.CreateRoleTag;
import kr.teammanagers.tag.request.UpdateRoleTag;
import kr.teammanagers.tag.request.UpdateTeamTag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TagController {

    private final TagCommandService tagCommandService;

    @PatchMapping("/team/{teamId}/tag/{tagId}")
    public ApiPayload<Void> updateTeamTag(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("teamId") final Long teamId,
            @PathVariable("tagId") final Long tagId,
            @RequestBody UpdateTeamTag updateTeamTag
    ) {
        tagCommandService.updateTeamTag(teamId, tagId, updateTeamTag);
        return ApiPayload.onSuccess();
    }

    @DeleteMapping("/team/{teamId}/tag/{tagId}")
    public ApiPayload<Void> deleteTeamTag(
            @AuthenticationPrincipal PrincipalDetails auth,
            @PathVariable("teamId") final Long teamId,
            @PathVariable("tagId") final Long tagId
    ) {
        tagCommandService.deleteTeamTag(teamId, tagId);
        return ApiPayload.onSuccess();
    }

    @PostMapping("/management/{teamManageId}/role")
    public ApiPayload<Void> createRoleTag(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("teamManageId") final Long teamManageId,
            @RequestBody final CreateRoleTag createRoleTag
    ) {
        tagCommandService.createRoleTag(teamManageId, createRoleTag);
        return ApiPayload.onSuccess();
    }

    @PatchMapping("/management/{teamManageId}/role/{tagId}")
    public ApiPayload<Void> updateRoleTag(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("teamManageId") final Long teamManageId,
            @PathVariable("tagId") final Long tagId,
            @RequestBody final UpdateRoleTag updateRoleTag
    ) {
        tagCommandService.updateRoleTag(teamManageId, tagId, updateRoleTag);
        return ApiPayload.onSuccess();
    }

    @DeleteMapping("/management/{teamManageId}/role/{tagId}")
    public ApiPayload<Void> deleteRoleTag(
            @AuthenticationPrincipal final PrincipalDetails auth,
            @PathVariable("teamManageId") final Long teamManageId,
            @PathVariable("tagId") final Long tagId
    ) {
        tagCommandService.deleteRoleTag(teamManageId, tagId);
        return ApiPayload.onSuccess();
    }
}
