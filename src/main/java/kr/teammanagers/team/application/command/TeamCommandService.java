package kr.teammanagers.team.application.command;

import kr.teammanagers.member.domain.Member;
import kr.teammanagers.team.dto.request.*;
import kr.teammanagers.team.dto.response.CreateTeamResult;
import kr.teammanagers.team.dto.response.UpdateTeamEndResult;
import org.springframework.web.multipart.MultipartFile;

public interface TeamCommandService {
    CreateTeamResult createTeam(Long authId, CreateTeam request, MultipartFile imageFile);

    void updateTeam(Long teamId, UpdateTeam request, MultipartFile imageFile);

    void createTeamPassword(Long teamId, CreateTeamPassword request);

    void joinTeam(Member auth, Long teamId, ValidatePassword request);

    UpdateTeamEndResult updateTeamState(Long authId, Long teamId);

    void createComment(CreateTeamComment request);

    void exitTeam(Long authId, Long teamId);
}
