package com.deepnyangning.capstonebe.domain.face.mapper;

import com.deepnyangning.capstonebe.domain.face.dto.FaceIssueReportResponse;
import com.deepnyangning.capstonebe.domain.face.entity.FaceIssueReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FaceIssueReportMapper {
    @Mapping(source = "user.id", target = "userId")
    FaceIssueReportResponse toResponseDto(FaceIssueReport faceIssueReport);
}
