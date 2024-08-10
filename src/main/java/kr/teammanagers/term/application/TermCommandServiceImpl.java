package kr.teammanagers.term.application;

import kr.teammanagers.common.payload.code.status.ErrorStatus;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.member.repository.MemberRepository;
import kr.teammanagers.term.domain.CheckedTerms;
import kr.teammanagers.term.dto.CreateTerms;
import kr.teammanagers.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TermCommandServiceImpl implements TermCommandService {

    private final TermRepository termRepository;
    private final MemberRepository memberRepository;

    @Override
    public void createTerms(Long memberId, CreateTerms request) {

        CheckedTerms newCheckedTerms = CreateTerms.from(request);
        newCheckedTerms.setMember(memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND)));

        termRepository.save(newCheckedTerms);
    }
}
