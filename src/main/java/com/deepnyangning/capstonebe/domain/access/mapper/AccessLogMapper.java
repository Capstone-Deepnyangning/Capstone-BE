package com.deepnyangning.capstonebe.domain.access.mapper;

import com.deepnyangning.capstonebe.domain.access.dto.AccessLogResponse;
import com.deepnyangning.capstonebe.domain.access.entity.AccessLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccessLogMapper {
    @Mapping(target = "userInfo", ignore = true)
    AccessLogResponse toResponseDto(AccessLog accessLog);
}
