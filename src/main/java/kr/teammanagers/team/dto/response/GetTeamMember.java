package kr.teammanagers.team.dto.response;

import kr.teammanagers.team.dto.TeamMemberDto;
import lombok.Builder;

import java.util.List;

@Builder
public record GetTeamMember(
        List<TeamMemberDto> teamMember
) {
    public static GetTeamMember from(List<TeamMemberDto> teamMember) {
        return GetTeamMember.builder()
                .teamMember(teamMember)
                .build();
    }
}
