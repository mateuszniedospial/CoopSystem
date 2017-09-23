package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.TaskHistory;

import com.coopsystem.repository.TaskHistoryRepository;
import com.coopsystem.service.TaskHistoryService;
import com.coopsystem.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TaskHistory.
 */
@RestController
@RequestMapping("/api")
public class TaskHistoryResource {

    private final Logger log = LoggerFactory.getLogger(TaskHistoryResource.class);

    private static final String ENTITY_NAME = "taskHistory";

    private final TaskHistoryRepository taskHistoryRepository;

    private final TaskHistoryService taskHistoryService;


    public TaskHistoryResource(TaskHistoryRepository taskHistoryRepository, TaskHistoryService taskHistoryService) {
        this.taskHistoryRepository = taskHistoryRepository;
        this.taskHistoryService = taskHistoryService;
    }

    /**
     * POST  /task-histories : Create a new taskHistory.
     *
     * @param taskHistory the taskHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskHistory, or with status 400 (Bad Request) if the taskHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/task-histories")
    @Timed
    public ResponseEntity<TaskHistory> createTaskHistory(@RequestBody TaskHistory taskHistory) throws URISyntaxException {
        log.debug("REST request to save TaskHistory : {}", taskHistory);
        if (taskHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new taskHistory cannot already have an ID")).body(null);
        }
        TaskHistory result = taskHistoryRepository.save(taskHistory);
        taskHistoryService.sendTaskUpdatedEmail(taskHistory);

        return ResponseEntity.created(new URI("/api/task-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /task-histories : Updates an existing taskHistory.
     *
     * @param taskHistory the taskHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskHistory,
     * or with status 400 (Bad Request) if the taskHistory is not valid,
     * or with status 500 (Internal Server Error) if the taskHistory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/task-histories")
    @Timed
    public ResponseEntity<TaskHistory> updateTaskHistory(@RequestBody TaskHistory taskHistory) throws URISyntaxException {
        log.debug("REST request to update TaskHistory : {}", taskHistory);
        if (taskHistory.getId() == null) {
            return createTaskHistory(taskHistory);
        }
        TaskHistory result = taskHistoryRepository.save(taskHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taskHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /task-histories : get all the taskHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of taskHistories in body
     */
    @GetMapping("/task-histories")
    @Timed
    public List<TaskHistory> getAllTaskHistories() {
        log.debug("REST request to get all TaskHistories");
        List<TaskHistory> taskHistories = taskHistoryRepository.findAll();
        return taskHistories;
    }

    @GetMapping("/task-histories/byTaskId")
    @Timed
    public List<TaskHistory> getTaskHistoriesByTaskId(@RequestParam(value = "query", required = false)  Long taskId) {
        log.debug("REST request to get TaskHistories by taskId");
        List<TaskHistory> taskHistories = taskHistoryRepository.findByTaskId(taskId);
        taskHistories.forEach((it)->{
            it.setTask(null);
        });
        return taskHistories;
    }

    @GetMapping("/task-histories/j-users/{id}")
    @Timed
    public List<TaskHistory> getHistoriesOfTasksModifiedByJUser(@PathVariable Long id) {
        log.debug("REST request to get all histories of tasks modified by specific user");
        log.debug("REST request to get all histories of tasks modified by specific user of id: {}", id);
        List<TaskHistory> taskHistories = taskHistoryRepository.queryHistoriesOfTasksModifiedByUser(id);
        return taskHistories;
    }

    @GetMapping("/task-histories/j-group/{id}")
    @Timed
    public List<TaskHistory> getHistoriesOfJGroupTasks(@PathVariable Long id){
        log.debug("REST request to get all histories of specific jgroup tasks.", id);
        List<TaskHistory> taskHistories = taskHistoryRepository.queryHistoriesOfJGroupTasks(id);
        return taskHistories;
    }

    /**
     * GET  /task-histories/:id : get the "id" taskHistory.
     *
     * @param id the id of the taskHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskHistory, or with status 404 (Not Found)
     */
    @GetMapping("/task-histories/{id}")
    @Timed
    public ResponseEntity<TaskHistory> getTaskHistory(@PathVariable Long id) {
        log.debug("REST request to get TaskHistory : {}", id);
        TaskHistory taskHistory = taskHistoryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(taskHistory));
    }

    /**
     * DELETE  /task-histories/:id : delete the "id" taskHistory.
     *
     * @param id the id of the taskHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/task-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteTaskHistory(@PathVariable Long id) {
        log.debug("REST request to delete TaskHistory : {}", id);
        taskHistoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
