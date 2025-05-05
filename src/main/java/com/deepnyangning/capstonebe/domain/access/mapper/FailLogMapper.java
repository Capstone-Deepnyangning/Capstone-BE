package com.deepnyangning.capstonebe.domain.access.mapper;

import com.deepnyangning.capstonebe.domain.access.dto.FailLogResponse;
import com.deepnyangning.capstonebe.domain.access.entity.FailLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FailLogMapper {
    FailLogResponse toResponseDto(FailLog failLog);
}
