package kr.teammanagers.member.application;

import kr.teammanagers.global.config.AmazonConfig;
import kr.teammanagers.global.provider.AmazonS3Provider;
import kr.teammanagers.member.domain.Comment;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.member.dto.request.UpdateProfile;
import kr.teammanagers.member.repository.CommentRepository;
import kr.teammanagers.member.repository.MemberRepository;
import kr.teammanagers.tag.application.module.TagCommandModuleService;
import kr.teammanagers.tag.domain.ConfidentRole;
import kr.teammanagers.tag.repository.ConfidentRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ConfidentRoleRepository confidentRoleRepository;

    private final TagCommandModuleService tagCommandModuleService;
    private final AmazonConfig amazonConfig;
    private final AmazonS3Provider amazonS3Provider;

    @Override
    public void updateProfile(final Long authId, final UpdateProfile request, final MultipartFile imageFile) {
        Member member = memberRepository.findById(authId).orElseThrow(RuntimeException::new);       // TODO : 예외 처리 필요

        updateMemberName(request.name(), member);
        updateMemberPhoneNumber(request.phoneNumber(), member);
        updateMemberBelong(request.belong(), member);
        updateConfidentRoles(request.confidentRole(), member);
        updateProfileImageIfPresent(imageFile, member);
    }

    @Override
    public void updateCommentState(final Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(RuntimeException::new);     // TODO : 예외 처리 필요
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

        List<ConfidentRole> currentRoles = confidentRoleRepository.findAllByMemberId(member.getId());
        List<String> currentRoleNames = currentRoles.stream()
                .map(role -> role.getTag().getName())
                .toList();

        tagCommandModuleService.addNewConfidentRoles(requestedRoles, currentRoleNames, member);
        tagCommandModuleService.removeOldConfidentRoles(requestedRoles, currentRoles);
    }

    private void updateProfileImageIfPresent(final MultipartFile imageFile, final Member member) {
        if (imageFile != null && !imageFile.isEmpty()) {
            updateProfileImage(imageFile, member);
        }
    }
}
