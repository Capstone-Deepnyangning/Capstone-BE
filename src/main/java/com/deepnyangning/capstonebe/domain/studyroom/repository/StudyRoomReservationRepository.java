package com.deepnyangning.capstonebe.domain.studyroom.repository;

import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoom;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomReservation;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRoomReservationRepository extends JpaRepository<StudyRoomReservation, Long> {
    @Query("SELECT r FROM StudyRoomReservation r WHERE REPLACE(UPPER(r.studyRoom.name), ' ', '') LIKE %:name%")
    Page<StudyRoomReservation> findStudyRoomReservationsByStudyRoomName(@Param("name") String name, Pageable pageable);
    Page<StudyRoomReservation> findStudyRoomReservationsByUser(User user, Pageable pageable);
}
