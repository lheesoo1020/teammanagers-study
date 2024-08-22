package kr.teammanagers.memo.application.query;

import kr.teammanagers.memo.application.module.MemoModuleService;
import kr.teammanagers.memo.domain.Memo;
import kr.teammanagers.memo.dto.MemoDto;
import kr.teammanagers.memo.dto.response.GetMemo;
import kr.teammanagers.memo.dto.response.GetMemoList;
import kr.teammanagers.tag.application.module.TagModuleService;
import kr.teammanagers.tag.domain.Tag;
import kr.teammanagers.tag.domain.TagMemo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoQueryServiceImpl implements MemoQueryService {

    private final MemoModuleService memoModuleService;
    private final TagModuleService tagModuleService;

    @Override
    public GetMemoList getMemoList(final Long teamId) {
        List<Memo> memoList = memoModuleService.findAllByTeamId(teamId);
        List<MemoDto> memoDtoList = memoList.stream()
                .map(memo -> {
                    List<Tag> tagList = tagModuleService.findAllTagMemoByMemoId(memo.getId()).stream()
                            .map(TagMemo::getTag).toList();
                    return MemoDto.of(memo, tagList);
                }).toList();

        return GetMemoList.from(memoDtoList);
    }

    @Override
    public GetMemo getMemo(final Long memoId) {
        Memo memo = memoModuleService.findById(memoId);
        List<Tag> tagList = tagModuleService.findAllTagMemoByMemoId(memoId).stream()
                .map(TagMemo::getTag).toList();

        return GetMemo.of(memo, tagList);
    }
}
