package com.deepnyangning.capstonebe.domain.facedata;

import com.deepnyangning.capstonebe.domain.user.User;
import com.deepnyangning.capstonebe.global.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaceData extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    private byte[] faceVector;

    @Enumerated(EnumType.STRING)
    private TrainingMethod trainingMethod;

    private float accuracy;
}
