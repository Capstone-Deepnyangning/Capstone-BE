package com.deepnyangning.capstonebe.domain.user.mapper;

import com.deepnyangning.capstonebe.domain.access.dto.AccessLogUserInfo;
import com.deepnyangning.capstonebe.domain.studyroom.dto.ReservationUserInfo;
import com.deepnyangning.capstonebe.domain.user.dto.UserResponse;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponseDto(User user);

    AccessLogUserInfo toAccessLogUserInfo(User user);

    ReservationUserInfo toReservationUserInfo(User user);
}
