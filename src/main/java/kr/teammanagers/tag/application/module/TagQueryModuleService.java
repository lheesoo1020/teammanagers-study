package kr.teammanagers.tag.application.module;

import kr.teammanagers.tag.domain.Tag;

import java.util.List;

public interface TagQueryModuleService {
    List<Tag> getAllConfidentRole(Long authId);

    List<Tag> getAllTeamTag(Long teamId);

    List<Tag> getAllTeamRoleTag(Long teamManageId);
}
