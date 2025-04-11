package com.deepnyangning.capstonebe.domain.studyroom.mapper;

import com.deepnyangning.capstonebe.domain.studyroom.dto.ReservationRequest;
import com.deepnyangning.capstonebe.domain.studyroom.dto.ReservationResponse;
import com.deepnyangning.capstonebe.domain.studyroom.dto.ReservationUpdate;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomReservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {StudyRoomMapper.class, StudyRoomParticipantMapper.class})
public interface StudyRoomReservationMapper {
    @Mapping(target = "studyRoom", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "participants", ignore = true)
    StudyRoomReservation toEntity(ReservationRequest reservationRequest);

    @Mapping(source = "studyRoom", target = "studyRoom")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "participants", target = "participants")
    ReservationResponse toResponseDto(StudyRoomReservation studyRoomReservation);

    @Mapping(target = "studyRoom", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "participants", ignore = true)
    void update(@MappingTarget StudyRoomReservation reservation, ReservationUpdate update);
}
