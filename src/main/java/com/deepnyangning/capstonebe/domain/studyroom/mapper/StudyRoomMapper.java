package com.deepnyangning.capstonebe.domain.studyroom.mapper;

import com.deepnyangning.capstonebe.domain.studyroom.dto.StudyRoomResponse;
import com.deepnyangning.capstonebe.domain.studyroom.dto.StudyRoomSimpleResponse;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudyRoomMapper {
    StudyRoomResponse toResponseDto(StudyRoom studyRoom);
    StudyRoomSimpleResponse toSimpleDto(StudyRoom studyRoom);
}
