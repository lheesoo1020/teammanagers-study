package kr.teammanagers.team.dto.request;

import kr.teammanagers.team.dto.RegisterCommentDto;
import kr.teammanagers.team.exception.validator.TeamMemberSize;

import java.util.List;

import static kr.teammanagers.team.constant.TeamErrorConstant.TEAM_MEMBER_SIZE;

public record CreateTeamComment(
        @TeamMemberSize(message = TEAM_MEMBER_SIZE)
        List<RegisterCommentDto> commentList
) {
}
