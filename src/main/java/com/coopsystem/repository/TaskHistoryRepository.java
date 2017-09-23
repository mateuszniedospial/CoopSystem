package com.coopsystem.repository;

import com.coopsystem.domain.TaskHistory;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the TaskHistory entity.
 */
@SuppressWarnings("unused")
public interface TaskHistoryRepository extends JpaRepository<TaskHistory,Long> {
    @Transactional
    List<TaskHistory> findByTaskId(Long taskId);

    @Transactional
    @Query("select taskHistory from TaskHistory taskHistory join fetch taskHistory.modifiedJUser inner join taskHistory.modifiedJUser userWhoModified where userWhoModified.id =:id order by taskHistory.created_date DESC")
    List<TaskHistory> queryHistoriesOfTasksModifiedByUser(@Param("id") Long id);

    @Transactional
    @Query("select taskHistories from TaskHistory taskHistories join fetch taskHistories.task inner join taskHistories.task innerTask where innerTask.jgroup.id = :id order by taskHistories.created_date DESC")
    List<TaskHistory> queryHistoriesOfJGroupTasks(@Param("id") Long id);
}
