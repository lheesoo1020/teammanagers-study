package kr.teammanagers.team.dto.response;

import kr.teammanagers.team.dto.SimpleTeamMemberDto;
import lombok.Builder;

import java.util.List;

@Builder
public record GetSimpleTeamMember(
        List<SimpleTeamMemberDto> teamMember
) {
    public static GetSimpleTeamMember from(final List<SimpleTeamMemberDto> teamMember) {
        return GetSimpleTeamMember.builder()
                .teamMember(teamMember)
                .build();
    }
}
