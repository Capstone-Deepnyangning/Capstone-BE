package com.deepnyangning.capstonebe.domain.user.repository;

import com.deepnyangning.capstonebe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
