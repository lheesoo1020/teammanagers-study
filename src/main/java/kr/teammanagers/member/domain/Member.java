package kr.teammanagers.member.domain;

import jakarta.persistence.*;
import kr.teammanagers.common.AuditingField;
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
}
