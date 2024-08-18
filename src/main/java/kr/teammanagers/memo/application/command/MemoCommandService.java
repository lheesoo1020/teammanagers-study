package kr.teammanagers.memo.application.command;

import kr.teammanagers.memo.dto.request.CreateMemo;
import kr.teammanagers.memo.dto.request.UpdateMemo;

public interface MemoCommandService {
    void createMemo(Long teamId, CreateMemo request);

    void updateMemo(Long memoId, UpdateMemo request);

    void deleteMemo(Long memoId);
}
