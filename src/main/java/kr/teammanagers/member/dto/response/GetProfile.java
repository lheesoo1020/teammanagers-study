package kr.teammanagers.member.dto.response;

import kr.teammanagers.member.domain.Comment;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.tag.domain.Tag;
import kr.teammanagers.tag.dto.TagDto;
import kr.teammanagers.team.dto.CommentDto;
import lombok.Builder;

import java.util.List;

@Builder
public record GetProfile(
        String imageUrl,
        String name,
        String phoneNumber,
        String belong,
        List<TagDto> confidentRole,
        List<CommentDto> commentList
) {

    public static GetProfile of(final Member member, final List<Tag> tagList, final List<Comment> commentList, final String imageUrl) {
        return GetProfile.builder()
                .imageUrl(imageUrl)
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .belong(member.getBelong())
                .confidentRole(
                        tagList.stream()
                                .map(TagDto::from)
                                .toList()
                )
                .commentList(
                        commentList.stream()
                                .map(CommentDto::from)
                                .toList()
                )
                .build();
    }
}
