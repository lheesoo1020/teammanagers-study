package kr.teammanagers.team.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static kr.teammanagers.common.payload.code.constant.ValidatorErrorConstant.NOT_NULL;
import static kr.teammanagers.common.payload.code.constant.ValidatorErrorConstant.SIZE;

public record ValidatePassword(
        @NotNull(message = NOT_NULL)
        @Size(max = 8, message = SIZE)
        String teamCode,
        @NotNull(message = NOT_NULL)
        String password
) {
}
