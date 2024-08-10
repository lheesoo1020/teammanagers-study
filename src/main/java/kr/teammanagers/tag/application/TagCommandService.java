package kr.teammanagers.tag.application;

import kr.teammanagers.tag.request.CreateRoleTag;
import kr.teammanagers.tag.request.UpdateRoleTag;
import kr.teammanagers.tag.request.UpdateTeamTag;

public interface TagCommandService {
    void updateTeamTag(Long teamId, Long tagId, UpdateTeamTag request);

    void deleteTeamTag(Long teamId, Long tagId);

    void createRoleTag(Long teamManageId, CreateRoleTag request);

    void updateRoleTag(Long teamManageId, Long tagId, UpdateRoleTag request);

    void deleteRoleTag(Long teamManageId, Long tagId);
}
