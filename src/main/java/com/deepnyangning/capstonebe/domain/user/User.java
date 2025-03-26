package com.deepnyangning.capstonebe.domain.user;

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

    private String phone;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImage;

    private boolean isActive;
}
