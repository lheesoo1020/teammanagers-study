package kr.teammanagers.todo.application;

import kr.teammanagers.common.Status;
import kr.teammanagers.common.payload.code.status.ErrorStatus;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.tag.dto.TagDto;
import kr.teammanagers.tag.repository.TeamRoleRepository;
import kr.teammanagers.team.repository.TeamManageRepository;
import kr.teammanagers.todo.dto.TodoDto;
import kr.teammanagers.todo.dto.TodoListDto;
import kr.teammanagers.todo.dto.response.GetTodoList;
import kr.teammanagers.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoQueryServiceImpl implements TodoQueryService {

    private final TodoRepository todoRepository;
    private final TeamManageRepository teamManageRepository;
    private final TeamRoleRepository teamRoleRepository;

    @Override
    public GetTodoList getTodoList(Long memberId, Long teamId) {

        List<TodoListDto> teamTodoListDtoList = teamManageRepository.findAllByTeamId(teamId).stream()
                .map(teamManage -> {
                    List<TodoDto> todoDtoList = todoRepository.findAllByTeamManage(teamManage).stream()
                            .map(TodoDto::from).toList();
                    List<TagDto> tagDtoList = teamRoleRepository.findAllByTeamManageId(teamManage.getId()).stream()
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

        Long ownerTeamManageId = teamManageRepository.findByMemberIdAndTeamId(memberId, teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_MANAGE_NOT_FOUND))
                .getId();

        return GetTodoList.of(ownerTeamManageId, teamTodoListDtoList, progress);
    }
}
