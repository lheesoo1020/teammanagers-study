package kr.teammanagers.todo.application.query;

import kr.teammanagers.common.Status;
import kr.teammanagers.tag.application.module.TagModuleService;
import kr.teammanagers.tag.dto.TagDto;
import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.todo.application.module.TodoModuleService;
import kr.teammanagers.todo.dto.TodoDto;
import kr.teammanagers.todo.dto.TodoListDto;
import kr.teammanagers.todo.dto.response.GetTodoList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoQueryServiceImpl implements TodoQueryService {

    private final TodoModuleService todoModuleService;
    private final TeamModuleService teamModuleService;
    private final TagModuleService tagModuleService;

    @Override
    public GetTodoList getTodoList(Long memberId, Long teamId) {

        List<TodoListDto> teamTodoListDtoList = teamModuleService.findTeamManageAllByTeamId(teamId).stream()
                .map(teamManage -> {
                    List<TodoDto> todoDtoList = todoModuleService.getTodoListByTeamManageId(teamManage.getId()).stream()
                            .map(TodoDto::from).toList();
                    List<TagDto> tagDtoList = tagModuleService.findAllTeamRoleByTeamManageId(teamManage.getId()).stream()
                            .map(teamRole -> TagDto.from(teamRole.getTag())).toList();
                    return TodoListDto.of(teamManage, tagDtoList, todoDtoList);
                }).toList();

        List<TodoDto> flatTeamTodoDtoList = teamTodoListDtoList.stream()
                .flatMap(todoListDto -> todoListDto.todoList().stream()).toList();

        Integer progress;

        if (flatTeamTodoDtoList.isEmpty()) {
            progress = 0;
        } else {
            progress = flatTeamTodoDtoList.stream().filter(todoDto -> todoDto.status() == Status.COMPLETED).toList().size() * 100
                    / flatTeamTodoDtoList.size();
        }

        Long ownerTeamManageId = teamModuleService.findTeamManageByMemberIdAndTeamId(memberId, teamId).getId();

        return GetTodoList.of(ownerTeamManageId, teamTodoListDtoList, progress);
    }
}
