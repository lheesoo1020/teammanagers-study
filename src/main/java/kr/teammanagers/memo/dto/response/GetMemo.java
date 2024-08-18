package kr.teammanagers.memo.dto.response;

import kr.teammanagers.memo.domain.Memo;
import kr.teammanagers.memo.dto.MemoDto;
import kr.teammanagers.tag.domain.Tag;
import lombok.Builder;

import java.util.List;

@Builder
public record GetMemo(
        MemoDto memo
) {
    public static GetMemo of(final Memo memo, final List<Tag> tagList) {
        return GetMemo.builder()
                .memo(MemoDto.of(memo, tagList))
                .build();
    }
}
