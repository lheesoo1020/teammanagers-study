package kr.teammanagers.term.application.command;

import kr.teammanagers.member.application.module.MemberModuleService;
import kr.teammanagers.term.application.module.TermModuleService;
import kr.teammanagers.term.domain.CheckedTerms;
import kr.teammanagers.term.dto.CreateTerms;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TermCommandServiceImpl implements TermCommandService {

    private final MemberModuleService memberModuleService;
    private final TermModuleService termModuleService;

    @Override
    public void createTerms(Long memberId, CreateTerms request) {
        CheckedTerms newCheckedTerms = CreateTerms.from(request);
        newCheckedTerms.setMember(memberModuleService.findMemberById(memberId));
        termModuleService.save(newCheckedTerms);
    }
}
