package kr.teammanagers.common.payload.code.status;

import kr.teammanagers.common.payload.code.base.ErrorBaseCode;
import kr.teammanagers.common.payload.code.dto.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements ErrorBaseCode {

    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(BAD_REQUEST, "400", "잘못된 요청입니다."),
    _UNAUTHORIZED(UNAUTHORIZED, "401", "인증이 필요합니다."),
    _FORBIDDEN(FORBIDDEN, "403", "금지된 요청입니다."),
    _NOT_FOUND(NOT_FOUND, "404", "찾을 수 없습니다."),

    // 인증 관련 응답
    AUTH_FORBIDDEN(FORBIDDEN, "AUTH403", "OAuth2 인증에 실패하였습니다."),
    AUTH_ILLEGAL_REGISTRATION_ID(NOT_ACCEPTABLE, "AUTH406", "Registration ID가 올바르지 않습니다."),
    AUTH_TOKEN_EXPIRED(UNAUTHORIZED, "AUTH4010", "토큰이 만료되었습니다."),
    AUTH_INVALID_TOKEN(UNAUTHORIZED, "AUTH4011", "올바르지 않은 토큰입니다."),
    AUTH_INVALID_JWT_SIGNATURE(UNAUTHORIZED, "AUTH4012", "잘못된 JWT 시그니처입니다."),

    // 팀 관련 응답
    TEAM_NOT_FOUND(NOT_FOUND, "TEAM404", "존재하지 않는 팀입니다."),
    TEAM_CONFLICT(CONFLICT, "TEAM409", "이미 가입된 팀입니다."),
    TEAM_PASSWORD_NOT_FOUND(NOT_FOUND, "TEAM4041", "비밀번호가 일치하지 않습니다."),

    // 팀관리 관련 응답
    TEAM_MANAGE_NOT_FOUND(NOT_FOUND, "TEAMMANAGE404", "존재하지 않는 팀관리입니다."),

    // 투두 관련 응답
    TODO_NOT_FOUND(NOT_FOUND, "TODO404", "존재하지 않는 투두입니다."),

    // 멤버 관련 응답
    MEMBER_NOT_FOUND(NOT_FOUND, "MEMBER404", "존재하지 않는 유저입니다."),
    MEMBER_SOCIAL_TYPE_NOT_FOUND(NOT_FOUND, "MEMBER4041", "존재하지 않는 소셜 타입입니다."),
    MEMBER_COMMENT_NOT_FOUND(NOT_FOUND, "MEMBER4042", "존재하지 않는 코멘트입니다."),

    // 일정 관련 응답
    CALENDAR_NOT_FOUND(NOT_FOUND, "CALENDAR404", "존재하지 않는 일정입니다."),
    TEAM_CALENDAR_NOT_FOUND(NOT_FOUND, "CALENDAR4041", "존재하지 않는 팀일정입니다."),

    // 알림 관련 응답
    ALARM_NOT_FOUND(NOT_FOUND, "ALARM404", "존재하지 않는 알림입니다."),

    // 스케줄 관련 응답
    SCHEDULE_NOT_FOUND(NOT_FOUND, "SCHEDULE404", "존재하지 않는 스케줄입니다."),
    SCHEDULE_ALREADY_EXIST(CONFLICT, "SCHEDULE409", "이미 스케줄이 존재합니다."),

    // 메모 관련 응답
    MEMO_NOT_FOUND(NOT_FOUND, "MEMO404", "존재하지 않는 메모입니다."),

    // 태그 관련 응답
    TAG_NOT_FOUND(NOT_FOUND, "TAG404", "존재하지 않는 태그입니다."),
    TAG_TEAM_NOT_FOUND(NOT_FOUND, "TAG4041", "존재하지 않는 팀 태그입니다."),
    TAG_ROLE_NOT_FOUND(NOT_FOUND, "TAG4042", "존재하지 않는 역할 태그입니다."),

    // 스토리지 관련 응답
    TEAM_DATA_NOT_FOUND(NOT_FOUND, "STORAGE404", "존재하지 않는 팀데이터 입니다."),

    // 피드백 관련 응답
    FEEDBACK_NOT_FOUND(NOT_FOUND, "FEEDBACK404", "존재하지 않는 피드백입니다."),
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
