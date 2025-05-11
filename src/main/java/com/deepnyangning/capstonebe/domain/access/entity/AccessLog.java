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
public class AccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
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
