package com.deepnyangning.capstonebe.domain.studyroom.service;

import com.deepnyangning.capstonebe.domain.studyroom.dto.ParticipantRequest;
import com.deepnyangning.capstonebe.domain.studyroom.entity.ReservationStatus;
import com.deepnyangning.capstonebe.domain.studyroom.repository.StudyRoomParticipantRepository;
import com.deepnyangning.capstonebe.domain.studyroom.repository.StudyRoomReservationRepository;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.service.UserService;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class StudyRoomParticipantValidator {
    private final UserService userService;
    private final StudyRoomReservationRepository reservationRepository;
    private final StudyRoomParticipantRepository participantRepository;

    public void validateParticipant(String identifier, ParticipantRequest participantRequest){
        String targetIdentifier = participantRequest.getIdentifier();
        LocalDate date = participantRequest.getDate();

        checkSelfParticipation(identifier, targetIdentifier);
        validateDuplicatedReservationOrParticipation(identifier,date);
        validateDuplicatedReservationOrParticipation(targetIdentifier,date);
    }

    public void checkSelfParticipation(String identifier, String participantIdentifier){
        if(identifier.equals(participantIdentifier)){
            throw new CustomException(ErrorCode.INVALID_SELF_PARTICIPATION);
        }
    }

    public void validateDuplicatedReservationOrParticipation(String identifier, LocalDate date){
        User user = userService.findByIdentifier(identifier);
        boolean hasReservation = reservationRepository.existsByUserAndDateAndStatusNot(user, date, ReservationStatus.CANCELED);
        boolean hasParticipation = participantRepository.existsByIdentifierAndDate(user.getIdentifier(), date);

        if(hasReservation || hasParticipation){
            throw new CustomException(ErrorCode.ALREADY_RESERVED_ON_DATE);
        }
    }
}
