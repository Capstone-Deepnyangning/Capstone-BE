package com.deepnyangning.capstonebe.domain.access.entity;

import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AuthMethod authMethod;

    private LocalDateTime accessTime;

    private Double similarity;

    @PrePersist
    protected void onCreate(){
        this.accessTime = LocalDateTime.now();
    }
}
