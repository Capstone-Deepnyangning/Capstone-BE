package com.deepnyangning.capstonebe.domain.access.mapper;

import com.deepnyangning.capstonebe.domain.access.dto.AccessLogPreviewResponse;
import com.deepnyangning.capstonebe.domain.access.dto.AccessLogResponse;
import com.deepnyangning.capstonebe.domain.access.entity.AccessLog;
import com.deepnyangning.capstonebe.domain.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AccessLogMapper {
    @Mapping(source = "user", target = "userInfo")
    AccessLogResponse toResponseDto(AccessLog accessLog);

    @Mapping(source = "user.identifier", target = "identifier")
    AccessLogPreviewResponse toPreviewDto(AccessLog accessLog);
}
