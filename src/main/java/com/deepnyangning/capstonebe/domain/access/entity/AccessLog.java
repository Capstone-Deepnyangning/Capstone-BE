package com.deepnyangning.capstonebe.domain.access.entity;

import com.deepnyangning.capstonebe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idx_access_type_time", columnList = "access_type, access_time"),
        @Index(name = "idx_user_id", columnList = "user_id")
})
public class AccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "FK_access_log_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE"
            )
    )
    private User user;

    @Enumerated(EnumType.STRING)
    private AccessType accessType;

    @Enumerated(EnumType.STRING)
    private AuthMethod authMethod;

    private LocalDateTime accessTime;

    private double similarity;

    @PrePersist
    protected void onCreate(){
        this.accessTime = LocalDateTime.now();
    }
}
