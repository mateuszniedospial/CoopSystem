//package com.myjir.web.rest;
//
//import com.myjir.CoopApp;
//
//import com.myjir.domain.WorkLog;
//import com.myjir.repository.WorkLogRepository;
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
// * Test class for the WorkLogResource REST controller.
// *
// * @see WorkLogResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = CoopApp.class)
//public class WorkLogResourceIntTest {
//
//    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final ZonedDateTime DEFAULT_MODIFY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_MODIFY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final ZonedDateTime DEFAULT_START_WORK = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_START_WORK = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final ZonedDateTime DEFAULT_STOP_WORK = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_STOP_WORK = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
//    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";
//
//    private static final Integer DEFAULT_TIME_IN_HOUR = 1;
//    private static final Integer UPDATED_TIME_IN_HOUR = 2;
//
//    @Autowired
//    private WorkLogRepository workLogRepository;
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
//    private MockMvc restWorkLogMockMvc;
//
//    private WorkLog workLog;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//            WorkLogResource workLogResource = new WorkLogResource(workLogRepository, reportResource);
//        this.restWorkLogMockMvc = MockMvcBuilders.standaloneSetup(workLogResource)
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
//    public static WorkLog createEntity(EntityManager em) {
//        WorkLog workLog = new WorkLog()
//                .createdDate(DEFAULT_CREATED_DATE)
//                .modifyDate(DEFAULT_MODIFY_DATE)
//                .startWork(DEFAULT_START_WORK)
//                .stopWork(DEFAULT_STOP_WORK)
//                .description(DEFAULT_DESCRIPTION)
//                .timeInHour(DEFAULT_TIME_IN_HOUR);
//        return workLog;
//    }
//
//    @Before
//    public void initTest() {
//        workLog = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createWorkLog() throws Exception {
//        int databaseSizeBeforeCreate = workLogRepository.findAll().size();
//
//        // Create the WorkLog
//
//        restWorkLogMockMvc.perform(post("/api/work-logs")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(workLog)))
//            .andExpect(status().isCreated());
//
//        // Validate the WorkLog in the database
//        List<WorkLog> workLogList = workLogRepository.findAll();
//        assertThat(workLogList).hasSize(databaseSizeBeforeCreate + 1);
//        WorkLog testWorkLog = workLogList.get(workLogList.size() - 1);
//        assertThat(testWorkLog.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
//        assertThat(testWorkLog.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
//        assertThat(testWorkLog.getStartWork()).isEqualTo(DEFAULT_START_WORK);
//        assertThat(testWorkLog.getStopWork()).isEqualTo(DEFAULT_STOP_WORK);
//        assertThat(testWorkLog.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
//        assertThat(testWorkLog.getTimeInHour()).isEqualTo(DEFAULT_TIME_IN_HOUR);
//    }
//
//    @Test
//    @Transactional
//    public void createWorkLogWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = workLogRepository.findAll().size();
//
//        // Create the WorkLog with an existing ID
//        WorkLog existingWorkLog = new WorkLog();
//        existingWorkLog.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restWorkLogMockMvc.perform(post("/api/work-logs")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(existingWorkLog)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Alice in the database
//        List<WorkLog> workLogList = workLogRepository.findAll();
//        assertThat(workLogList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkTimeInHourIsRequired() throws Exception {
//        int databaseSizeBeforeTest = workLogRepository.findAll().size();
//        // set the field null
//        workLog.setTimeInHour(null);
//
//        // Create the WorkLog, which fails.
//
//        restWorkLogMockMvc.perform(post("/api/work-logs")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(workLog)))
//            .andExpect(status().isBadRequest());
//
//        List<WorkLog> workLogList = workLogRepository.findAll();
//        assertThat(workLogList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllWorkLogs() throws Exception {
//        // Initialize the database
//        workLogRepository.saveAndFlush(workLog);
//
//        // Get all the workLogList
//        restWorkLogMockMvc.perform(get("/api/work-logs?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(workLog.getId().intValue())))
//            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
//            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(sameInstant(DEFAULT_MODIFY_DATE))))
//            .andExpect(jsonPath("$.[*].startWork").value(hasItem(sameInstant(DEFAULT_START_WORK))))
//            .andExpect(jsonPath("$.[*].stopWork").value(hasItem(sameInstant(DEFAULT_STOP_WORK))))
//            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
//            .andExpect(jsonPath("$.[*].timeInHour").value(hasItem(DEFAULT_TIME_IN_HOUR)));
//    }
//
//    @Test
//    @Transactional
//    public void getWorkLog() throws Exception {
//        // Initialize the database
//        workLogRepository.saveAndFlush(workLog);
//
//        // Get the workLog
//        restWorkLogMockMvc.perform(get("/api/work-logs/{id}", workLog.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(workLog.getId().intValue()))
//            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
//            .andExpect(jsonPath("$.modifyDate").value(sameInstant(DEFAULT_MODIFY_DATE)))
//            .andExpect(jsonPath("$.startWork").value(sameInstant(DEFAULT_START_WORK)))
//            .andExpect(jsonPath("$.stopWork").value(sameInstant(DEFAULT_STOP_WORK)))
//            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
//            .andExpect(jsonPath("$.timeInHour").value(DEFAULT_TIME_IN_HOUR));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingWorkLog() throws Exception {
//        // Get the workLog
//        restWorkLogMockMvc.perform(get("/api/work-logs/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateWorkLog() throws Exception {
//        // Initialize the database
//        workLogRepository.saveAndFlush(workLog);
//        int databaseSizeBeforeUpdate = workLogRepository.findAll().size();
//
//        // Update the workLog
//        WorkLog updatedWorkLog = workLogRepository.findOne(workLog.getId());
//        updatedWorkLog
//                .createdDate(UPDATED_CREATED_DATE)
//                .modifyDate(UPDATED_MODIFY_DATE)
//                .startWork(UPDATED_START_WORK)
//                .stopWork(UPDATED_STOP_WORK)
//                .description(UPDATED_DESCRIPTION)
//                .timeInHour(UPDATED_TIME_IN_HOUR);
//
//        restWorkLogMockMvc.perform(put("/api/work-logs")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedWorkLog)))
//            .andExpect(status().isOk());
//
//        // Validate the WorkLog in the database
//        List<WorkLog> workLogList = workLogRepository.findAll();
//        assertThat(workLogList).hasSize(databaseSizeBeforeUpdate);
//        WorkLog testWorkLog = workLogList.get(workLogList.size() - 1);
//        assertThat(testWorkLog.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
//        assertThat(testWorkLog.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
//        assertThat(testWorkLog.getStartWork()).isEqualTo(UPDATED_START_WORK);
//        assertThat(testWorkLog.getStopWork()).isEqualTo(UPDATED_STOP_WORK);
//        assertThat(testWorkLog.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
//        assertThat(testWorkLog.getTimeInHour()).isEqualTo(UPDATED_TIME_IN_HOUR);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingWorkLog() throws Exception {
//        int databaseSizeBeforeUpdate = workLogRepository.findAll().size();
//
//        // Create the WorkLog
//
//        // If the entity doesn't have an ID, it will be created instead of just being updated
//        restWorkLogMockMvc.perform(put("/api/work-logs")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(workLog)))
//            .andExpect(status().isCreated());
//
//        // Validate the WorkLog in the database
//        List<WorkLog> workLogList = workLogRepository.findAll();
//        assertThat(workLogList).hasSize(databaseSizeBeforeUpdate + 1);
//    }
//
//    @Test
//    @Transactional
//    public void deleteWorkLog() throws Exception {
//        // Initialize the database
//        workLogRepository.saveAndFlush(workLog);
//        int databaseSizeBeforeDelete = workLogRepository.findAll().size();
//
//        // Get the workLog
//        restWorkLogMockMvc.perform(delete("/api/work-logs/{id}", workLog.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<WorkLog> workLogList = workLogRepository.findAll();
//        assertThat(workLogList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(WorkLog.class);
//    }
//}
