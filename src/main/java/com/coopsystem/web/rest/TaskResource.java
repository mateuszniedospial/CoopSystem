package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.Task;

import com.coopsystem.repository.TaskDescriptionRepository;
import com.coopsystem.repository.TaskRepository;
import com.coopsystem.service.TaskService;
import com.coopsystem.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Task.
 */
@RestController
@RequestMapping("/api")
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    private static final String ENTITY_NAME = "task";

    private final TaskRepository taskRepository;
    private final TaskDescriptionRepository taskDescriptionRepository;

    private final TaskService taskService;


    public TaskResource(TaskRepository taskRepository, TaskDescriptionRepository taskDescriptionRepository, TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskDescriptionRepository = taskDescriptionRepository;
        this.taskService = taskService;
    }

    /**
     * POST  /tasks : Create a new task.
     *
     * @param task the task to create
     * @return the ResponseEntity with status 201 (Created) and with body the new task, or with status 400 (Bad Request) if the task has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tasks")
    @Timed
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) throws URISyntaxException {
        log.debug("REST request to save Task : {}", task);
        task.setCreatedDate(ZonedDateTime.now());
        task.setModifyDate(ZonedDateTime.now());// not remove this line, its important for search tool
        if (task.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new task cannot already have an ID")).body(null);
        }
        Task result = taskRepository.save(task);
        return ResponseEntity.created(new URI("/api/tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tasks : Updates an existing task.
     *
     * @param task the task to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated task,
     * or with status 400 (Bad Request) if the task is not valid,
     * or with status 500 (Internal Server Error) if the task couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tasks")
    @Timed
    public ResponseEntity<Task> updateTask(@Valid @RequestBody Task task) throws URISyntaxException {
        log.debug("REST request to update Task : {}", task);
        if (task.getId() == null) {
            return createTask(task);
        }
        Task taskFromDB = taskRepository.findOneWithEagerRelationships(task.getId());
        Task result = taskRepository.save(task);
        taskService.updateSprintIfRequired(task, taskFromDB);
        taskService.updateTaskHistory(task,taskFromDB);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, task.getId().toString()))
            .body(result);
    }

    @PutMapping("/tasks/estimate")
    @Timed
    public ResponseEntity<Task> updateEstimate(@Valid @RequestBody Task task) throws URISyntaxException {
        log.debug("REST request to update estimate Task : {}", task);
        Task taskFromDB = taskRepository.findOne(task.getId());
        taskFromDB.setEstimateTime(task.getEstimateTime());
        return updateTask(task);
    }

    /**
     * GET  /tasks : get all the tasks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tasks in body
     */
    @GetMapping("/tasks")
    @Timed
    public List<Task> getAllTasks() {
        log.debug("REST request to get all Tasks");
        List<Task> tasks = taskRepository.findAllWithEagerRelationships();
        return tasks;
    }

    @GetMapping("/tasks/byProject/{id}")
    @Timed
    public List<Task> getAllProjectsTasks(@PathVariable Long id) {
        log.debug("REST request to get all projects Tasks");
        List<Task> tasks = taskRepository.findAllProjectTasks(id);
        return tasks;
    }

    /**
     * GET  /tasks/:id : get the "id" task.
     *
     * @param id the id of the task to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the task, or with status 404 (Not Found)
     */
    @GetMapping("/tasks/{id}")
    @Timed
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        log.debug("REST request to get Task : {}", id);
        Task task = taskRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(task));
    }

    @GetMapping("/tasks/j-groups/{id}")
    @Timed
    public List<Task> getJGroupTasks(@PathVariable Long id) {
        log.debug("REST request to get JGroup Tasks using JGroup ID : {}", id);
        return taskRepository.findAllJGroupTasksWithEagerRelationships(id);
    }

    @GetMapping("/tasks/getLastSprintTaskByGroupId")
    @Timed
    public List<Task> getLastSprintTaskByGroupId(@RequestParam(value = "query", required = false)  Long jGroupId) {
        log.debug("REST :getLastSprintTaskByGroupId : ", jGroupId);
        return taskService.getAllTaskFromLastSprintByJGroupId(jGroupId);
    }

    @GetMapping("/tasks/j-users/{id}")
    @Timed
    public List<Task> getJUserTasks(@PathVariable Long id) {
        log.debug("REST request to get JUser Tasks using JUser ID : {}", id);
        return taskRepository.findAllTasksAssignedToUser(id);
    }

    /**
     * DELETE  /tasks/:id : delete the "id" task.
     *
     * @param id the id of the task to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.debug("REST request to delete Task : {}", id);
        taskRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    @GetMapping("/tasks/byParentId")
    @Timed
    public  List<Task> getTasksByParentId(@RequestParam(value = "query", required = false)  Long parentId) {
        log.debug("REST request to get Tasks by parentId : {}", parentId);
        List<Task> tasks = taskRepository.findTasksByParentId(parentId);
        tasks.forEach(it->{
            it.setParent(null);// get Tasks by Parent without parent
        });
        return tasks;
    }

    @GetMapping("/tasks/bySprintId")
    @Timed
    public  List<Task> getTasksBySprinttId(@RequestParam(value = "query", required = false)  Long sprintId) {
        log.debug("REST request to get Tasks by sprintId : {}", sprintId);
        List<Task> tasks = taskRepository.findTasksBySprintId(sprintId);
        return tasks;
    }
}
