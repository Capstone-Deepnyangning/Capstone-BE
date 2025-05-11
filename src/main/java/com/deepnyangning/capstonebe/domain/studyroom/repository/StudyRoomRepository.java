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
import java.util.List;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {

    @Query(value = "SELECT * FROM study_room sr " +
            "WHERE (:cursorName IS NULL OR sr.name > :cursorName) " +
            "AND sr.id NOT IN (" +
            "SELECT r.study_room_id FROM study_room_reservation r " +
            "WHERE r.date = :date " +
            "AND r.status != 'CANCELED' " +
            "GROUP BY r.study_room_id " +
            "HAVING SUM(TIMESTAMPDIFF(MINUTE, r.start_time, r.end_time)) >= :availableMinutes" +
            ") ORDER BY sr.name ASC LIMIT :limit", nativeQuery = true)
    List<StudyRoom> findAvailableStudyRoomsByDate(@Param("date") LocalDate date,
                                                  @Param("availableMinutes") int availableMinutes,
                                                  @Param("cursorName") String cursorName,
                                                  @Param("limit") int limit);

    @Query(value = "SELECT * FROM study_room sr " +
            "WHERE (:cursorName IS NULL OR sr.name > :cursorName) " +
            "AND sr.id NOT IN ( " +
            "   SELECT r.study_room_id FROM study_room_reservation r " +
            "   WHERE r.date = :date " +
            "   AND r.end_time > :startTime " +
            "   AND r.start_time < :endTime " +
            "   AND r.status != 'CANCELED' " +
            ") " +
            "ORDER BY sr.name ASC " +
            "LIMIT :limit", nativeQuery = true)
    List<StudyRoom> findAvailableStudyRooms(@Param("date") LocalDate date,
                                            @Param("startTime") LocalTime startTime,
                                            @Param("endTime") LocalTime endTime,
                                            @Param("cursorName") String cursorName,
                                            @Param("limit") int limit);

}
