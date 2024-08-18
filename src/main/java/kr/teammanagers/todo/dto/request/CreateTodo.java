package kr.teammanagers.todo.dto.request;

import jakarta.validation.constraints.Size;
import kr.teammanagers.common.Status;
import kr.teammanagers.common.payload.code.constant.ValidatorErrorConstant;
import kr.teammanagers.todo.domain.Todo;

public record CreateTodo(
        @Size(max = 30, message = ValidatorErrorConstant.SIZE)
        String title
) {
    public Todo toTodo() {
        return Todo.builder()
                .title(title)
                .status(Status.PROCEEDING)
                .build();
    }
}
