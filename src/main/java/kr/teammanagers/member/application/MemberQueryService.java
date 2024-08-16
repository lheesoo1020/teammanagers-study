package kr.teammanagers.member.application;

import kr.teammanagers.member.dto.response.*;

public interface MemberQueryService {
    GetProfile getProfile(Long authId);

    GetMyTodoList getMyTodoList(Long authId);

    GetSimplePortfolioList getSimplePortfolioList(Long authId);

    GetPortfolio getPortfolio(Long authId, Long teamId);

    GetMemberTeam getMemberTeam(Long authId);
}
