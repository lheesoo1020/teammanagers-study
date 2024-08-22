package kr.teammanagers.tag.application.module;

import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.memo.domain.Memo;
import kr.teammanagers.tag.application.lambda.TagAction;
import kr.teammanagers.tag.application.lambda.TagRemovalAction;
import kr.teammanagers.tag.domain.*;
import kr.teammanagers.tag.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static kr.teammanagers.common.payload.code.status.ErrorStatus.*;

@Service
@RequiredArgsConstructor
public class TagModuleServiceImpl implements TagModuleService {

    private final TagRepository tagRepository;
    private final ConfidentRoleRepository confidentRoleRepository;
    private final TagTeamRepository tagTeamRepository;
    private final TeamRoleRepository teamRoleRepository;
    private final TagMemoRepository tagMemoRepository;

    @Override
    public <T> T save(final T entity, Class<T> clazz) {
        if (clazz.equals(TagTeam.class)) {
            return clazz.cast(tagTeamRepository.save((TagTeam) entity));
        } else if (clazz.equals(TeamRole.class)) {
            return clazz.cast(teamRoleRepository.save((TeamRole) entity));
        } else if (clazz.equals(TagMemo.class)) {
            return clazz.cast(tagMemoRepository.save((TagMemo) entity));
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + clazz);
        }
    }

    @Override
    public <T> T findByEntityIdAndTagId(final Long entityId, final Long tagId, Class<T> clazz) {
        if (clazz.equals(TagTeam.class)) {
            return clazz.cast(tagTeamRepository.findByTeamIdAndTagId(entityId, tagId)
                    .orElseThrow(() -> new GeneralException(TAG_TEAM_NOT_FOUND)));
        } else if (clazz.equals(TeamRole.class)) {
            return clazz.cast(teamRoleRepository.findByTeamManageIdAndTagId(entityId, tagId)
                    .orElseThrow(() -> new GeneralException(TAG_ROLE_NOT_FOUND)));
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + clazz);
        }
    }

    @Override
    public TagTeam findTagTeamByTeamIdAndTagId(final Long teamId, final Long tagId) {
        return tagTeamRepository.findByTeamIdAndTagId(teamId, tagId)
                .orElseThrow(() -> new GeneralException(TAG_TEAM_NOT_FOUND));
    }

    @Override
    public List<ConfidentRole> findAllConfidentRoleByMemberId(final Long memberId) {
        return confidentRoleRepository.findAllByMemberId(memberId);
    }

    @Override
    public List<TeamRole> findAllTeamRoleByTeamManageId(final Long teamManageId) {
        return teamRoleRepository.findAllByTeamManageId(teamManageId);
    }

    @Override
    public List<TagTeam> findAllTagTeamByTeamId(final Long teamId) {
        return tagTeamRepository.findAllByTeamId(teamId);
    }

    @Override
    public List<TagMemo> findAllTagMemoByMemoId(final Long memoId) {
        return tagMemoRepository.findAllByMemoId(memoId);
    }

    @Override
    public <T> void delete(final T entity, Class<T> clazz) {
        if (clazz.equals(TagTeam.class)) {
            tagTeamRepository.delete((TagTeam) entity);
        } else if (clazz.equals(TeamRole.class)) {
            teamRoleRepository.delete((TeamRole) entity);
        } else if (clazz.equals(TagMemo.class)) {
            tagMemoRepository.delete((TagMemo) entity);
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + clazz);
        }
    }

    @Override
    public void deleteTagTeam(final TagTeam tagTeam) {
        tagTeamRepository.delete(tagTeam);
    }

    @Override
    public void deleteTagMemo(final TagMemo tagMemo) {
        tagMemoRepository.delete(tagMemo);
    }

    @Override
    public void deleteAllTeamRole(final List<TeamRole> teamRoleList) {
        teamRoleRepository.deleteAll(teamRoleList);
    }

    @Override
    public Tag findOrCreateTag(final String tagName) {
        return tagRepository.findByName(tagName).orElseGet(() ->
                tagRepository.save(
                        Tag.builder()
                                .name(tagName)
                                .build()
                )
        );
    }

    @Override
    public void validateAndDeleteTagByTagId(final Long tagId) {
        if (!isTagInUse(tagId)) {
            tagRepository.deleteById(tagId);
        }
    }

    @Override
    public void addNewConfidentRoles(final List<String> requestedRoles, final List<String> currentRoleNames, final Member member) {
        addNewTags(requestedRoles, currentRoleNames, tagName -> saveConfidentRole(tagName, member));
    }

    @Override
    public void addNewTagMemo(final List<String> requestedTagMemo, final List<String> currentTagMemoNames, final Memo memo) {
        addNewTags(requestedTagMemo, currentTagMemoNames, tagName -> saveTagMemo(tagName, memo));
    }

    @Override
    public void removeOldConfidentRoles(final List<String> requestedRoles, final List<ConfidentRole> currentRoles) {
        removeOldTags(requestedRoles, currentRoles, confidentRole -> {
            Long tagId = confidentRole.getTag().getId();
            confidentRoleRepository.delete(confidentRole);
            validateAndDeleteTagByTagId(tagId);
        });
    }

    @Override
    public void removeOldTagMemo(final List<String> requestedTagMemo, final List<TagMemo> currentTagMemoNames) {
        removeOldTags(requestedTagMemo, currentTagMemoNames, tagMemo -> {
            Long tagId = tagMemo.getTag().getId();
            tagMemoRepository.delete(tagMemo);
            validateAndDeleteTagByTagId(tagId);
        });
    }

    @Override
    public List<Tag> getAllConfidentRole(final Long authId) {
        return confidentRoleRepository.findAllByMemberId(authId).stream()
                .map(ConfidentRole::getTag)
                .toList();
    }

    @Override
    public List<Tag> getAllTeamTag(final Long teamId) {
        return tagTeamRepository.findAllByTeamId(teamId).stream()
                .map(TagTeam::getTag)
                .toList();
    }

    @Override
    public List<Tag> getAllTeamRoleTag(final Long teamManageId) {
        return teamRoleRepository.findAllByTeamManageId(teamManageId).stream()
                .map(TeamRole::getTag)
                .toList();
    }

    private <T> void removeOldTags(List<String> requestedTags, List<T> currentTags, TagRemovalAction<T> action) {
        currentTags.stream()
                .filter(tagEntity -> !requestedTags.contains(extractTagName(tagEntity)))
                .forEach(action::execute);
    }

    private void addNewTags(List<String> requestedTags, List<String> currentTagNames, TagAction action) {
        requestedTags.stream()
                .filter(tagName -> !currentTagNames.contains(tagName))
                .forEach(action::execute);
    }

    private String extractTagName(Object tagEntity) {
        if (tagEntity instanceof ConfidentRole) {
            return ((ConfidentRole) tagEntity).getTag().getName();
        } else if (tagEntity instanceof TagMemo) {
            return ((TagMemo) tagEntity).getTag().getName();
        }
        return null;
    }

    private void saveConfidentRole(final String tagName, final Member member) {
        Tag tag = findOrCreateTag(tagName);
        confidentRoleRepository.save(
                ConfidentRole.builder()
                        .member(member)
                        .tag(tag)
                        .build()
        );
    }

    private void saveTagMemo(final String tagName, final Memo memo) {
        Tag tag = findOrCreateTag(tagName);
        tagMemoRepository.save(
                TagMemo.builder()
                        .tag(tag)
                        .memo(memo)
                        .build()
        );
    }

    private boolean isTagInUse(Long tagId) {
        return tagTeamRepository.existsByTagId(tagId) ||
                confidentRoleRepository.existsByTagId(tagId) ||
                tagMemoRepository.existsByTagId(tagId) ||
                teamRoleRepository.existsByTagId(tagId);
    }
}
