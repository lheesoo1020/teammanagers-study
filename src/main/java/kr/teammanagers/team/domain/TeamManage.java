package kr.teammanagers.team.domain;

import jakarta.persistence.*;
import kr.teammanagers.common.AuditingField;
import kr.teammanagers.member.domain.Member;
import lombok.*;

@Entity
@Table(name = "team_manage")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamManage extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    // Mapping
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    private TeamManage(final Boolean isDeleted, final Member member, final Team team) {
        this.isDeleted = isDeleted;
        this.member = member;
        this.team = team;
    }
}
