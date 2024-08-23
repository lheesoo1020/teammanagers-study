package kr.teammanagers.auth.dto;

import jakarta.security.auth.message.AuthException;
import kr.teammanagers.member.domain.Member;
import lombok.Builder;

import java.util.Map;

import static kr.teammanagers.common.payload.code.status.ErrorStatus.AUTH_ILLEGAL_REGISTRATION_ID;


@Builder
public record OAuth2UserInfo(
        String name,
        String email,
        String profile,
        String providerId,
        String birth,
        String phoneNumber
) {
    public static OAuth2UserInfo of(final String registrationId, final Map<String, Object> attributes) throws AuthException {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            case "naver" -> ofNaver(attributes);
            default -> throw new AuthException(AUTH_ILLEGAL_REGISTRATION_ID.getMessage());
        };
    }

    private static OAuth2UserInfo ofGoogle(final Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .providerId((String) attributes.get("sub"))
                .name(((String) attributes.get("email")).split("@")[0])
                .email((String) attributes.get("email"))
                .build();
    }

    private static OAuth2UserInfo ofKakao(final Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .providerId(String.valueOf(attributes.get("id")))
                .name((String) profile.get("nickname"))
                .profile((String) profile.get("profile_image_url"))
                .build();
    }

    private static OAuth2UserInfo ofNaver(final Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2UserInfo.builder()
                .providerId((String) response.get("id"))
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .profile((String) response.get("profile_image"))
                .birth((String) response.get(("birthday")))
                .phoneNumber((String) response.get("mobile"))
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .providerId(providerId)
                .name(name)
                .email(email)
                .birth(birth)
                .phoneNumber(phoneNumber)
                .build();
    }
}