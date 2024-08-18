package kr.teammanagers.notice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.teammanagers.notice.domain.Notice;

import static kr.teammanagers.common.payload.code.constant.ValidatorErrorConstant.NOT_NULL;
import static kr.teammanagers.common.payload.code.constant.ValidatorErrorConstant.SIZE;

public record CreateNotice(
        @NotNull(message = NOT_NULL)
        @Size(max = 100, message = SIZE)
        String content
) {
    public Notice toNotice() {
        return Notice.builder()
                .content(this.content)
                .build();
    }
}
