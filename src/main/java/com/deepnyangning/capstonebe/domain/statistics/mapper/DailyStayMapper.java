package com.deepnyangning.capstonebe.domain.statistics.mapper;

import com.deepnyangning.capstonebe.domain.statistics.dto.DailyStayResponse;
import com.deepnyangning.capstonebe.domain.statistics.entity.DailyStay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DailyStayMapper {
    @Mapping(source = "totalStay", target = "stayMinutes")
    DailyStayResponse toResponseDto(DailyStay dailyStay);
}
