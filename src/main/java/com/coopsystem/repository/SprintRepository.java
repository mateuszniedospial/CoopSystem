package com.coopsystem.repository;

import com.coopsystem.domain.Sprint;

import com.coopsystem.domain.enumeration.SprintLifeCycle;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Sprint entity.
 */
@SuppressWarnings("unused")
public interface SprintRepository extends JpaRepository<Sprint,Long> {

    @Transactional
    @Query("Select distinct sprint from Sprint sprint where sprint.jgroup = " +
        "(select jgroup from JGroup jgroup where jgroup.id =:id) and sprint.lifeCycle =:lifeCycle " +
        "order by sprint.createdDate desc")
    List<Sprint> findSprintsByJGroupIdAndLifeCycleOrderByCreatedDateDesc(@Param("id")Long id, @Param("lifeCycle")SprintLifeCycle lifeCycle);

    @Transactional
    List<Sprint> findByStopTimeBeforeAndLifeCycle(@Param("stopTime") ZonedDateTime stopTime, @Param("lifeCycle")SprintLifeCycle lifeCycle);

    @Transactional
    List<Sprint> findByLifeCycle(@Param("lifeCycle")SprintLifeCycle lifeCycle);

}
