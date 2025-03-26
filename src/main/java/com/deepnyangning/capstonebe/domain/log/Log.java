package com.deepnyangning.capstonebe.domain.log;

import com.deepnyangning.capstonebe.domain.user.User;
import com.deepnyangning.capstonebe.global.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Log extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private AuthMethod authMethod;

    private boolean isFailed;

    private String failReason;

    private LocalDate entryDate;

    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private int retryCount;
}
