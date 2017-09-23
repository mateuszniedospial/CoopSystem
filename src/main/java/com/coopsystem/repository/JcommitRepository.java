package com.coopsystem.repository;

import com.coopsystem.domain.Jcommit;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the Jcommit entity.
 */
@SuppressWarnings("unused")
public interface JcommitRepository extends JpaRepository<Jcommit,Long> {

    @Query("select distinct jcommit from Jcommit jcommit left join fetch jcommit.tasks")
    List<Jcommit> findAllWithEagerRelationships();

    @Query("select jcommit from Jcommit jcommit left join fetch jcommit.tasks where jcommit.id =:id")
    Jcommit findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select jcommit from Jcommit jcommit left join fetch jcommit.tasks tasks where tasks.id in(:taskId) ")
    List<Jcommit> findByTasksOrderByCommitDateDesc(@Param("taskId") Long id);

    @Query("select jcommit from Jcommit jcommit left join fetch jcommit.tasks tasks where tasks.id in(:taskId) order by jcommit.commitDate desc")
    List<Jcommit> findByTasks(@Param("taskId") Long id);

}
