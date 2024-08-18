package kr.teammanagers.memo.application.module;

import kr.teammanagers.common.payload.code.status.ErrorStatus;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.memo.domain.Memo;
import kr.teammanagers.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoModuleServiceImpl implements MemoModuleService {

    private final MemoRepository memoRepository;

    @Override
    public Memo findById(final Long id) {
        return memoRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMO_NOT_FOUND));
    }

    @Override
    public List<Memo> findAllByTeamId(final Long teamId) {
        return memoRepository.findAllByTeamId(teamId);
    }

    @Override
    public Memo save(final Memo memo) {
        return memoRepository.save(memo);
    }

    @Override
    public void deleteById(final Long memoId) {
        memoRepository.deleteById(memoId);
    }
}
