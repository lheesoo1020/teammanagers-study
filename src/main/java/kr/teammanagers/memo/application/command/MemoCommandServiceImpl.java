package kr.teammanagers.memo.application.command;

import kr.teammanagers.memo.application.module.MemoModuleService;
import kr.teammanagers.memo.domain.Memo;
import kr.teammanagers.memo.dto.request.CreateMemo;
import kr.teammanagers.memo.dto.request.UpdateMemo;
import kr.teammanagers.tag.application.module.TagModuleService;
import kr.teammanagers.tag.domain.TagMemo;
import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.team.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemoCommandServiceImpl implements MemoCommandService {

    private final MemoModuleService memoModuleService;
    private final TeamModuleService teamModuleService;
    private final TagModuleService tagModuleService;

    @Override
    public void createMemo(final Long teamId, final CreateMemo request) {
        Team team = teamModuleService.findById(teamId, Team.class);
        Memo memo = request.toMemo();
        memo.setTeam(team);
        memoModuleService.save(memo);

        request.tagList().stream()
                .map(tagModuleService::findOrCreateTag)
                .forEach(tag -> {
                    tagModuleService.save(
                            TagMemo.builder()
                                    .tag(tag)
                                    .memo(memo)
                                    .build(),
                            TagMemo.class
                    );
                });
    }

    @Override
    public void updateMemo(final Long memoId, final UpdateMemo request) {
        Memo memo = memoModuleService.findById(memoId);

        updateTitle(request.title(), memo);
        updateTagMemoList(request.tagList(), memo);
        updateContent(request.content(), memo);
    }

    @Override
    public void deleteMemo(final Long memoId) {
        tagModuleService.findAllTagMemoByMemoId(memoId)
                .forEach(tagMemo -> {
                    Long oldTagId = tagMemo.getTag().getId();
                    tagModuleService.deleteTagMemo(tagMemo);
                    tagModuleService.validateAndDeleteTagByTagId(oldTagId);
                });
        memoModuleService.deleteById(memoId);
    }

    private void updateTitle(final String title, final Memo memo) {
        if (title != null) {
            memo.updateTitle(title);
        }
    }

    private void updateTagMemoList(final List<String> requestedTagMemoList, final Memo memo) {
        if (requestedTagMemoList == null) {
            return;
        }

        List<TagMemo> currentTagMemoList = tagModuleService.findAllTagMemoByMemoId(memo.getId());
        List<String> currentTagMemoNames = currentTagMemoList.stream()
                .map(tagMemo -> tagMemo.getTag().getName())
                .toList();

        tagModuleService.addNewTagMemo(requestedTagMemoList, currentTagMemoNames, memo);
        tagModuleService.removeOldTagMemo(requestedTagMemoList, currentTagMemoList);
    }

    private void updateContent(final String content, final Memo memo) {
        if (content != null) {
            memo.updateContent(content);
        }
    }
}
