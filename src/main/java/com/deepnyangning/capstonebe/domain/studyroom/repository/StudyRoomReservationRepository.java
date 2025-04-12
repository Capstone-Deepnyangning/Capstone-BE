package com.deepnyangning.capstonebe.domain.studyroom.repository;

import com.deepnyangning.capstonebe.domain.studyroom.entity.ReservationStatus;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoom;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomReservation;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface StudyRoomReservationRepository extends JpaRepository<StudyRoomReservation, Long> {
    @Query("SELECT r FROM StudyRoomReservation r WHERE REPLACE(UPPER(r.studyRoom.name), ' ', '') LIKE %:name%")
    Page<StudyRoomReservation> findStudyRoomReservationsByStudyRoomName(@Param("name") String name, Pageable pageable);
    Page<StudyRoomReservation> findStudyRoomReservationsByUser(User user, Pageable pageable);
    List<StudyRoomReservation> findByDateAndEndTimeLessThanEqualAndStatus(LocalDate date, LocalTime time, ReservationStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM StudyRoomReservation r " +
            "WHERE r.studyRoom.id = :studyRoomId AND r.date = :date " +
            "AND r.status = :status " +
            "AND r.endTime > :startTime AND r.startTime < :endTime")
    List<StudyRoomReservation> findConflictReservations(@Param("studyRoomId") Long studyRoomId,
                                                        @Param("date") LocalDate date,
                                                        @Param("startTime") LocalTime startTime,
                                                        @Param("endTime") LocalTime endTime,
                                                        @Param("status") ReservationStatus status);
}
