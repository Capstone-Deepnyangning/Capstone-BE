package com.deepnyangning.capstonebe.domain.studyroom.mapper;

import com.deepnyangning.capstonebe.domain.studyroom.dto.ParticipantRequest;
import com.deepnyangning.capstonebe.domain.studyroom.dto.ParticipantResponse;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomParticipant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudyRoomParticipantMapper {
    @Mapping(target = "reservation", ignore = true)
    StudyRoomParticipant toEntity(ParticipantRequest participantRequest);

    ParticipantResponse toResponseDto(StudyRoomParticipant studyRoomParticipant);
}
