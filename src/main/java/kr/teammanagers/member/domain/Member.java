package kr.teammanagers.member.domain;

import jakarta.persistence.*;
import kr.teammanagers.common.AuditingField;
import kr.teammanagers.common.payload.code.status.ErrorStatus;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.member.dto.SocialType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;

    @Column()
    private String birth;

    @Column(length = 50)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(length = 20)
    private String belong;

    @Column(unique = true, nullable = false, length = 64)
    private String providerId;

    @Column(name = "refresh_token", length = 500)
    private String refreshToken;

    @Builder
    private Member(final String name, final String birth, final String email, final String phoneNumber, final String belong, final String providerId) {
        this.name = name;
        this.birth = birth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.belong = belong;
        this.providerId = providerId;
    }

    public void updateName(final String name) {
        this.name = name;
    }

    public void updatePhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateBelong(final String belong) {
        this.belong = belong;
    }

    public void updateRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public SocialType judgeLoginProcess() {
        return switch (this.providerId.length()) {
            case 43 -> SocialType.NAVER;
            case 10 -> SocialType.KAKAO;
            case 21 -> SocialType.GOOGLE;
            default -> throw new GeneralException(ErrorStatus.MEMBER_SOCIAL_TYPE_NOT_FOUND);
        };
    }
}
