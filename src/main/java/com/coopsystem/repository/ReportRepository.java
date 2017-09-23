package com.coopsystem.repository;

import com.coopsystem.domain.Report;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data JPA repository for the Report entity.
 */
@SuppressWarnings("unused")
public interface ReportRepository extends JpaRepository<Report,Long> {

    @Transactional
    Report findOneBySprintId(Long sprintId);
}
