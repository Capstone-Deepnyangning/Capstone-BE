package com.deepnyangning.capstonebe.domain.statistics.entity;

import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.global.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyStay extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate date;

    private int totalStay;
}
