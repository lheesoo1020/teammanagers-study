package kr.teammanagers.memo.dto.request;

import jakarta.validation.constraints.Size;
import kr.teammanagers.memo.domain.Memo;
import kr.teammanagers.tag.exception.validator.TagMaxSize;

import java.util.List;

import static kr.teammanagers.common.payload.code.constant.ValidatorErrorConstant.SIZE;
import static kr.teammanagers.tag.constant.TagErrorConstant.TAG_MAX_SIZE_LIMIT;

public record CreateMemo(
        @Size(max = 20, message = SIZE)
        String title,
        @TagMaxSize(message = TAG_MAX_SIZE_LIMIT)
        List<String> tagList,
        @Size(max = 1000, message = SIZE)
        String content
) {

    public Memo toMemo() {
        return Memo.builder()
                .title(title)
                .content(content)
                .build();
    }
}
