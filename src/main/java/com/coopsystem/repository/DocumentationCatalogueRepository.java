package com.coopsystem.repository;

import com.coopsystem.domain.DocumentationCatalogue;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the DocumentationCatalogue entity.
 */
@SuppressWarnings("unused")
public interface DocumentationCatalogueRepository extends JpaRepository<DocumentationCatalogue,Long> {

    @Transactional
    @Query("select distinct documentationCatalogue from DocumentationCatalogue documentationCatalogue left join fetch documentationCatalogue.project inner join documentationCatalogue.project project where project.id in (:id)")
    List<DocumentationCatalogue> findAllProjectCatalogue(@Param("id") Long id);

    @Query("select distinct documentationCatalogue from DocumentationCatalogue documentationCatalogue where documentationCatalogue.label in (:label)")
    DocumentationCatalogue findByLabel(@Param("label") String label);

    @Query("select distinct documentationCatalogue from DocumentationCatalogue documentationCatalogue where documentationCatalogue.parent.id is null")
    DocumentationCatalogue byParent();

    @Query("select distinct documentationCatalogue from DocumentationCatalogue documentationCatalogue where documentationCatalogue.parent.id in (:id)")
    List<DocumentationCatalogue> findNestedFolders(@Param("id") Long id);

}
