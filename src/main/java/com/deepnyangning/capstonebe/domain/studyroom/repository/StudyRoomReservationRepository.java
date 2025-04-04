package com.deepnyangning.capstonebe.domain.studyroom.repository;

import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRoomReservationRepository extends JpaRepository<StudyRoomReservation, Long> {
}
