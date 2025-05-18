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
    boolean existsByUserAndDateAndStatusNot(User user, LocalDate date, ReservationStatus status);

    @Query(value = """
        SELECT r.* FROM study_room_reservation r
        JOIN study_room sr ON r.study_room_id = sr.id
        WHERE (:name IS NULL OR REPLACE(UPPER(sr.name), ' ', '') LIKE %:name%)
        AND (
            :cursorDate IS NULL
            OR (r.date < :cursorDate OR (r.date = :cursorDate AND r.start_time < :cursorStartTime))
        )
        ORDER BY r.date DESC, r.start_time DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<StudyRoomReservation> findByStudyRoomName(@Param("name") String name,
                                                   @Param("cursorDate") LocalDate cursorDate,
                                                   @Param("cursorStartTime") LocalTime cursorStartTime,
                                                   @Param("limit") int limit);

    @Query(value = "SELECT r.* FROM study_room_reservation r " +
            "WHERE r.user_id = :userId " +
            "AND (:cursorDate IS NULL OR r.date < :cursorDate) " +
            "ORDER BY r.date DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<StudyRoomReservation> findByUser(@Param("userId") Long userId,
                                          @Param("cursorDate") LocalDate cursorDate,
                                          @Param("limit") int limit);
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

    List<StudyRoomReservation> findByDateAndStartTimeAndStatus(LocalDate date, LocalTime startTime, ReservationStatus status);
}
