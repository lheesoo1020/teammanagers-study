package kr.teammanagers.feedback.domain;

import jakarta.persistence.*;
import kr.teammanagers.common.AuditingField;
import kr.teammanagers.storage.domain.TeamData;
import kr.teammanagers.team.domain.TeamManage;
import lombok.*;

@Entity
@Table(name = "feedback")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Feedback extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String content;

    // Mapping
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_manage_id")
    private TeamManage teamManage;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_data_id")
    private TeamData teamData;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Feedback parent;

    @Builder
    private Feedback(final String content) {
        this.content = content;
    }
}
