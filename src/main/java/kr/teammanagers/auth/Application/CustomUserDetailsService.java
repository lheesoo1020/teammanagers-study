package kr.teammanagers.auth.Application;


import kr.teammanagers.auth.dto.CustomUserDetails;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.member.application.module.MemberModuleService;
import kr.teammanagers.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

import static kr.teammanagers.common.payload.code.status.ErrorStatus.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberModuleService memberModuleService;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Member member = memberModuleService.findMemberByProviderId(username)
                .orElseThrow(() -> new GeneralException(MEMBER_NOT_FOUND));

        Collection<GrantedAuthority> authorities = Collections.emptyList(); // 필요에 따라 권한 설정

        return new CustomUserDetails(member, authorities);
    }
}