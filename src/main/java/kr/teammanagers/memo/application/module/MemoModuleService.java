package kr.teammanagers.memo.application.module;

import kr.teammanagers.memo.domain.Memo;

import java.util.List;

public interface MemoModuleService {
    Memo findById(Long id);

    List<Memo> findAllByTeamId(Long teamId);

    Memo save(Memo memo);

    void deleteById(Long memoId);
}
