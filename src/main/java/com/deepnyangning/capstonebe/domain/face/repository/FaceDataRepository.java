package com.deepnyangning.capstonebe.domain.face.repository;

import com.deepnyangning.capstonebe.domain.face.entity.FaceData;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaceDataRepository extends JpaRepository<FaceData, Long> {
    void deleteAllByUser(User user);
}
