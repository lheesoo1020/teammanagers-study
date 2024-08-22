package kr.teammanagers.tag.application.module;

import kr.teammanagers.member.domain.Member;
import kr.teammanagers.memo.domain.Memo;
import kr.teammanagers.tag.domain.*;

import java.util.List;

public interface TagModuleService {
    <T> T save(T entity, Class<T> clazz);

    <T> T findByEntityIdAndTagId(Long entityId, Long tagId, Class<T> clazz);

    TagTeam findTagTeamByTeamIdAndTagId(Long teamId, Long tagId);

    List<ConfidentRole> findAllConfidentRoleByMemberId(Long memberId);

    List<TeamRole> findAllTeamRoleByTeamManageId(Long teamManageId);

    List<TagTeam> findAllTagTeamByTeamId(Long teamId);

    List<TagMemo> findAllTagMemoByMemoId(Long memoId);

    <T> void delete(T entity, Class<T> clazz);

    void deleteTagTeam(TagTeam tagTeam);

    void deleteTagMemo(TagMemo tagMemo);

    void deleteAllTeamRole(List<TeamRole> teamRoleList);

    Tag findOrCreateTag(String tagName);

    void validateAndDeleteTagByTagId(Long tagId);

    void addNewConfidentRoles(List<String> requestedRoles, List<String> currentRoleNames, Member member);

    void addNewTagMemo(List<String> requestedTagMemo, List<String> currentTagMemoNames, Memo memo);

    void removeOldConfidentRoles(List<String> requestedRoles, List<ConfidentRole> currentRoles);

    void removeOldTagMemo(List<String> requestedTagMemo, List<TagMemo> currentTagMemoNames);

    List<Tag> getAllConfidentRole(Long authId);

    List<Tag> getAllTeamTag(Long teamId);

    List<Tag> getAllTeamRoleTag(Long teamManageId);
}
