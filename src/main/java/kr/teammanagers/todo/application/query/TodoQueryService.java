package kr.teammanagers.todo.application.query;

import kr.teammanagers.todo.dto.response.GetTodoList;

public interface TodoQueryService {
    GetTodoList getTodoList(Long memberId, Long teamId);
}
