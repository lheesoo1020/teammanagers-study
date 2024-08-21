package kr.teammanagers.calendar.domain;

import jakarta.persistence.*;
import kr.teammanagers.common.AuditingField;
import kr.teammanagers.team.domain.TeamManage;
import lombok.*;

@Entity
@Table(name = "team_calendar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamCalendar extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isAlarmed;

    // Mapping
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_mange_id")
    private TeamManage teamManage;

    @Builder
    private TeamCalendar(final Boolean isAlarmed) {
        this.isAlarmed = isAlarmed;
    }
}
