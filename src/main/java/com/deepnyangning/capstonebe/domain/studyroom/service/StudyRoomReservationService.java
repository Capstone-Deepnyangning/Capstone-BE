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

    @Transactional
    public ReservationResponse saveReservation(ReservationRequest reservationRequest){
        StudyRoomReservation reservation = reservationMapper.toEntity(reservationRequest);
        reservation.setStudyRoom(studyRoomService.findStudyRoomById(reservationRequest.getStudyRoomId()));
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
