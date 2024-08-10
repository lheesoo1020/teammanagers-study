package kr.teammanagers.todo.application;

import kr.teammanagers.todo.dto.request.CreateTodo;
import kr.teammanagers.todo.dto.request.UpdateTodo;

public interface TodoCommandService {
    void createTodo(CreateTodo request, Long memberId, Long teamId);
    void updateTodoTitle(UpdateTodo reqeust, Long todoId);
    void updateTodoStatus(Long todoId);
    void deleteTodo(Long todoId);
}
