package kr.teammanagers.schedule.domain;

import jakarta.persistence.*;
import kr.teammanagers.common.AuditingField;
import kr.teammanagers.team.domain.TeamManage;
import lombok.*;

@Entity
@Table(name = "schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Schedule extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "monday_value", length = 1000))
    })
    private TimeTable monday;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "tuesday_value", length = 1000))
    })
    private TimeTable tuesday;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wednesday_value", length = 1000))
    })
    private TimeTable wednesday;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "thursday_value", length = 1000))
    })
    private TimeTable thursday;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "friday_value", length = 1000))
    })
    private TimeTable friday;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "saturday_value", length = 1000))
    })
    private TimeTable saturday;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "sunday_value", length = 1000))
    })
    private TimeTable sunday;

    // Mapping
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "team_manage_id")
    private TeamManage teamManage;

    @Builder
    private Schedule(final TimeTable monday, final TimeTable tuesday, final TimeTable wednesday, final TimeTable thursday, final TimeTable friday, final TimeTable saturday, final TimeTable sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public void update(final TimeTable monday, final TimeTable tuesday, final TimeTable wednesday, final TimeTable thursday, final TimeTable friday, final TimeTable saturday, final TimeTable sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }
}
