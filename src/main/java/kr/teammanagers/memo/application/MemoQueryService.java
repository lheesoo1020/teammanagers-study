package kr.teammanagers.memo.application;

import kr.teammanagers.memo.dto.response.GetMemoList;

public interface MemoQueryService {
    GetMemoList getMemoList(Long teamId);
}
