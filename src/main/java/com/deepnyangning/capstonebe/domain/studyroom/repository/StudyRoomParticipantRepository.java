package com.deepnyangning.capstonebe.domain.studyroom.repository;

import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomParticipant;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface StudyRoomParticipantRepository extends JpaRepository<StudyRoomParticipant, Long> {
    @Query("""
        SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
        FROM StudyRoomParticipant p
        WHERE p.identifier = :identifier
        AND p.reservation.date = :date
        AND p.reservation.status != 'CANCELED'
    """)
    boolean existsByIdentifierAndDate(@Param("identifier") String identifier, @Param("date") LocalDate date);
}
