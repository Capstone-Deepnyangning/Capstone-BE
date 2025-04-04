package com.deepnyangning.capstonebe.domain.studyroom.service;

import com.deepnyangning.capstonebe.domain.studyroom.repository.StudyRoomParticipantRepository;
import com.deepnyangning.capstonebe.domain.studyroom.repository.StudyRoomReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyRoomService {
    private final StudyRoomReservationRepository reservationRepository;
    private final StudyRoomParticipantRepository participantRepository;
}
