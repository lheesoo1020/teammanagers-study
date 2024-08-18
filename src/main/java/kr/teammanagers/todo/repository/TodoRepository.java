package kr.teammanagers.todo.repository;

import kr.teammanagers.team.domain.TeamManage;
import kr.teammanagers.todo.domain.Todo;
import kr.teammanagers.todo.dto.request.UpdateTodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoQueryDsl {
    List<Todo> findAllByTeamManageId(Long teamManageId);
}
