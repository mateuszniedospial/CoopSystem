package com.coopsystem.repository;

import com.coopsystem.domain.ProjectDocumentation;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Spring Data JPA repository for the ProjectDocumentation entity.
 */
@SuppressWarnings("unused")
public interface ProjectDocumentationRepository extends JpaRepository<ProjectDocumentation,Long> {

    @Transactional
    @Query("select distinct projectDoc from ProjectDocumentation projectDoc left join fetch projectDoc.project inner join projectDoc.project project where project.id in (:id)")
    List<ProjectDocumentation> findAllProjectDocumentations(@Param("id") Long id);

    @Transactional
    @Query("select distinct projectDoc from ProjectDocumentation projectDoc where projectDoc.underCatalogue.id in (:id)")
    List<ProjectDocumentation> findAllByCatalogue(@Param("id") Long id);

    @Transactional
    @Query("select distinct projectDocumentation from ProjectDocumentation projectDocumentation where projectDocumentation.label in (:label)")
    ProjectDocumentation findByLabel(@Param("label") String label);
}
