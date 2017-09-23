package com.coopsystem.repository;

import com.coopsystem.domain.Project;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the Project entity.
 */
@SuppressWarnings("unused")
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Transactional
    @Query("select distinct project from Project project left join fetch project.jgroups")
    List<Project> findAllWithEagerRelationships();

    @Transactional
    @Query("select project from Project project left join fetch project.jgroups where project.id =:id")
    Project findOneWithEagerRelationships(@Param("id") Long id);

    @Transactional
    @Query("select distinct project from Project project left join fetch project.jgroups jgroups where (select jUser from JUser jUser where jUser.id =:id) member of jgroups.jusers")
    List<Project> findAllJUserProjects(@Param("id") Long id);

    @Transactional
    @Query("select distinct project from Project project left join fetch project.jgroups jgroups where jgroups.id = :id")
    List<Project> findProjectByJGroupId(@Param("id") Long id);

}
