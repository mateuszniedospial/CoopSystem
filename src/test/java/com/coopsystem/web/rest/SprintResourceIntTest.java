//package com.myjir.web.rest;
//
//import com.myjir.CoopApp;
//
//import com.myjir.domain.Sprint;
//import com.myjir.repository.SprintRepository;
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
//import com.myjir.domain.enumeration.SprintLifeCycle;
///**
// * Test class for the SprintResource REST controller.
// *
// * @see SprintResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = CoopApp.class)
//public class SprintResourceIntTest {
//
//    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
//    private static final String UPDATED_TITLE = "BBBBBBBBBB";
//
//    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final ZonedDateTime DEFAULT_MODIFY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_MODIFY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final SprintLifeCycle DEFAULT_LIFE_CYCLE = SprintLifeCycle.ACTIVE;
//    private static final SprintLifeCycle UPDATED_LIFE_CYCLE = SprintLifeCycle.OUTDATED;
//
//    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final ZonedDateTime DEFAULT_STOP_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_STOP_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final Integer DEFAULT_DURATION_TIME = 1;
//    private static final Integer UPDATED_DURATION_TIME = 2;
//
//    private static final Float DEFAULT_SUM_OF_ESTIMATE = 1F;
//    private static final Float UPDATED_SUM_OF_ESTIMATE = 2F;
//
//    private static final Float DEFAULT_SUM_OF_REMAINING = 1F;
//    private static final Float UPDATED_SUM_OF_REMAINING = 2F;
//
//    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
//    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";
//
//    @Autowired
//    private SprintRepository sprintRepository;
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
//    private MockMvc restSprintMockMvc;
//
//    private Sprint sprint;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//            SprintResource sprintResource = new SprintResource(sprintRepository, reportService);
//        this.restSprintMockMvc = MockMvcBuilders.standaloneSetup(sprintResource)
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
//    public static Sprint createEntity(EntityManager em) {
//        Sprint sprint = new Sprint()
//                .title(DEFAULT_TITLE)
//                .createdDate(DEFAULT_CREATED_DATE)
//                .modifyDate(DEFAULT_MODIFY_DATE)
//                .lifeCycle(DEFAULT_LIFE_CYCLE)
//                .startTime(DEFAULT_START_TIME)
//                .stopTime(DEFAULT_STOP_TIME)
//                .durationTime(DEFAULT_DURATION_TIME)
//                .sumOfEstimate(DEFAULT_SUM_OF_ESTIMATE)
//                .sumOfRemaining(DEFAULT_SUM_OF_REMAINING)
//                .description(DEFAULT_DESCRIPTION);
//        return sprint;
//    }
//
//    @Before
//    public void initTest() {
//        sprint = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createSprint() throws Exception {
//        int databaseSizeBeforeCreate = sprintRepository.findAll().size();
//
//        // Create the Sprint
//
//        restSprintMockMvc.perform(post("/api/sprints")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(sprint)))
//            .andExpect(status().isCreated());
//
//        // Validate the Sprint in the database
//        List<Sprint> sprintList = sprintRepository.findAll();
//        assertThat(sprintList).hasSize(databaseSizeBeforeCreate + 1);
//        Sprint testSprint = sprintList.get(sprintList.size() - 1);
//        assertThat(testSprint.getTitle()).isEqualTo(DEFAULT_TITLE);
//        assertThat(testSprint.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
//        assertThat(testSprint.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
//        assertThat(testSprint.getLifeCycle()).isEqualTo(DEFAULT_LIFE_CYCLE);
//        assertThat(testSprint.getStartTime()).isEqualTo(DEFAULT_START_TIME);
//        assertThat(testSprint.getStopTime()).isEqualTo(DEFAULT_STOP_TIME);
//        assertThat(testSprint.getDurationTime()).isEqualTo(DEFAULT_DURATION_TIME);
//        assertThat(testSprint.getSumOfEstimate()).isEqualTo(DEFAULT_SUM_OF_ESTIMATE);
//        assertThat(testSprint.getSumOfRemaining()).isEqualTo(DEFAULT_SUM_OF_REMAINING);
//        assertThat(testSprint.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
//    }
//
//    @Test
//    @Transactional
//    public void createSprintWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = sprintRepository.findAll().size();
//
//        // Create the Sprint with an existing ID
//        Sprint existingSprint = new Sprint();
//        existingSprint.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restSprintMockMvc.perform(post("/api/sprints")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(existingSprint)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Alice in the database
//        List<Sprint> sprintList = sprintRepository.findAll();
//        assertThat(sprintList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkTitleIsRequired() throws Exception {
//        int databaseSizeBeforeTest = sprintRepository.findAll().size();
//        // set the field null
//        sprint.setTitle(null);
//
//        // Create the Sprint, which fails.
//
//        restSprintMockMvc.perform(post("/api/sprints")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(sprint)))
//            .andExpect(status().isBadRequest());
//
//        List<Sprint> sprintList = sprintRepository.findAll();
//        assertThat(sprintList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllSprints() throws Exception {
//        // Initialize the database
//        sprintRepository.saveAndFlush(sprint);
//
//        // Get all the sprintList
//        restSprintMockMvc.perform(get("/api/sprints?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(sprint.getId().intValue())))
//            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
//            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
//            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(sameInstant(DEFAULT_MODIFY_DATE))))
//            .andExpect(jsonPath("$.[*].lifeCycle").value(hasItem(DEFAULT_LIFE_CYCLE.toString())))
//            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
//            .andExpect(jsonPath("$.[*].stopTime").value(hasItem(sameInstant(DEFAULT_STOP_TIME))))
//            .andExpect(jsonPath("$.[*].durationTime").value(hasItem(DEFAULT_DURATION_TIME)))
//            .andExpect(jsonPath("$.[*].sumOfEstimate").value(hasItem(DEFAULT_SUM_OF_ESTIMATE.doubleValue())))
//            .andExpect(jsonPath("$.[*].sumOfRemaining").value(hasItem(DEFAULT_SUM_OF_REMAINING.doubleValue())))
//            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
//    }
//
//    @Test
//    @Transactional
//    public void getSprint() throws Exception {
//        // Initialize the database
//        sprintRepository.saveAndFlush(sprint);
//
//        // Get the sprint
//        restSprintMockMvc.perform(get("/api/sprints/{id}", sprint.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(sprint.getId().intValue()))
//            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
//            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
//            .andExpect(jsonPath("$.modifyDate").value(sameInstant(DEFAULT_MODIFY_DATE)))
//            .andExpect(jsonPath("$.lifeCycle").value(DEFAULT_LIFE_CYCLE.toString()))
//            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
//            .andExpect(jsonPath("$.stopTime").value(sameInstant(DEFAULT_STOP_TIME)))
//            .andExpect(jsonPath("$.durationTime").value(DEFAULT_DURATION_TIME))
//            .andExpect(jsonPath("$.sumOfEstimate").value(DEFAULT_SUM_OF_ESTIMATE.doubleValue()))
//            .andExpect(jsonPath("$.sumOfRemaining").value(DEFAULT_SUM_OF_REMAINING.doubleValue()))
//            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingSprint() throws Exception {
//        // Get the sprint
//        restSprintMockMvc.perform(get("/api/sprints/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateSprint() throws Exception {
//        // Initialize the database
//        sprintRepository.saveAndFlush(sprint);
//        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();
//
//        // Update the sprint
//        Sprint updatedSprint = sprintRepository.findOne(sprint.getId());
//        updatedSprint
//                .title(UPDATED_TITLE)
//                .createdDate(UPDATED_CREATED_DATE)
//                .modifyDate(UPDATED_MODIFY_DATE)
//                .lifeCycle(UPDATED_LIFE_CYCLE)
//                .startTime(UPDATED_START_TIME)
//                .stopTime(UPDATED_STOP_TIME)
//                .durationTime(UPDATED_DURATION_TIME)
//                .sumOfEstimate(UPDATED_SUM_OF_ESTIMATE)
//                .sumOfRemaining(UPDATED_SUM_OF_REMAINING)
//                .description(UPDATED_DESCRIPTION);
//
//        restSprintMockMvc.perform(put("/api/sprints")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedSprint)))
//            .andExpect(status().isOk());
//
//        // Validate the Sprint in the database
//        List<Sprint> sprintList = sprintRepository.findAll();
//        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
//        Sprint testSprint = sprintList.get(sprintList.size() - 1);
//        assertThat(testSprint.getTitle()).isEqualTo(UPDATED_TITLE);
//        assertThat(testSprint.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
//        assertThat(testSprint.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
//        assertThat(testSprint.getLifeCycle()).isEqualTo(UPDATED_LIFE_CYCLE);
//        assertThat(testSprint.getStartTime()).isEqualTo(UPDATED_START_TIME);
//        assertThat(testSprint.getStopTime()).isEqualTo(UPDATED_STOP_TIME);
//        assertThat(testSprint.getDurationTime()).isEqualTo(UPDATED_DURATION_TIME);
//        assertThat(testSprint.getSumOfEstimate()).isEqualTo(UPDATED_SUM_OF_ESTIMATE);
//        assertThat(testSprint.getSumOfRemaining()).isEqualTo(UPDATED_SUM_OF_REMAINING);
//        assertThat(testSprint.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingSprint() throws Exception {
//        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();
//
//        // Create the Sprint
//
//        // If the entity doesn't have an ID, it will be created instead of just being updated
//        restSprintMockMvc.perform(put("/api/sprints")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(sprint)))
//            .andExpect(status().isCreated());
//
//        // Validate the Sprint in the database
//        List<Sprint> sprintList = sprintRepository.findAll();
//        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate + 1);
//    }
//
//    @Test
//    @Transactional
//    public void deleteSprint() throws Exception {
//        // Initialize the database
//        sprintRepository.saveAndFlush(sprint);
//        int databaseSizeBeforeDelete = sprintRepository.findAll().size();
//
//        // Get the sprint
//        restSprintMockMvc.perform(delete("/api/sprints/{id}", sprint.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<Sprint> sprintList = sprintRepository.findAll();
//        assertThat(sprintList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(Sprint.class);
//    }
//}
