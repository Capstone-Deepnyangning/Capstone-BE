package com.deepnyangning.capstonebe.domain.face.repository;

import com.deepnyangning.capstonebe.domain.face.entity.FaceIssueReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaceIssueReportRepository extends JpaRepository<FaceIssueReport, Long> {
}
