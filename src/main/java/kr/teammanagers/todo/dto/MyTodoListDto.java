package kr.teammanagers.todo.dto;

import kr.teammanagers.tag.domain.Tag;
import kr.teammanagers.tag.dto.TagDto;
import kr.teammanagers.team.domain.Team;
import kr.teammanagers.todo.domain.Todo;
import lombok.Builder;

import java.util.List;

@Builder
public record MyTodoListDto(
        String title,
        Long teamId,
        List<TagDto> teamTagList,
        List<TodoDto> todoList
) {
    public static MyTodoListDto of(final Team team, final List<Tag> tagList, final List<Todo> todoList) {
        return MyTodoListDto.builder()
                .title(team.getTitle())
                .teamId(team.getId())
                .teamTagList(tagList.stream()
                        .map(TagDto::from)
                        .toList())
                .todoList(todoList.stream()
                        .map(TodoDto::from)
                        .toList())
                .build();
    }
}
