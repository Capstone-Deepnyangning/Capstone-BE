package com.deepnyangning.capstonebe.domain.studyroom.repository;

import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {
    StudyRoom findByName(String name);

    @Query("SELECT sr FROM StudyRoom sr WHERE sr.id NOT IN (" +
            "SELECT r.studyRoom.id FROM StudyRoomReservation r " +
            "WHERE r.date = :date AND r.endTime >= :startTime AND r.startTime <= :endTime " +
            "AND r.status != 'CANCELED')")
    Page<StudyRoom> findAvailableStudyRooms(@Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime, Pageable pageable);
}
