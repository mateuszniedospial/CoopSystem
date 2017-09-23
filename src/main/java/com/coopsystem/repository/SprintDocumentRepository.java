package com.coopsystem.repository;

import com.coopsystem.domain.SprintDocument;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the SprintDocument entity.
 */
@SuppressWarnings("unused")
public interface SprintDocumentRepository extends JpaRepository<SprintDocument,Long> {

}
