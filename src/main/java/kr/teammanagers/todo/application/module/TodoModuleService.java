package kr.teammanagers.todo.application.module;

import kr.teammanagers.todo.domain.Todo;

import java.util.List;

public interface TodoModuleService {
    Todo getTodoById(Long id);
    List<Todo> getTodoListByTeamManageId(Long teamManageId);
    void saveTodo(Todo todo);
    void deleteTodoById(Long id);
}
