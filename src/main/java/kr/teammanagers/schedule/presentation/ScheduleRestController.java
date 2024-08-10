package kr.teammanagers.schedule.presentation;

import kr.teammanagers.auth.dto.PrincipalDetails;
import kr.teammanagers.common.payload.code.ApiPayload;
import kr.teammanagers.schedule.application.ScheduleCommandService;
import kr.teammanagers.schedule.application.ScheduleQueryService;
import kr.teammanagers.schedule.dto.request.CreateSchedule;
import kr.teammanagers.schedule.dto.request.UpdateSchedule;
import kr.teammanagers.schedule.dto.response.GetTeamSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScheduleRestController {

    private final ScheduleCommandService scheduleCommandService;
    private final ScheduleQueryService scheduleQueryService;

    @PostMapping("/team/{teamId}/schedule")
    public ApiPayload<Void> createSchedule(@AuthenticationPrincipal final PrincipalDetails auth,
                                           @RequestBody final CreateSchedule request,
                                           @PathVariable(name = "teamId") final Long teamId) {

        scheduleCommandService.create(auth.member().getId(), teamId, request);

        return ApiPayload.onSuccess();
    }

    @PatchMapping("/team/{teamId}/schedule")
    public ApiPayload<Void> updateSchedule(@AuthenticationPrincipal final PrincipalDetails auth,
                                           @RequestBody final UpdateSchedule request,
                                           @PathVariable(name = "teamId") final Long teamId) {

        scheduleCommandService.update(auth.member().getId(), teamId, request);

        return ApiPayload.onSuccess();
    }

    @DeleteMapping("/team/{teamId}/schedule")
    public ApiPayload<Void> deleteSchedule(@AuthenticationPrincipal final PrincipalDetails auth,
                                           @PathVariable(name = "teamId") final Long teamId) {

        scheduleCommandService.delete(auth.member().getId(), teamId);

        return ApiPayload.onSuccess();
    }

    @GetMapping("team/{teamId}/schedule")
    public ApiPayload<GetTeamSchedule> getTeamSchedule(@AuthenticationPrincipal final PrincipalDetails auth,
                                                       @PathVariable(name = "teamId") final Long teamId) {

        GetTeamSchedule getTeamSchedule = scheduleQueryService.getTeamSchedule(teamId);

        return ApiPayload.onSuccess(getTeamSchedule);
    }
}