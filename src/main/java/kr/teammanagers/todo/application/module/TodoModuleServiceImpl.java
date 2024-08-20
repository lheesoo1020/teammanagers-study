package kr.teammanagers.todo.application.module;

import kr.teammanagers.common.payload.code.status.ErrorStatus;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.todo.domain.Todo;
import kr.teammanagers.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoModuleServiceImpl implements TodoModuleService {

    private final TodoRepository todoRepository;

    @Override
    public Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TODO_NOT_FOUND));
    }

    @Override
    public List<Todo> getTodoListByTeamManageId(Long teamManageId) {
        return todoRepository.findAllByTeamManageId(teamManageId);
    }

    @Override
    public void saveTodo(Todo todo) {
        todoRepository.save(todo);
    }

    @Override
    public void deleteTodoById(Long id) {
        todoRepository.deleteById(id);
    }
}
