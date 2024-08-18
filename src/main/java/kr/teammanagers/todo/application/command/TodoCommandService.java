package kr.teammanagers.todo.application.command;

import kr.teammanagers.todo.dto.request.CreateTodo;
import kr.teammanagers.todo.dto.request.UpdateTodo;

public interface TodoCommandService {
    void createTodo(CreateTodo request, Long teamManageId);
    void updateTodoTitle(UpdateTodo request, Long todoId);
    void updateTodoStatus(Long todoId);
    void deleteTodo(Long todoId);
}
