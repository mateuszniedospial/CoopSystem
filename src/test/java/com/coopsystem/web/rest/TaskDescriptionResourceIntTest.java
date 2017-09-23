//package com.myjir.web.rest;
//
//import com.myjir.CoopApp;
//
//import com.myjir.domain.TaskDescription;
//import com.myjir.repository.TaskDescriptionRepository;
//import com.myjir.web.rest.errors.ExceptionTranslator;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.Base64Utils;
//
//import javax.persistence.EntityManager;
//import java.time.Instant;
//import java.time.ZonedDateTime;
//import java.time.ZoneOffset;
//import java.time.ZoneId;
//import java.util.List;
//
//import static com.myjir.web.rest.TestUtil.sameInstant;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * Test class for the TaskDescriptionResource REST controller.
// *
// * @see TaskDescriptionResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = CoopApp.class)
//public class TaskDescriptionResourceIntTest {
//
//    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final ZonedDateTime DEFAULT_MODIFY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_MODIFY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
//    private static final String UPDATED_CONTENT = "BBBBBBBBBB";
//
//    @Autowired
//    private TaskDescriptionRepository taskDescriptionRepository;
//
//    @Autowired
//    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
//
//    @Autowired
//    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
//
//    @Autowired
//    private ExceptionTranslator exceptionTranslator;
//
//    @Autowired
//    private EntityManager em;
//
//    private MockMvc restTaskDescriptionMockMvc;
//
//    private TaskDescription taskDescription;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//            TaskDescriptionResource taskDescriptionResource = new TaskDescriptionResource(taskDescriptionRepository);
//        this.restTaskDescriptionMockMvc = MockMvcBuilders.standaloneSetup(taskDescriptionResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setControllerAdvice(exceptionTranslator)
//            .setMessageConverters(jacksonMessageConverter).build();
//    }
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static TaskDescription createEntity(EntityManager em) {
//        TaskDescription taskDescription = new TaskDescription()
//                .createdDate(DEFAULT_CREATED_DATE)
//                .modifyDate(DEFAULT_MODIFY_DATE)
//                .content(DEFAULT_CONTENT);
//        return taskDescription;
//    }
//
//    @Before
//    public void initTest() {
//        taskDescription = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createTaskDescription() throws Exception {
//        int databaseSizeBeforeCreate = taskDescriptionRepository.findAll().size();
//
//        // Create the TaskDescription
//
//        restTaskDescriptionMockMvc.perform(post("/api/task-descriptions")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(taskDescription)))
//            .andExpect(status().isCreated());
//
//        // Validate the TaskDescription in the database
//        List<TaskDescription> taskDescriptionList = taskDescriptionRepository.findAll();
//        assertThat(taskDescriptionList).hasSize(databaseSizeBeforeCreate + 1);
//        TaskDescription testTaskDescription = taskDescriptionList.get(taskDescriptionList.size() - 1);
//        assertThat(testTaskDescription.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
//        assertThat(testTaskDescription.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
//        assertThat(testTaskDescription.getContent()).isEqualTo(DEFAULT_CONTENT);
//    }
//
//    @Test
//    @Transactional
//    public void createTaskDescriptionWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = taskDescriptionRepository.findAll().size();
//
//        // Create the TaskDescription with an existing ID
//        TaskDescription existingTaskDescription = new TaskDescription();
//        existingTaskDescription.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restTaskDescriptionMockMvc.perform(post("/api/task-descriptions")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(existingTaskDescription)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Alice in the database
//        List<TaskDescription> taskDescriptionList = taskDescriptionRepository.findAll();
//        assertThat(taskDescriptionList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void getAllTaskDescriptions() throws Exception {
//        // Initialize the database
//        taskDescriptionRepository.saveAndFlush(taskDescription);
//
//        // Get all the taskDescriptionList
//        restTaskDescriptionMockMvc.perform(get("/api/task-descriptions?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(taskDescription.getId().intValue())))
//            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
//            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(sameInstant(DEFAULT_MODIFY_DATE))))
//            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
//    }
//
//    @Test
//    @Transactional
//    public void getTaskDescription() throws Exception {
//        // Initialize the database
//        taskDescriptionRepository.saveAndFlush(taskDescription);
//
//        // Get the taskDescription
//        restTaskDescriptionMockMvc.perform(get("/api/task-descriptions/{id}", taskDescription.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(taskDescription.getId().intValue()))
//            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
//            .andExpect(jsonPath("$.modifyDate").value(sameInstant(DEFAULT_MODIFY_DATE)))
//            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingTaskDescription() throws Exception {
//        // Get the taskDescription
//        restTaskDescriptionMockMvc.perform(get("/api/task-descriptions/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateTaskDescription() throws Exception {
//        // Initialize the database
//        taskDescriptionRepository.saveAndFlush(taskDescription);
//        int databaseSizeBeforeUpdate = taskDescriptionRepository.findAll().size();
//
//        // Update the taskDescription
//        TaskDescription updatedTaskDescription = taskDescriptionRepository.findOne(taskDescription.getId());
//        updatedTaskDescription
//                .createdDate(UPDATED_CREATED_DATE)
//                .modifyDate(UPDATED_MODIFY_DATE)
//                .content(UPDATED_CONTENT);
//
//        restTaskDescriptionMockMvc.perform(put("/api/task-descriptions")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedTaskDescription)))
//            .andExpect(status().isOk());
//
//        // Validate the TaskDescription in the database
//        List<TaskDescription> taskDescriptionList = taskDescriptionRepository.findAll();
//        assertThat(taskDescriptionList).hasSize(databaseSizeBeforeUpdate);
//        TaskDescription testTaskDescription = taskDescriptionList.get(taskDescriptionList.size() - 1);
//        assertThat(testTaskDescription.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
//        assertThat(testTaskDescription.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
//        assertThat(testTaskDescription.getContent()).isEqualTo(UPDATED_CONTENT);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingTaskDescription() throws Exception {
//        int databaseSizeBeforeUpdate = taskDescriptionRepository.findAll().size();
//
//        // Create the TaskDescription
//
//        // If the entity doesn't have an ID, it will be created instead of just being updated
//        restTaskDescriptionMockMvc.perform(put("/api/task-descriptions")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(taskDescription)))
//            .andExpect(status().isCreated());
//
//        // Validate the TaskDescription in the database
//        List<TaskDescription> taskDescriptionList = taskDescriptionRepository.findAll();
//        assertThat(taskDescriptionList).hasSize(databaseSizeBeforeUpdate + 1);
//    }
//
//    @Test
//    @Transactional
//    public void deleteTaskDescription() throws Exception {
//        // Initialize the database
//        taskDescriptionRepository.saveAndFlush(taskDescription);
//        int databaseSizeBeforeDelete = taskDescriptionRepository.findAll().size();
//
//        // Get the taskDescription
//        restTaskDescriptionMockMvc.perform(delete("/api/task-descriptions/{id}", taskDescription.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<TaskDescription> taskDescriptionList = taskDescriptionRepository.findAll();
//        assertThat(taskDescriptionList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(TaskDescription.class);
//    }
//}
