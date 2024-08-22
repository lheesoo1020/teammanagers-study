package kr.teammanagers.auth.Application;
import kr.teammanagers.auth.dto.OAuth2UserInfo;
import kr.teammanagers.auth.dto.PrincipalDetails;
import kr.teammanagers.global.exception.AuthException;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException, AuthException {
        // 1. 유저 정보(attributes) 가져오기
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

        // 2. resistrationId 가져오기 (third-party id)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. userNameAttributeName 가져오기
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // 4. 유저 정보 dto 생성
        OAuth2UserInfo oAuth2UserInfo = null;
        try {
            oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);
        } catch (jakarta.security.auth.message.AuthException e) {
            throw new RuntimeException(e);
        }

        // 5. 회원가입 및 로그인
        Member member = getOrSave(oAuth2UserInfo);
        boolean isNewUser = isNewUser(member);

        // 6. OAuth2User로 반환
        return new PrincipalDetails(member, oAuth2UserAttributes, userNameAttributeName, isNewUser);
    }


    private Member getOrSave(OAuth2UserInfo oAuth2UserInfo) {
        return memberRepository.findByProviderId(oAuth2UserInfo.providerId())
                .orElseGet(() -> memberRepository.save(oAuth2UserInfo.toEntity()));
    }
    private boolean isNewUser(Member member) {
        // 신규 사용자 여부 판단 로직
        return member.getCreatedAt() != null && member.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(3));
    }
}