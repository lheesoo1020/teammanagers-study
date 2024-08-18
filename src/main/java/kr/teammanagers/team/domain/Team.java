package kr.teammanagers.team.domain;

import jakarta.persistence.*;
import kr.teammanagers.common.AuditingField;
import kr.teammanagers.common.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(name = "team_code", length = 8)
    private String teamCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private String password;

    @Builder
    private Team(final String title, final String teamCode, final Status status, final String password) {
        this.title = title;
        this.teamCode = teamCode;
        this.status = status;
        this.password = password;
    }

    public void updateTitle(final String title) {
        this.title = title;
    }

    public void updateTeamCode(final String teamCode) {
        this.teamCode = teamCode;
    }

    public void updatePassword(final String password) {
        this.password = password;
    }

    public void updateStatus(final Status status) {
        this.status = status;
    }
}
