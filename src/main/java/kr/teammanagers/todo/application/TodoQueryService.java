package kr.teammanagers.todo.application;

import kr.teammanagers.todo.dto.request.CreateTodo;
import kr.teammanagers.todo.dto.response.GetTodoList;

public interface TodoQueryService {
    GetTodoList getTodoList(Long memberId, Long teamId);
}
