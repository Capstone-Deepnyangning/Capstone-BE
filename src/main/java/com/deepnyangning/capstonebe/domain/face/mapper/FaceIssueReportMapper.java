package com.deepnyangning.capstonebe.domain.face.mapper;

import com.deepnyangning.capstonebe.domain.face.dto.FaceIssuePreviewResponse;
import com.deepnyangning.capstonebe.domain.face.dto.FaceIssueReportResponse;
import com.deepnyangning.capstonebe.domain.face.entity.FaceIssueReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FaceIssueReportMapper {
    @Mapping(source = "user.identifier", target = "identifier")
    @Mapping(source = "user.name", target = "name")
    FaceIssueReportResponse toResponseDto(FaceIssueReport faceIssueReport);

    @Mapping(source = "user.identifier", target = "identifier")
    FaceIssuePreviewResponse toPreviewDto(FaceIssueReport faceIssueReport);
}
