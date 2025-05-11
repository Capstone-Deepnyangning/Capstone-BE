package com.deepnyangning.capstonebe.domain.studyroom.service;

import com.deepnyangning.capstonebe.domain.studyroom.dto.ParticipantRequest;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomParticipant;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomReservation;
import com.deepnyangning.capstonebe.domain.studyroom.mapper.StudyRoomParticipantMapper;
import com.deepnyangning.capstonebe.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyRoomParticipantService {
    private final StudyRoomParticipantValidator validator;
    private final StudyRoomParticipantMapper participantMapper;
    private final UserService userService;

    public boolean existsParticipant(String identifier, ParticipantRequest participantRequest){
        validator.validateParticipant(identifier, participantRequest);

        return userService.existsByIdentifierAndName(participantRequest.getIdentifier(), participantRequest.getName());
    }

    @Transactional
    public void saveParticipants(List<ParticipantRequest> participantRequests, StudyRoomReservation reservation){
        for(ParticipantRequest request:participantRequests){
            StudyRoomParticipant participant = participantMapper.toEntity(request);
            participant.setReservation(reservation);
            reservation.getParticipants().add(participant);
        }
    }

    @Transactional
    public void updateParticipants(List<ParticipantRequest> newRequests, StudyRoomReservation reservation){
        reservation.getParticipants().clear();
        saveParticipants(newRequests, reservation);
    }
}
