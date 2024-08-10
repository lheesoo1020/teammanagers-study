package kr.teammanagers.storage.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.teammanagers.common.AuditingField;
import kr.teammanagers.common.EntityStatus;
import kr.teammanagers.team.domain.TeamManage;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "team_data")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamData extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(length = 20)
    @NotNull
    private String size;

    @Column()
    @NotNull
    private LocalDateTime uploadAt;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private String uploader;

    @Column(name = "file_extension", nullable = false)
    private String fileExtension;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_status", nullable = false)
    private EntityStatus entityStatus;

    // Mapping
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_manage_id")
    private TeamManage teamManage;



    @Builder
    private TeamData(final String title, final String size, final String fileUrl) {
        this.title = title;
        this.size = size;
        this.fileUrl = fileUrl;
        this.entityStatus = EntityStatus.ACTIVE;
    }
}
