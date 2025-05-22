package com.deepnyangning.capstonebe.domain.user.entity;

import com.deepnyangning.capstonebe.domain.notification.entity.FcmToken;
import com.deepnyangning.capstonebe.domain.user.entity.Role;
import com.deepnyangning.capstonebe.global.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identifier;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    private boolean isFaceRegistered = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FcmToken> fcmTokens = new ArrayList<>();
}
