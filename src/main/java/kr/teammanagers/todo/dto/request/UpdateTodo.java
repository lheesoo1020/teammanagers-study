package kr.teammanagers.todo.dto.request;

import jakarta.validation.constraints.Size;
import kr.teammanagers.common.payload.code.constant.ValidatorErrorConstant;

public record UpdateTodo(
        @Size(max = 30, message = ValidatorErrorConstant.SIZE)
        String title
) {
}
