package com.deepnyangning.capstonebe.domain.studyroom.service;

import com.deepnyangning.capstonebe.domain.studyroom.dto.StudyRoomResponse;
import com.deepnyangning.capstonebe.domain.studyroom.entity.ReservationStatus;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoom;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomReservation;
import com.deepnyangning.capstonebe.domain.studyroom.mapper.StudyRoomMapper;
import com.deepnyangning.capstonebe.domain.studyroom.repository.StudyRoomRepository;
import com.deepnyangning.capstonebe.domain.studyroom.repository.StudyRoomReservationRepository;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StudyRoomService {
    private final StudyRoomRepository studyRoomRepository;
    private final StudyRoomMapper studyRoomMapper;
    private final StudyRoomReservationRepository reservationRepository;

    public StudyRoom findStudyRoomById(Long id){
        return studyRoomRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_ROOM_NOT_FOUND));
    }

    public List<StudyRoomResponse> findAvailableStudyRooms(LocalDate date, LocalTime startTime, LocalTime endTime, String cursorName, int size){
        if(date.getDayOfWeek() == DayOfWeek.SUNDAY) return List.of();

        List<StudyRoom> studyRooms;
        if (date != null && startTime == null && endTime == null){
            int availableMinutes;
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if(dayOfWeek == DayOfWeek.SATURDAY){
                availableMinutes = 360;
            }
            else{
                availableMinutes = 660;
            }
            studyRooms =  studyRoomRepository.findAvailableStudyRoomsByDate(date, availableMinutes, cursorName, size+1);
        }
        else {
            studyRooms = studyRoomRepository.findAvailableStudyRooms(date, startTime, endTime, cursorName, size+1);
        }

        List<StudyRoomReservation> allReservations = reservationRepository.findByDateAndStatusNot(date, ReservationStatus.CANCELED);
        Map<Long, List<StudyRoomReservation>> reservationsById = allReservations.stream()
                .collect(Collectors.groupingBy(res -> res.getStudyRoom().getId()));

        return studyRooms.stream().map(studyRoom -> {
            StudyRoomResponse response = studyRoomMapper.toResponseDto(studyRoom);
            List<StudyRoomReservation> reservations = reservationsById.getOrDefault(studyRoom.getId(), List.of());
            response.setReservedTimes(findReservedTimes(reservations, date));
            return response;
        }).toList();
    }

    public Map<Integer, Boolean> findReservedTimes(List<StudyRoomReservation> reservations, LocalDate date){
        Map<Integer, Boolean> reservedTimes = new LinkedHashMap<>();
        int endHour = (date.getDayOfWeek() == DayOfWeek.SATURDAY) ? 16 : 20;

        for(int hour = 10; hour <= endHour; hour++){
            LocalTime hourStart = LocalTime.of(hour, 0);
            LocalTime hourEnd = hourStart.plusHours(1);

            boolean isReserved = reservations.stream().anyMatch(res ->
                        res.getStartTime().isBefore(hourEnd) && res.getEndTime().isAfter(hourStart)
                    );
            reservedTimes.put(hour, isReserved);
        }
        return reservedTimes;
    }
}
