package com.deepnyangning.capstonebe.domain.studyroom.entity;

import com.deepnyangning.capstonebe.global.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyRoomParticipant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private StudyRoomReservation reservation;

    private String identifier;

    private String name;
}
