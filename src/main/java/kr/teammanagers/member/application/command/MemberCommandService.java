package kr.teammanagers.member.application.command;

import kr.teammanagers.member.dto.request.UpdateProfile;
import org.springframework.web.multipart.MultipartFile;

public interface MemberCommandService {
    void updateProfile(Long authId, UpdateProfile request, MultipartFile imageFile);

    void updateCommentState(Long commentId);
}
