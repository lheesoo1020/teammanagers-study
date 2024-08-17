package kr.teammanagers.todo.dto.response;

import kr.teammanagers.todo.dto.TodoListDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public record GetTodoList(
        Long ownerTeamManageId,
        List<TodoListDto> teamTodoList,
        Integer progress
) {
    public static GetTodoList of(Long ownerTeamManageId, List<TodoListDto> teamTodoList, Integer progress) {
        return GetTodoList.builder()
                .ownerTeamManageId(ownerTeamManageId)
                .teamTodoList(teamTodoList)
                .progress(progress)
                .build();
    }
}
