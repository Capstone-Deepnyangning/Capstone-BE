package com.deepnyangning.capstonebe.domain.user.repository;

import com.deepnyangning.capstonebe.domain.user.entity.Role;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    boolean existsByIdentifierAndName(String identifier, String name);

    List<User> findByRole(Role role);
}
