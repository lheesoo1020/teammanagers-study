package kr.teammanagers.member.dto.response;

import kr.teammanagers.todo.dto.MyTodoListDto;
import lombok.Builder;

import java.util.List;

@Builder
public record GetMyTodoList(
        List<MyTodoListDto> myTodoListDtos
) {
    public static GetMyTodoList from(final List<MyTodoListDto> myTodoListDtos) {
        return GetMyTodoList.builder()
                .myTodoListDtos(myTodoListDtos)
                .build();
    }
}
