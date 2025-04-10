package com.deepnyangning.capstonebe.domain.studyroom.service;

import com.deepnyangning.capstonebe.domain.studyroom.dto.StudyRoomResponse;
import com.deepnyangning.capstonebe.domain.studyroom.mapper.StudyRoomMapper;
import com.deepnyangning.capstonebe.domain.studyroom.repository.StudyRoomRepository;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyRoomService {
    private final StudyRoomRepository studyRoomRepository;
    private final StudyRoomMapper studyRoomMapper;

    public StudyRoomResponse findStudyRoom(Long id){
        return studyRoomMapper.toResponseDto(studyRoomRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_ROOM_NOT_FOUND)));
    }
}
