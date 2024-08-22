package kr.teammanagers.alarm.presentation;

import kr.teammanagers.alarm.application.command.AlarmCommandService;
import kr.teammanagers.alarm.application.query.AlarmQueryService;
import kr.teammanagers.alarm.dto.request.CreateAlarm;
import kr.teammanagers.alarm.dto.response.GetAlarm;
import kr.teammanagers.auth.dto.PrincipalDetails;
import kr.teammanagers.common.payload.code.ApiPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AlarmRestController {

    private final AlarmCommandService alarmCommandService;
    private final AlarmQueryService alarmQueryService;

    @PostMapping("/team/{teamId}/alarm/{referenceId}")
    public ApiPayload<Void> createAlarm(@AuthenticationPrincipal final PrincipalDetails auth,
                                        @RequestBody final CreateAlarm request,
                                        @PathVariable(name = "teamId") final Long teamId,
                                        @PathVariable(name = "referenceId") final Long referenceId) {

        alarmCommandService.createAlarm(request, auth.member().getId(), teamId, referenceId);

        return ApiPayload.onSuccess();
    }

    @GetMapping("/alarm/{teamId}")
    public ApiPayload<GetAlarm> getAlarm(@AuthenticationPrincipal final PrincipalDetails auth,
                                         @PathVariable(name = "teamId") Long teamId) {

        GetAlarm getAlarm = alarmQueryService.get(auth.member().getId(), teamId);

        return ApiPayload.onSuccess(getAlarm);
    }

    @DeleteMapping("/alarm/{alarmId}")
    public ApiPayload<Void> deleteAlarm(@AuthenticationPrincipal final PrincipalDetails auth,
                                        @PathVariable(name = "alarmId") Long alarmId) {

        alarmCommandService.delete(alarmId);

        return ApiPayload.onSuccess();
    }

    @PatchMapping("alarm/{alarmId}")
    public ApiPayload<Void> readAlarm(@AuthenticationPrincipal final PrincipalDetails auth,
                                      @PathVariable(name = "alarmId") Long alarmId) {

        alarmCommandService.read(alarmId);

        return ApiPayload.onSuccess();
    }
}
