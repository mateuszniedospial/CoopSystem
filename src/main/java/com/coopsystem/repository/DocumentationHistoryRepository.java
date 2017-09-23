package com.coopsystem.repository;

import com.coopsystem.domain.DocumentationHistory;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Spring Data JPA repository for the DocumentationHistory entity.
 */
@SuppressWarnings("unused")
public interface DocumentationHistoryRepository extends JpaRepository<DocumentationHistory,Long> {

    @Transactional
    @Query("select distinct docHistory from DocumentationHistory docHistory where docHistory.projectDocumentation.id in (:id)")
    List<DocumentationHistory> findAllVersionsOfDocument(@Param("id") Long id);
}
