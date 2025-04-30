package com.deepnyangning.capstonebe.domain.user.entity;

import com.deepnyangning.capstonebe.domain.user.entity.Role;
import com.deepnyangning.capstonebe.global.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
}
