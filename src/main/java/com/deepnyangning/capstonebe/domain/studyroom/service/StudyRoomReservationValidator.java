package com.deepnyangning.capstonebe.domain.studyroom.service;

import com.deepnyangning.capstonebe.domain.studyroom.entity.ReservationStatus;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoom;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomReservation;
import com.deepnyangning.capstonebe.domain.studyroom.repository.StudyRoomParticipantRepository;
import com.deepnyangning.capstonebe.domain.studyroom.repository.StudyRoomReservationRepository;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.service.UserService;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StudyRoomReservationValidator {
    private final StudyRoomParticipantRepository participantRepository;
    private final StudyRoomReservationRepository reservationRepository;
    private final StudyRoomService studyRoomService;
    private final UserService userService;

    public User validateReservation(String identifier, LocalDate date, LocalTime startTime, LocalTime endTime, Long studyRoomId, int participantCnt) {
        User user = validateDuplicatedReservationOrParticipation(identifier, date);
        validateReservationDuration(startTime, endTime);
        validateReservationTimeRange(date, startTime, endTime);
        validateMinParticipants(studyRoomId, participantCnt);
        checkTimeConflict(studyRoomId, date, startTime, endTime);
        return user;
    }

    public void validateReservationDuration(LocalTime startTime, LocalTime endTime){
        long minutes = Duration.between(startTime, endTime).toMinutes();
        if(minutes != 60 && minutes != 120) {
            throw new CustomException(ErrorCode.INVALID_RESERVATION_DURATION);
        }
    }

    public void validateReservationTimeRange(LocalDate date, LocalTime startTime, LocalTime endTime){
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if(dayOfWeek == DayOfWeek.SUNDAY){
            throw new CustomException(ErrorCode.INVALID_RESERVATION_TIME, "일요일에는 예약이 불가능합니다.");
        }

        LocalTime openTime = LocalTime.of(10, 0);
        LocalTime closeTime = (dayOfWeek == DayOfWeek.SATURDAY) ? LocalTime.of(16, 0) : LocalTime.of(21, 0);

        if(startTime.isBefore(openTime) || endTime.isAfter(closeTime)){
            throw new CustomException(ErrorCode.INVALID_RESERVATION_TIME);
        }
    }

    public void checkTimeConflict(Long studyRoomId, LocalDate date, LocalTime startTime, LocalTime endTime){
        List<StudyRoomReservation> existedReservations =  reservationRepository.findConflictReservations(studyRoomId, date, startTime, endTime, ReservationStatus.CONFIRMED);
        if(!existedReservations.isEmpty()){
            throw new CustomException(ErrorCode.DUPLICATE_RESERVATION);
        }
    }

    public void validateMinParticipants(Long studyRoomId, int participantCnt){
        StudyRoom studyRoom = studyRoomService.findStudyRoomById(studyRoomId);
        if (participantCnt < studyRoom.getMinCapacity() - 1){
            throw new CustomException(ErrorCode.INVALID_STUDY_ROOM_PARTICIPANTS,
                    String.format("최소 동반 이용자는 %d명입니다.", studyRoom.getMinCapacity() - 1));
        }
    }

    public User validateDuplicatedReservationOrParticipation(String identifier, LocalDate date){
        User user = userService.findByIdentifier(identifier);
        boolean hasReservation = reservationRepository.existsByUserAndDateAndStatusNot(user, date, ReservationStatus.CANCELED);
        boolean hasParticipation = participantRepository.existsByIdentifierAndDate(user.getIdentifier(), date);

        if(hasReservation){
            throw new CustomException(ErrorCode.ALREADY_RESERVED_ON_DATE);
        }
        if(hasParticipation){
            throw new CustomException(ErrorCode.ALREADY_RESERVED_ON_DATE, "이미 다른 스터디룸에 동반이용자로 등록되어 있습니다. 같은 날짜에 추가 예약이 불가능합니다.");
        }
        return user;
    }
}
