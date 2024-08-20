package kr.teammanagers.common.payload.code.status;

import kr.teammanagers.common.payload.code.base.ErrorBaseCode;
import kr.teammanagers.common.payload.code.dto.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements ErrorBaseCode {

    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "404", "찾을 수 없습니다."),


    // 팀 관련 응답
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "TEAM404", "존재하지 않는 팀입니다."),
    TEAM_CONFLICT(HttpStatus.CONFLICT, "TEAM409", "이미 가입된 팀입니다."),
    TEAM_PASSWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "TEAM4041", "비밀번호가 일치하지 않습니다."),

    // 팀관리 관련 응답
    TEAM_MANAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "TEAMMANAGE404", "존재하지 않는 팀관리입니다."),

    // 투두 관련 응답
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO404", "존재하지 않는 투두입니다."),

    // 멤버 관련 응답
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404", "존재하지 않는 유저입니다."),
    MEMBER_SOCIAL_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4041", "존재하지 않는 소셜 타입입니다."),

    // 일정 관련 응답
    CALENDAR_NOT_FOUND(HttpStatus.NOT_FOUND, "CALENDAR404", "존재하지 않는 일정입니다."),

    // 알림 관련 응답
    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "ALARM404", "존재하지 않는 알림입니다."),

    // 스케줄 관련 응답
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE404", "존재하지 않는 스케줄입니다."),

    // 메모 관련 응답
    MEMO_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMO404", "존재하지 않는 메모입니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return null;
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
