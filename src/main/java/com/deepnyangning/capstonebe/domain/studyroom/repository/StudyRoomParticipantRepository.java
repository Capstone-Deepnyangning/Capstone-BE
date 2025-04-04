package com.deepnyangning.capstonebe.domain.studyroom.repository;

import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRoomParticipantRepository extends JpaRepository<StudyRoomParticipant, Long> {
}
