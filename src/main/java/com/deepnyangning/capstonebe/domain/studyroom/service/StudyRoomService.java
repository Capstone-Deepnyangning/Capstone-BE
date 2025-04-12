package com.deepnyangning.capstonebe.domain.studyroom.service;

import com.deepnyangning.capstonebe.domain.studyroom.dto.StudyRoomResponse;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoom;
import com.deepnyangning.capstonebe.domain.studyroom.mapper.StudyRoomMapper;
import com.deepnyangning.capstonebe.domain.studyroom.repository.StudyRoomRepository;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;


@Service
@RequiredArgsConstructor
public class StudyRoomService {
    private final StudyRoomRepository studyRoomRepository;
    private final StudyRoomMapper studyRoomMapper;

    public StudyRoom findStudyRoomById(Long id){
        return studyRoomRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_ROOM_NOT_FOUND));
    }

    public Page<StudyRoomResponse> findAvailableStudyRooms(LocalDate date, LocalTime startTime, LocalTime endTime, Pageable pageable){
        if (date != null && startTime == null && endTime == null){
            int availableMinutes;
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if(dayOfWeek == DayOfWeek.SATURDAY){
                availableMinutes = 360;
            }
            else{
                availableMinutes = 660;
            }
            return studyRoomRepository.findAvailableStudyRoomsByDate(date, availableMinutes, pageable)
                    .map(studyRoomMapper::toResponseDto);
        }
        return studyRoomRepository.findAvailableStudyRooms(date, startTime, endTime, pageable)
                .map(studyRoomMapper::toResponseDto);
    }
}
