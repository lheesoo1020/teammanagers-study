package kr.teammanagers.term.application.module;

import kr.teammanagers.term.domain.CheckedTerms;
import kr.teammanagers.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermModuleServiceImpl implements TermModuleService {

    private final TermRepository termRepository;

    @Override
    public CheckedTerms save(final CheckedTerms checkedTerms) {
        return termRepository.save(checkedTerms);
    }
}
