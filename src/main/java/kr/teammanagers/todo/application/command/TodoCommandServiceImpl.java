package kr.teammanagers.todo.application.command;

import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.todo.application.module.TodoModuleService;
import kr.teammanagers.todo.domain.Todo;
import kr.teammanagers.todo.dto.request.CreateTodo;
import kr.teammanagers.todo.dto.request.UpdateTodo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoCommandServiceImpl implements TodoCommandService {

    private final TodoModuleService todoModuleService;
    private final TeamModuleService teamModuleService;

    @Override
    public void createTodo(CreateTodo request, Long teamManageId) {
        Todo newTodo = request.toTodo();
        newTodo.setTeamManage(teamModuleService.getTeamManageById(teamManageId));

        todoModuleService.saveTodo(newTodo);
    }

    @Override
    public void updateTodoTitle(UpdateTodo request, Long todoId) {
        Todo todoForUpdate = todoModuleService.getTodoById(todoId);

        todoForUpdate.changeTitle(request.title());
    }

    @Override
    public void updateTodoStatus(Long todoId) {
        Todo todoForUpdate = todoModuleService.getTodoById(todoId);

        todoForUpdate.switchStatus();
    }

    @Override
    public void deleteTodo(Long todoId) {
        todoModuleService.deleteTodoById(todoId);
    }


}
