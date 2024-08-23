package kr.teammanagers.member.application.module;

import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.member.domain.Comment;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.member.repository.CommentRepository;
import kr.teammanagers.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static kr.teammanagers.common.payload.code.status.ErrorStatus.MEMBER_COMMENT_NOT_FOUND;
import static kr.teammanagers.common.payload.code.status.ErrorStatus.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemberModuleServiceImpl implements MemberModuleService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Override
    public <T> T save(final T entity, Class<T> clazz) {
        if (clazz.equals(Member.class)) {
            return clazz.cast(memberRepository.save((Member) entity));
        } else if (clazz.equals(Comment.class)) {
            return clazz.cast(commentRepository.save((Comment) entity));
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + clazz);
        }
    }

    @Override
    public Optional<Member> findMemberByProviderId(final String providerId) {
        return memberRepository.findByProviderId(providerId);
    }

    @Override
    public Member findMemberById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MEMBER_NOT_FOUND));
    }

    @Override
    public Comment findCommentById(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(MEMBER_COMMENT_NOT_FOUND));
    }

    @Override
    public List<Comment> findCommentAllByMemberId(final Long memberId) {
        return commentRepository.findAllByMemberId(memberId);
    }
}
