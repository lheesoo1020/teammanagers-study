package kr.teammanagers.team.dto;

import kr.teammanagers.team.domain.TeamManage;
import lombok.Builder;

@Builder
public record SimpleTeamManageDto(
        Long teamManageId,
        String name
) {
    public static SimpleTeamManageDto from(TeamManage teamManage) {
        return SimpleTeamManageDto.builder().
                teamManageId(teamManage.getId())
                .name(teamManage.getMember().getName())
                .build();
    }
}
