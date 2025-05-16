package com.deepnyangning.capstonebe.domain.notification.repository;

import com.deepnyangning.capstonebe.domain.notification.entity.FcmToken;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    List<FcmToken> findByUser(User user);

    Optional<FcmToken> findByToken(String token);

    void deleteByUserAndToken(User user, String token);
}
