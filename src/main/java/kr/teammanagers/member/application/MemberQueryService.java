package kr.teammanagers.member.application;

import kr.teammanagers.member.dto.response.GetMemberTeam;
import kr.teammanagers.member.dto.response.GetPortfolio;
import kr.teammanagers.member.dto.response.GetProfile;
import kr.teammanagers.member.dto.response.GetSimplePortfolioList;

public interface MemberQueryService {
    GetProfile getProfile(Long authId);

    GetSimplePortfolioList getSimplePortfolioList(Long authId);

    GetPortfolio getPortfolio(Long authId, Long teamId);

    GetMemberTeam getMemberTeam(Long authId);
}
