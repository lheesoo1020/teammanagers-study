package kr.teammanagers.todo.domain;

import jakarta.persistence.*;
import kr.teammanagers.common.AuditingField;
import kr.teammanagers.common.Status;
import kr.teammanagers.team.domain.TeamManage;
import lombok.*;

@Entity
@Table(name = "todo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Todo extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    // Mapping
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_manage_id")
    private TeamManage teamManage;

    @Builder
    private Todo(final String title, final Status status) {
        this.title = title;
        this.status = status;
    }

    public void changeTitle(final String title) {
        this.title = title;
    }

    public void switchStatus() {
        if (this.status == Status.PROCEEDING) this.status = Status.COMPLETED;
        else this.status = Status.PROCEEDING;
    }

}
