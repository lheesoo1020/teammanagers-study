package kr.teammanagers.auth.dto;

import kr.teammanagers.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomUserDetails extends User implements OAuth2User {

    private final Member member;
    private final Map<String, Object> attributes;

    public CustomUserDetails(final Member member, final Collection<? extends GrantedAuthority> authorities) {
        super(member.getEmail(), "", authorities); // password는 빈 문자열로 설정
        this.member = member;
        this.attributes = Collections.emptyMap();
    }

    public CustomUserDetails(Member member, Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
        super(member.getEmail(), "", authorities); // password는 빈 문자열로 설정
        this.member = member;
        this.attributes = attributes;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return member.getProviderId();
    }
}