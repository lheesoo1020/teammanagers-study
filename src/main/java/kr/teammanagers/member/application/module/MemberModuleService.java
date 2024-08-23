package kr.teammanagers.member.application.module;

import kr.teammanagers.member.domain.Comment;
import kr.teammanagers.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberModuleService {

    <T> T save(T entity, Class<T> clazz);

    Optional<Member> findMemberByProviderId(String providerId);

    Member findMemberById(Long memberId);

    Comment findCommentById(Long commentId);

    List<Comment> findCommentAllByMemberId(Long memberId);
}
