package com.coopsystem.repository;

import com.coopsystem.core.sql.postgres.NativeSQL;
import com.coopsystem.domain.Task;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Transactional
    @Query("select tasks from Task tasks join fetch tasks.project inner join tasks.project proj where proj.id in (:id)")
    List<Task> findAllProjectTasks(@Param("id") Long id);

    @Transactional
    @Query("select distinct tasks from Task tasks join fetch tasks.jgroup inner join tasks.jgroup jgroup " +
        "left join fetch tasks.watchers left join fetch tasks.historic_sprints where jgroup.id in (:id) ")
    List<Task> findAllJGroupTasksWithEagerRelationships(@Param("id") Long id);

    @Transactional
    @Query("select distinct tasks from Task tasks join fetch tasks.jgroup inner join tasks.jgroup jgroup where jgroup.id in (:id) order by tasks.createdDate DESC")
    List<Task> findAllJGroupTasks(@Param("id") Long id);

    @Transactional
    @Query("select distinct tasks from Task tasks join fetch tasks.assignee inner join tasks.assignee assignee where assignee.id in (:id)")
    List<Task> findAllTasksAssignedToUser(@Param("id") Long id);

    @Transactional
    @Query("select distinct task from Task task left join fetch task.watchers left join fetch task.historic_sprints")
    List<Task> findAllWithEagerRelationships();

    @Transactional
    @Query("select task from Task task left join fetch task.watchers left join fetch task.historic_sprints where task.id =:id")
    Task findOneWithEagerRelationships(@Param("id") Long id);

    @Transactional
    List<Task> findTasksByParentId(Long id);

    @Transactional
    List<Task> findTasksBySprintId(Long id);

    @Transactional
    @Query(value = NativeSQL.FIND_TASK_BY_MANY_PARAMETER + "order by id desc", nativeQuery = true)
    List<Task> search(@Param("jGroupId") Long jGroupJd,
                      @Param("status") String status, @Param("title") String title,
                      @Param("env") String env, @Param("version") String version,
                      @Param("projectId") String projectId,
                      @Param("priority") String priority, @Param("createdDateBefore") ZonedDateTime createdDateBefore,
                      @Param("createdDateAfter") ZonedDateTime createdDateAfter,
                      @Param("modifyDateBefore") ZonedDateTime modifyDateBefore,
                      @Param("modifyDateAfter") ZonedDateTime modifyDateAfter,
                      @Param("description") String description,
                      @Param("comment") String comment);

    @Transactional
    @Query(value = NativeSQL.FIND_TASK_BY_MANY_PARAMETER_WITH_ASSIGNEE  + "order by id desc", nativeQuery = true)
    List<Task> searchWithAssignee(@Param("jGroupId") Long jGroupJd,
                                  @Param("status") String status, @Param("title") String title,
                                  @Param("env") String env, @Param("version") String version,
                                  @Param("projectId") String projectId,
                                  @Param("priority") String priority, @Param("assigneeId") Long assigneeId,
                                  @Param("createdDateBefore") ZonedDateTime createdDateBefore,
                                  @Param("createdDateAfter") ZonedDateTime createdDateAfter,
                                  @Param("modifyDateBefore") ZonedDateTime modifyDateBefore,
                                  @Param("modifyDateAfter") ZonedDateTime modifyDateAfter,
                                  @Param("description") String description,
                                  @Param("comment") String comment);

}
