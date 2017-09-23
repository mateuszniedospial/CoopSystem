package com.coopsystem.repository;

import com.coopsystem.domain.TaskDescription;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Spring Data JPA repository for the TaskDescription entity.
 */
@SuppressWarnings("unused")
public interface TaskDescriptionRepository extends JpaRepository<TaskDescription,Long> {


    @Transactional
    @Query("select taskDescription from TaskDescription taskDescription join fetch taskDescription.task inner join taskDescription.task task where task.id =:id")
    TaskDescription findTaskDescriptionOfTask(@Param("id") Long id);


    @org.springframework.transaction.annotation.Transactional
    TaskDescription findDescriptionByTaskId(Long taskId);
}
