package kr.teammanagers.team.dto.request;

import jakarta.validation.constraints.NotNull;
import kr.teammanagers.common.Status;
import kr.teammanagers.tag.exception.validator.TagMaxSize;
import kr.teammanagers.team.constant.TeamConstant;
import kr.teammanagers.team.domain.Team;

import java.util.List;

import static kr.teammanagers.common.payload.code.constant.ValidatorErrorConstant.NOT_NULL;
import static kr.teammanagers.tag.constant.TagErrorConstant.TAG_MAX_SIZE_LIMIT;

public record CreateTeam(
        @NotNull(message = NOT_NULL)
        String title,

        @TagMaxSize(message = TAG_MAX_SIZE_LIMIT)
        List<String> teamTagList
) {
    public Team toTeam() {
        return Team.builder()
                .title(title)
                .status(Status.PROCEEDING)
                .password(TeamConstant.DEFAULT_TEAM_PASSWORD)
                .build();
    }
}
