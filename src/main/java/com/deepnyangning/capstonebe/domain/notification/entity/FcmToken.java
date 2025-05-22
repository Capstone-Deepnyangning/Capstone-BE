package com.deepnyangning.capstonebe.domain.notification.entity;

import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.global.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FcmToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void assignUser(User user){
        this.user = user;
        user.getFcmTokens().add(this);
    }
}
