package kr.teammanagers.team.dto.request;

import jakarta.validation.constraints.Size;

import static kr.teammanagers.common.payload.code.constant.ValidatorErrorConstant.SIZE;

public record CreateTeamPassword(
        @Size(max = 6, message = SIZE)
        String password
) {
}
