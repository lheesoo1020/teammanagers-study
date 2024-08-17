package kr.teammanagers.todo.presentation;

import kr.teammanagers.auth.dto.PrincipalDetails;
import kr.teammanagers.common.payload.code.ApiPayload;
import kr.teammanagers.todo.application.TodoCommandService;
import kr.teammanagers.todo.application.TodoQueryService;
import kr.teammanagers.todo.dto.request.CreateTodo;
import kr.teammanagers.todo.dto.request.UpdateTodo;
import kr.teammanagers.todo.dto.response.GetTodoList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TodoRestController {

    private final TodoCommandService todoCommandService;
    private final TodoQueryService todoQueryService;

    @PostMapping("/team/{teamManageId}/todo")
    public ApiPayload<Void> create(@AuthenticationPrincipal final PrincipalDetails auth,
                                   @RequestBody final CreateTodo request,
                                   @PathVariable(name = "teamManageId") final Long teamManageId) {

        todoCommandService.createTodo(request, teamManageId);
        return ApiPayload.onSuccess();
    }

    @GetMapping("/team/{teamId}/todo")
    public ApiPayload<GetTodoList> get(@AuthenticationPrincipal final PrincipalDetails auth,
                                       @PathVariable(name = "teamId") final Long teamId) {

        GetTodoList getTodoList = todoQueryService.getTodoList(auth.member().getId(), teamId);

        return ApiPayload.onSuccess(getTodoList);
    }

    @PatchMapping("/todo/{todoId}")
    public ApiPayload<Void> updateTitle(@AuthenticationPrincipal final PrincipalDetails auth,
                                        @RequestBody final UpdateTodo request,
                                        @PathVariable(name = "todoId") final Long todoId) {

        todoCommandService.updateTodoTitle(request, todoId);

        return ApiPayload.onSuccess();
    }

    @PatchMapping("/todo/{todoId}/state")
    public ApiPayload<Void> updateStatus(@AuthenticationPrincipal final PrincipalDetails auth,
                                         @PathVariable(name = "todoId") final Long todoId) {

        todoCommandService.updateTodoStatus(todoId);

        return ApiPayload.onSuccess();
    }

    @DeleteMapping("/todo/{todoId}")
    public ApiPayload<Void> delete(@AuthenticationPrincipal final PrincipalDetails auth,
                                   @PathVariable(name = "todoId") final Long todoId) {

        todoCommandService.deleteTodo(todoId);

        return ApiPayload.onSuccess(null);
    }
}
