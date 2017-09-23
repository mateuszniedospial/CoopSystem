package com.coopsystem.repository;

import com.coopsystem.domain.WorkLog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkLog entity.
 */
@SuppressWarnings("unused")
public interface WorkLogRepository extends JpaRepository<WorkLog,Long> {

    List<WorkLog> findWorkLogByTaskId(Long taskId);

    List<WorkLog> findWorkLogByJuserId(Long jUserId);
}
