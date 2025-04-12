package com.deepnyangning.capstonebe.domain.studyroom.service;

import com.deepnyangning.capstonebe.domain.studyroom.dto.ReservationRequest;
import com.deepnyangning.capstonebe.domain.studyroom.dto.ReservationResponse;
import com.deepnyangning.capstonebe.domain.studyroom.dto.ReservationUpdate;
import com.deepnyangning.capstonebe.domain.studyroom.entity.ReservationStatus;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomParticipant;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomReservation;
import com.deepnyangning.capstonebe.domain.studyroom.mapper.StudyRoomReservationMapper;
import com.deepnyangning.capstonebe.domain.studyroom.repository.StudyRoomReservationRepository;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.service.UserService;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyRoomReservationService {
    private final StudyRoomReservationRepository reservationRepository;
    private final StudyRoomReservationMapper reservationMapper;
    private final StudyRoomService studyRoomService;
    private final UserService userService;
    private final StudyRoomParticipantService participantService;

    public void validateReservationDuration(LocalTime startTime, LocalTime endTime){
        long minutes = Duration.between(startTime, endTime).toMinutes();
        if(minutes != 60 && minutes != 120) {
            throw new CustomException(ErrorCode.INVALID_RESERVATION_DURATION);
        }
    }

    public void validateReservationTimeRange(LocalDate date, LocalTime startTime, LocalTime endTime){
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        LocalTime openTime = LocalTime.of(10, 0);
        LocalTime closeTime = (dayOfWeek == DayOfWeek.SATURDAY) ? LocalTime.of(16, 0) : LocalTime.of(21, 0);

        if(startTime.isBefore(openTime) || endTime.isAfter(closeTime)){
            throw new CustomException(ErrorCode.INVALID_RESERVATION_TIME);
        }
    }

    public void checkTimeConflict(Long studyRoomId, LocalDate date, LocalTime startTime, LocalTime endTime){
        boolean exists = reservationRepository.existsByStudyRoomIdAndDateAndTime(studyRoomId, date, startTime, endTime, ReservationStatus.CONFIRMED);
        if(exists){
            throw new CustomException(ErrorCode.DUPLICATE_RESERVATION);
        }
    }

    @Transactional
    public ReservationResponse saveReservation(ReservationRequest reservationRequest){
        LocalDate date = reservationRequest.getDate();
        LocalTime startTime = reservationRequest.getStartTime();
        LocalTime endTime = reservationRequest.getEndTime();
        Long studyRoomId = reservationRequest.getStudyRoomId();

        validateReservationDuration(startTime, endTime);
        validateReservationTimeRange(date, startTime, endTime);
        checkTimeConflict(studyRoomId, date, startTime, endTime);

        StudyRoomReservation reservation = reservationMapper.toEntity(reservationRequest);
        reservation.setStudyRoom(studyRoomService.findStudyRoomById(studyRoomId));
        reservation.setUser(userService.findById(reservationRequest.getUserId()));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        participantService.saveParticipants(reservationRequest.getParticipants(), reservation);
        reservationRepository.save(reservation);
        return reservationMapper.toResponseDto(reservation);
    }

    public Page<ReservationResponse> findReservationsByStudyRoomName(String name, Pageable pageable){
        Page<StudyRoomReservation> reservations;
        if (name == null || name.trim().isEmpty()) {
            reservations = reservationRepository.findAll(pageable);
        } else{
            reservations = reservationRepository.findStudyRoomReservationsByStudyRoomName(name.toUpperCase().replace(" ", ""), pageable);
        }
        return reservations.map(reservationMapper::toResponseDto);
    }

    public Page<ReservationResponse> findReservationsByUser(Long userId, Pageable pageable){
        User user = userService.findById(userId);
        return reservationRepository.findStudyRoomReservationsByUser(user, pageable)
                .map(reservationMapper::toResponseDto);
    }

    public ReservationResponse findReservationById(Long id){
        return reservationMapper.toResponseDto(reservationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND)));
    }

    @Transactional
    public ReservationResponse updateReservation(Long id, ReservationUpdate reservationUpdate){
        StudyRoomReservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
        reservationMapper.update(reservation, reservationUpdate);
        participantService.updateParticipants(reservationUpdate.getParticipants(), reservation);
        reservationRepository.save(reservation);
        return reservationMapper.toResponseDto(reservation);
    }

    @Transactional
    public ReservationResponse updateReservationStatus(Long id, String status){
        StudyRoomReservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
        reservation.setStatus(ReservationStatus.valueOf(status));
        return reservationMapper.toResponseDto(reservation);
    }

    @Transactional
    public void cancelReservation(Long id){
        updateReservationStatus(id, "CANCELED");
    }

    @Scheduled(cron = "0 0 * * * *")
    public void updateCompletedReservations(){
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<StudyRoomReservation> reservations = reservationRepository.findByDateAndEndTimeLessThanEqualAndStatus(today, now, ReservationStatus.CONFIRMED);

        for(StudyRoomReservation reservation:reservations){
            reservation.setStatus(ReservationStatus.COMPLETED);
        }

        reservationRepository.saveAll(reservations);
    }
}
