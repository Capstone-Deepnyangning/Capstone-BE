package com.deepnyangning.capstonebe.domain.studyroom.service;

import com.deepnyangning.capstonebe.domain.studyroom.dto.*;
import com.deepnyangning.capstonebe.domain.studyroom.entity.ReservationStatus;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoom;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomReservation;
import com.deepnyangning.capstonebe.domain.studyroom.mapper.StudyRoomReservationMapper;
import com.deepnyangning.capstonebe.domain.studyroom.repository.StudyRoomReservationRepository;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.service.UserService;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudyRoomReservationService {
    private final StudyRoomReservationRepository reservationRepository;
    private final StudyRoomReservationMapper reservationMapper;
    private final StudyRoomService studyRoomService;
    private final StudyRoomParticipantService participantService;
    private final StudyRoomReservationValidator validator;
    private final UserService userService;

    @Transactional
    public ReservationResponse saveReservation(String identifier, ReservationRequest reservationRequest){
        LocalDate date = reservationRequest.getDate();
        LocalTime startTime = reservationRequest.getStartTime();
        LocalTime endTime = reservationRequest.getEndTime();
        Long studyRoomId = reservationRequest.getStudyRoomId();
        int participantCnt = reservationRequest.getParticipants().size();

        User user = validator.validateReservation(identifier, date, startTime, endTime, studyRoomId, participantCnt);

        StudyRoomReservation reservation = reservationMapper.toEntity(reservationRequest);
        reservation.setStudyRoom(studyRoomService.findStudyRoomById(studyRoomId));
        reservation.setUser(user);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        participantService.saveParticipants(reservationRequest.getParticipants(), reservation);
        reservationRepository.save(reservation);
        return reservationMapper.toResponseDto(reservation);
    }

    public List<AdminReservationResponse> findReservationsByStudyRoomName(String name, LocalDate cursorDate, LocalTime cursorStartTime, int size) {
        String formattedName = (name == null || name.isBlank()) ? null : name.toUpperCase().replace(" ", "");
        List<StudyRoomReservation> reservations = reservationRepository.findByStudyRoomName(formattedName, cursorDate, cursorStartTime, size+1);
        return reservations.stream().map(reservationMapper::toAdminResponse).toList();
    }

    public List<ReservationResponse> findReservationsByUser(String identifier, LocalDate cursorDate, int size){
        Long userId = userService.findByIdentifier(identifier).getId();
        return reservationRepository.findByUser(userId, cursorDate, size+1)
                .stream().map(reservationMapper::toResponseDto).toList();
    }

    public ReservationResponse findReservationById(Long id){
        return reservationMapper.toResponseDto(reservationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND)));
    }

    public List<AvailableTimeOption> findAvailableTimes(Long studyRoomId, LocalDate date){
        StudyRoom studyRoom = studyRoomService.findStudyRoomById(studyRoomId);
        List<StudyRoomReservation> reservations = reservationRepository.findByStudyRoomAndDateAndStatusNot(studyRoom, date, ReservationStatus.CANCELED);

        int endHour = (date.getDayOfWeek() == DayOfWeek.SATURDAY) ? 16 : 21;
        LocalTime START = LocalTime.of(10, 0);
        LocalTime END = LocalTime.of(endHour, 0);

        Set<LocalTime> reservedSlots = new HashSet<>();
        for(StudyRoomReservation res : reservations){
            LocalTime cur = res.getStartTime();
            while(cur.isBefore(res.getEndTime())){
                reservedSlots.add(cur);
                cur = cur.plusHours(1);
            }
        }

        List<AvailableTimeOption> result = new ArrayList<>();
        for(LocalTime start = START; start.isBefore(END); start = start.plusHours(1)){
            if(reservedSlots.contains(start)) continue;

            List<LocalTime> ends = new ArrayList<>();
            LocalTime end1 = start.plusHours(1);
            LocalTime end2 = start.plusHours(2);

            // 1시간 예약 가능 여부
            if(end1.isBefore(END.plusSeconds(1))){
                ends.add(end1);
            }

            // 2시간 예약 가능 여부
            if(!reservedSlots.contains(start.plusHours(1)) && end2.isBefore(END.plusSeconds(1))){
                ends.add(end2);
            }

            if(!ends.isEmpty()){
                result.add(new AvailableTimeOption(start, ends));
            }
        }
        return result;
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
    public AdminReservationResponse updateReservationStatus(Long id, String status){
        StudyRoomReservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
        reservation.setStatus(ReservationStatus.valueOf(status));
        return reservationMapper.toAdminResponse(reservation);
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
