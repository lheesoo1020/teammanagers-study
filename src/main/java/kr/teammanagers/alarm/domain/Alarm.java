package kr.teammanagers.alarm.domain;

import jakarta.persistence.*;
import kr.teammanagers.common.AuditingField;
import kr.teammanagers.team.domain.TeamManage;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alarm")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Alarm extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AlarmType type;

    @Column
    private Long referenceId;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private Boolean isRead;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamManage_id")
    private TeamManage teamManage;

    @Builder
    private Alarm(final Long referenceId, final AlarmType type, final LocalDateTime date, final Boolean isRead) {
        this.referenceId = referenceId;
        this.type = type;
        this.isRead = isRead;
        this.date = date;
    }

    public void read() {
        this.isRead = true;
    }
}
