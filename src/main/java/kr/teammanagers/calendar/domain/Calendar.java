package kr.teammanagers.calendar.domain;

import jakarta.persistence.*;
import kr.teammanagers.common.AuditingField;
import kr.teammanagers.common.Status;
import kr.teammanagers.team.domain.Team;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "calendar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Calendar extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(length = 100)
    private String content;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    //Mapping
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    private Calendar(final String title, final String content, final LocalDateTime date, final Status status) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.status = status;
    }

    public void updateTitle(final String title) {
        this.title = title;
    }

    public void updateContent(final String content) {
        this.content = content;
    }

    public void switchStatus() {
        if (this.status == Status.PROCEEDING) this.status = Status.COMPLETED;
        else this.status = Status.PROCEEDING;
    }
}
