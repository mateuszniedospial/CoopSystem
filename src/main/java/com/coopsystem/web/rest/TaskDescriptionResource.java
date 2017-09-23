package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.TaskDescription;

import com.coopsystem.repository.TaskDescriptionRepository;
import com.coopsystem.service.TaskHistoryDiffService;
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
 * REST controller for managing TaskDescription.
 */
@RestController
@RequestMapping("/api")
public class TaskDescriptionResource {

    private final Logger log = LoggerFactory.getLogger(TaskDescriptionResource.class);

    private static final String ENTITY_NAME = "taskDescription";

    private final TaskDescriptionRepository taskDescriptionRepository;
    private final TaskHistoryDiffService taskHistoryDiffService;
    public TaskDescriptionResource(TaskDescriptionRepository taskDescriptionRepository, TaskHistoryDiffService taskHistoryDiffService) {
        this.taskDescriptionRepository = taskDescriptionRepository;
        this.taskHistoryDiffService = taskHistoryDiffService;
    }

    /**
     * POST  /task-descriptions : Create a new taskDescription.
     *
     * @param taskDescription the taskDescription to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskDescription, or with status 400 (Bad Request) if the taskDescription has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/task-descriptions")
    @Timed
    public ResponseEntity<TaskDescription> createTaskDescription(@RequestBody TaskDescription taskDescription) throws URISyntaxException {
        log.debug("REST request to save TaskDescription : {}", taskDescription);
        if (taskDescription.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new taskDescription cannot already have an ID")).body(null);
        }
        TaskDescription result = taskDescriptionRepository.save(taskDescription);
        return ResponseEntity.created(new URI("/api/task-descriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /task-descriptions : Updates an existing taskDescription.
     *
     * @param taskDescription the taskDescription to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskDescription,
     * or with status 400 (Bad Request) if the taskDescription is not valid,
     * or with status 500 (Internal Server Error) if the taskDescription couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/task-descriptions")
    @Timed
    public ResponseEntity<TaskDescription> updateTaskDescription(@RequestBody TaskDescription taskDescription) throws URISyntaxException {
        log.debug("REST request to update TaskDescription : {}", taskDescription);
        if (taskDescription.getId() == null) {
            return createTaskDescription(taskDescription);
        }
        TaskDescription oldDescription = taskDescriptionRepository.findOne(taskDescription.getId());
        taskHistoryDiffService.updateTaskDescriptionHistory(taskDescription, oldDescription);
        TaskDescription result = taskDescriptionRepository.save(taskDescription);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taskDescription.getId().toString()))
            .body(result);
    }

    /**
     * GET  /task-descriptions : get all the taskDescriptions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of taskDescriptions in body
     */
    @GetMapping("/task-descriptions")
    @Timed
    public List<TaskDescription> getAllTaskDescriptions() {
        log.debug("REST request to get all TaskDescriptions");
        List<TaskDescription> taskDescriptions = taskDescriptionRepository.findAll();
        return taskDescriptions;
    }

    /**
     * GET  /task-descriptions/:id : get the "id" taskDescription.
     *
     * @param id the id of the taskDescription to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskDescription, or with status 404 (Not Found)
     */
    @GetMapping("/task-descriptions/{id}")
    @Timed
    public ResponseEntity<TaskDescription> getTaskDescription(@PathVariable Long id) {
        log.debug("REST request to get TaskDescription : {}", id);
        TaskDescription taskDescription = taskDescriptionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(taskDescription));
    }

    @GetMapping("/task-descriptions/tasks/{id}")
    @Timed
    public ResponseEntity<TaskDescription> getTaskDescriptionOfSpecificTask(@PathVariable Long id) {
        log.debug("REST request to get TaskDescription of specific task : {}", id);
        TaskDescription taskDescription = taskDescriptionRepository.findTaskDescriptionOfTask(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(taskDescription));
    }


    /**
     * GET  /task-descriptions/byTaskId : only description without task
     **/
    @GetMapping("/task-descriptions/byTaskId")
    @Timed
    public ResponseEntity<TaskDescription> getTaskDescriptionByTaskId(@RequestParam(value = "query", required = false)  Long taskId) {
        log.debug("REST request to get TaskDescription : {}", taskId);
        TaskDescription taskDescription = taskDescriptionRepository.findDescriptionByTaskId(taskId);
        if(taskDescription!=null)taskDescription.setTask(null);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(taskDescription));
    }

    /**
     * DELETE  /task-descriptions/:id : delete the "id" taskDescription.
     *
     * @param id the id of the taskDescription to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/task-descriptions/{id}")
    @Timed
    public ResponseEntity<Void> deleteTaskDescription(@PathVariable Long id) {
        log.debug("REST request to delete TaskDescription : {}", id);
        taskDescriptionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
