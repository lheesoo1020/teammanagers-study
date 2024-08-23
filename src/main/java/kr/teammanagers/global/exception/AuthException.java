package kr.teammanagers.global.exception;

import kr.teammanagers.common.payload.code.status.ErrorStatus;

public class AuthException extends GeneralException {
    public AuthException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}