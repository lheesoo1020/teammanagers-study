package kr.teammanagers.term.application.module;

import kr.teammanagers.term.domain.CheckedTerms;

public interface TermModuleService {
    CheckedTerms save(CheckedTerms checkedTerms);
}
