package kr.teammanagers.memo.application.query;

import kr.teammanagers.memo.dto.response.GetMemo;
import kr.teammanagers.memo.dto.response.GetMemoList;

public interface MemoQueryService {
    GetMemoList getMemoList(Long teamId);

    GetMemo getMemo(Long memoId);
}
