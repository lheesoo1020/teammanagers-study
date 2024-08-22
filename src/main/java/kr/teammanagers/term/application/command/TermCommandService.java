package kr.teammanagers.term.application.command;

import kr.teammanagers.term.dto.CreateTerms;

public interface TermCommandService {
    void createTerms(Long memberId, CreateTerms request);
}
