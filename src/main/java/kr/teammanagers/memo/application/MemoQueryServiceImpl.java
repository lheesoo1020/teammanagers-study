package kr.teammanagers.memo.application;

import kr.teammanagers.memo.domain.Memo;
import kr.teammanagers.memo.dto.MemoDto;
import kr.teammanagers.memo.dto.response.GetMemoList;
import kr.teammanagers.memo.repository.MemoRepository;
import kr.teammanagers.tag.domain.Tag;
import kr.teammanagers.tag.domain.TagMemo;
import kr.teammanagers.tag.repository.TagMemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoQueryServiceImpl implements MemoQueryService {

    private final MemoRepository memoRepository;
    private final TagMemoRepository tagMemoRepository;

    @Override
    public GetMemoList getMemoList(final Long teamId) {
        List<Memo> memoList = memoRepository.findAllByTeamId(teamId);
        List<MemoDto> memoDtoList = memoList.stream()
                .map(memo -> {
                    List<Tag> tagList = tagMemoRepository.findAllByMemoId(memo.getId()).stream()
                            .map(TagMemo::getTag).toList();
                    return MemoDto.of(memo, tagList);
                }).toList();

        return GetMemoList.from(memoDtoList);
    }
}
