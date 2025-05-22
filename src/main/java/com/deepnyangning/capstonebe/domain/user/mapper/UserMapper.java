package com.deepnyangning.capstonebe.domain.user.mapper;

import com.deepnyangning.capstonebe.domain.access.dto.AccessLogUserInfo;
import com.deepnyangning.capstonebe.domain.notification.entity.FcmToken;
import com.deepnyangning.capstonebe.domain.studyroom.dto.ReservationUserInfo;
import com.deepnyangning.capstonebe.domain.user.dto.UserResponse;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "fcmTokens", expression = "java(mapFcmTokens(user.getFcmTokens()))")
    UserResponse toResponseDto(User user);

    AccessLogUserInfo toAccessLogUserInfo(User user);

    ReservationUserInfo toReservationUserInfo(User user);

    default List<String> mapFcmTokens(List<FcmToken> tokens){
        return tokens.stream().map(FcmToken::getToken).toList();
    }
}
