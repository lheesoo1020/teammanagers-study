package kr.teammanagers.member.application.command;

import kr.teammanagers.global.config.AmazonConfig;
import kr.teammanagers.global.provider.AmazonS3Provider;
import kr.teammanagers.member.application.module.MemberModuleService;
import kr.teammanagers.member.domain.Comment;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.member.dto.request.UpdateProfile;
import kr.teammanagers.tag.application.module.TagModuleService;
import kr.teammanagers.tag.domain.ConfidentRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberModuleService memberModuleService;
    private final TagModuleService tagModuleService;
    private final AmazonConfig amazonConfig;
    private final AmazonS3Provider amazonS3Provider;

    @Override
    public void updateProfile(final Long authId, final UpdateProfile request, final MultipartFile imageFile) {
        Member member = memberModuleService.findMemberById(authId);

        updateMemberName(request.name(), member);
        updateMemberPhoneNumber(request.phoneNumber(), member);
        updateMemberBelong(request.belong(), member);
        updateConfidentRoles(request.confidentRole(), member);
        updateProfileImageIfPresent(imageFile, member);
    }

    @Override
    public void updateCommentState(final Long commentId) {
        Comment comment = memberModuleService.findCommentById(commentId);
        comment.updateIsHidden();
    }

    private void updateProfileImage(final MultipartFile imageFile, final Member member) {
        String memberProfilePath = amazonConfig.getMemberProfilePath();
        if (amazonS3Provider.isFileExist(memberProfilePath, member.getId())) {
            amazonS3Provider.deleteFile(memberProfilePath, member.getId());
        }
        amazonS3Provider.uploadImage(memberProfilePath, member.getId(), imageFile);
    }

    private void updateMemberPhoneNumber(final String phoneNumber, final Member member) {
        if (phoneNumber != null) {
            member.updatePhoneNumber(phoneNumber);
        }
    }

    private void updateMemberName(final String name, final Member member) {
        if (name != null) {
            member.updateName(name);
        }
    }

    private void updateMemberBelong(final String belong, final Member member) {
        if (belong != null) {
            member.updateBelong(belong);
        }
    }

    private void updateConfidentRoles(final List<String> requestedRoles, final Member member) {
        if (requestedRoles == null) {
            return;
        }

        List<ConfidentRole> currentRoles = tagModuleService.findAllConfidentRoleByMemberId(member.getId());
        List<String> currentRoleNames = currentRoles.stream()
                .map(role -> role.getTag().getName())
                .toList();

        tagModuleService.addNewConfidentRoles(requestedRoles, currentRoleNames, member);
        tagModuleService.removeOldConfidentRoles(requestedRoles, currentRoles);
    }

    private void updateProfileImageIfPresent(final MultipartFile imageFile, final Member member) {
        if (imageFile != null && !imageFile.isEmpty()) {
            updateProfileImage(imageFile, member);
        }
    }
}
