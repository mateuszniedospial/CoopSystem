package com.coopsystem.web.rest;

import com.coopsystem.CoopApp;

import com.coopsystem.domain.Task;
import com.coopsystem.repository.TaskDescriptionRepository;
import com.coopsystem.repository.TaskRepository;
import com.coopsystem.service.TaskService;
import com.coopsystem.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.coopsystem.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coopsystem.domain.enumeration.TaskLifeCycle;
import com.coopsystem.domain.enumeration.TaskType;
import com.coopsystem.domain.enumeration.TaskPriority;
/**
 * Test class for the TaskResource REST controller.
 *
 * @see TaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoopApp.class)
public class TaskResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final TaskLifeCycle DEFAULT_LIFE_CYCLE = TaskLifeCycle.TODO;
    private static final TaskLifeCycle UPDATED_LIFE_CYCLE = TaskLifeCycle.INPROGRESS;

    private static final String DEFAULT_ENVIROMENT = "AAAAAAAAAA";
    private static final String UPDATED_ENVIROMENT = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final TaskType DEFAULT_TYPE = TaskType.TTASK;
    private static final TaskType UPDATED_TYPE = TaskType.BUG;

    private static final TaskPriority DEFAULT_PRIORITY = TaskPriority.MAJOR;
    private static final TaskPriority UPDATED_PRIORITY = TaskPriority.MINOR;

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_REMAINING_TIME = "AAAAAAAAAA";
    private static final String UPDATED_REMAINING_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_ESTIMATE_TIME = "AAAAAAAAAA";
    private static final String UPDATED_ESTIMATE_TIME = "BBBBBBBBBB";

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskDescriptionRepository taskDescriptionRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTaskMockMvc;

    private Task task;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            TaskResource taskResource = new TaskResource(taskRepository, taskDescriptionRepository, taskService);
        this.restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createEntity(EntityManager em) {
        Task task = new Task()
                .createdDate(DEFAULT_CREATED_DATE)
                .modifyDate(DEFAULT_MODIFY_DATE)
                .lifeCycle(DEFAULT_LIFE_CYCLE)
                .enviroment(DEFAULT_ENVIROMENT)
                .title(DEFAULT_TITLE)
                .type(DEFAULT_TYPE)
                .priority(DEFAULT_PRIORITY)
                .version(DEFAULT_VERSION)
                .remainingTime(DEFAULT_REMAINING_TIME)
                .estimateTime(DEFAULT_ESTIMATE_TIME);
        return task;
    }

    @Before
    public void initTest() {
        task = createEntity(em);
    }

    @Test
    @Transactional
    public void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // Create the Task

        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isCreated());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTask.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
        assertThat(testTask.getLifeCycle()).isEqualTo(DEFAULT_LIFE_CYCLE);
        assertThat(testTask.getEnviroment()).isEqualTo(DEFAULT_ENVIROMENT);
        assertThat(testTask.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTask.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTask.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testTask.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testTask.getRemainingTime()).isEqualTo(DEFAULT_REMAINING_TIME);
        assertThat(testTask.getEstimateTime()).isEqualTo(DEFAULT_ESTIMATE_TIME);
    }

    @Test
    @Transactional
    public void createTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // Create the Task with an existing ID
        Task existingTask = new Task();
        existingTask.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTask)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setTitle(null);

        // Create the Task, which fails.

        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setType(null);

        // Create the Task, which fails.

        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setPriority(null);

        // Create the Task, which fails.

        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstimateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setEstimateTime(null);

        // Create the Task, which fails.

        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTasks() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(sameInstant(DEFAULT_MODIFY_DATE))))
            .andExpect(jsonPath("$.[*].lifeCycle").value(hasItem(DEFAULT_LIFE_CYCLE.toString())))
            .andExpect(jsonPath("$.[*].enviroment").value(hasItem(DEFAULT_ENVIROMENT.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].remainingTime").value(hasItem(DEFAULT_REMAINING_TIME.toString())))
            .andExpect(jsonPath("$.[*].estimateTime").value(hasItem(DEFAULT_ESTIMATE_TIME.toString())));
    }

    @Test
    @Transactional
    public void getTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifyDate").value(sameInstant(DEFAULT_MODIFY_DATE)))
            .andExpect(jsonPath("$.lifeCycle").value(DEFAULT_LIFE_CYCLE.toString()))
            .andExpect(jsonPath("$.enviroment").value(DEFAULT_ENVIROMENT.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.remainingTime").value(DEFAULT_REMAINING_TIME.toString()))
            .andExpect(jsonPath("$.estimateTime").value(DEFAULT_ESTIMATE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task
        Task updatedTask = taskRepository.findOne(task.getId());
        updatedTask
                .createdDate(UPDATED_CREATED_DATE)
                .modifyDate(UPDATED_MODIFY_DATE)
                .lifeCycle(UPDATED_LIFE_CYCLE)
                .enviroment(UPDATED_ENVIROMENT)
                .title(UPDATED_TITLE)
                .type(UPDATED_TYPE)
                .priority(UPDATED_PRIORITY)
                .version(UPDATED_VERSION)
                .remainingTime(UPDATED_REMAINING_TIME)
                .estimateTime(UPDATED_ESTIMATE_TIME);

        restTaskMockMvc.perform(put("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTask)))
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTask.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
        assertThat(testTask.getLifeCycle()).isEqualTo(UPDATED_LIFE_CYCLE);
        assertThat(testTask.getEnviroment()).isEqualTo(UPDATED_ENVIROMENT);
        assertThat(testTask.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTask.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTask.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testTask.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testTask.getRemainingTime()).isEqualTo(UPDATED_REMAINING_TIME);
        assertThat(testTask.getEstimateTime()).isEqualTo(UPDATED_ESTIMATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Create the Task

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTaskMockMvc.perform(put("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isCreated());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        int databaseSizeBeforeDelete = taskRepository.findAll().size();

        // Get the task
        restTaskMockMvc.perform(delete("/api/tasks/{id}", task.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Task.class);
    }
}
