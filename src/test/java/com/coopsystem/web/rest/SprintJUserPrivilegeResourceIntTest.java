package com.coopsystem.web.rest;

import com.coopsystem.CoopApp;

import com.coopsystem.domain.SprintJUserPrivilege;
import com.coopsystem.repository.SprintJUserPrivilegeRepository;
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

import com.coopsystem.domain.enumeration.SprintPrivilegeType;
/**
 * Test class for the SprintJUserPrivilegeResource REST controller.
 *
 * @see SprintJUserPrivilegeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoopApp.class)
public class SprintJUserPrivilegeResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final SprintPrivilegeType DEFAULT_TYPE = SprintPrivilegeType.SCRUM_MASTER;
    private static final SprintPrivilegeType UPDATED_TYPE = SprintPrivilegeType.PRODUCT_OWNER;

    @Autowired
    private SprintJUserPrivilegeRepository sprintJUserPrivilegeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSprintJUserPrivilegeMockMvc;

    private SprintJUserPrivilege sprintJUserPrivilege;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            SprintJUserPrivilegeResource sprintJUserPrivilegeResource = new SprintJUserPrivilegeResource(sprintJUserPrivilegeRepository);
        this.restSprintJUserPrivilegeMockMvc = MockMvcBuilders.standaloneSetup(sprintJUserPrivilegeResource)
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
    public static SprintJUserPrivilege createEntity(EntityManager em) {
        SprintJUserPrivilege sprintJUserPrivilege = new SprintJUserPrivilege()
                .createdDate(DEFAULT_CREATED_DATE)
                .modifyDate(DEFAULT_MODIFY_DATE)
                .type(DEFAULT_TYPE);
        return sprintJUserPrivilege;
    }

    @Before
    public void initTest() {
        sprintJUserPrivilege = createEntity(em);
    }

    @Test
    @Transactional
    public void createSprintJUserPrivilege() throws Exception {
        int databaseSizeBeforeCreate = sprintJUserPrivilegeRepository.findAll().size();

        // Create the SprintJUserPrivilege

        restSprintJUserPrivilegeMockMvc.perform(post("/api/sprint-j-user-privileges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintJUserPrivilege)))
            .andExpect(status().isCreated());

        // Validate the SprintJUserPrivilege in the database
        List<SprintJUserPrivilege> sprintJUserPrivilegeList = sprintJUserPrivilegeRepository.findAll();
        assertThat(sprintJUserPrivilegeList).hasSize(databaseSizeBeforeCreate + 1);
        SprintJUserPrivilege testSprintJUserPrivilege = sprintJUserPrivilegeList.get(sprintJUserPrivilegeList.size() - 1);
        assertThat(testSprintJUserPrivilege.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSprintJUserPrivilege.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
        assertThat(testSprintJUserPrivilege.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createSprintJUserPrivilegeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sprintJUserPrivilegeRepository.findAll().size();

        // Create the SprintJUserPrivilege with an existing ID
        SprintJUserPrivilege existingSprintJUserPrivilege = new SprintJUserPrivilege();
        existingSprintJUserPrivilege.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSprintJUserPrivilegeMockMvc.perform(post("/api/sprint-j-user-privileges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSprintJUserPrivilege)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SprintJUserPrivilege> sprintJUserPrivilegeList = sprintJUserPrivilegeRepository.findAll();
        assertThat(sprintJUserPrivilegeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sprintJUserPrivilegeRepository.findAll().size();
        // set the field null
        sprintJUserPrivilege.setType(null);

        // Create the SprintJUserPrivilege, which fails.

        restSprintJUserPrivilegeMockMvc.perform(post("/api/sprint-j-user-privileges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintJUserPrivilege)))
            .andExpect(status().isBadRequest());

        List<SprintJUserPrivilege> sprintJUserPrivilegeList = sprintJUserPrivilegeRepository.findAll();
        assertThat(sprintJUserPrivilegeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSprintJUserPrivileges() throws Exception {
        // Initialize the database
        sprintJUserPrivilegeRepository.saveAndFlush(sprintJUserPrivilege);

        // Get all the sprintJUserPrivilegeList
        restSprintJUserPrivilegeMockMvc.perform(get("/api/sprint-j-user-privileges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprintJUserPrivilege.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(sameInstant(DEFAULT_MODIFY_DATE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getSprintJUserPrivilege() throws Exception {
        // Initialize the database
        sprintJUserPrivilegeRepository.saveAndFlush(sprintJUserPrivilege);

        // Get the sprintJUserPrivilege
        restSprintJUserPrivilegeMockMvc.perform(get("/api/sprint-j-user-privileges/{id}", sprintJUserPrivilege.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sprintJUserPrivilege.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifyDate").value(sameInstant(DEFAULT_MODIFY_DATE)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSprintJUserPrivilege() throws Exception {
        // Get the sprintJUserPrivilege
        restSprintJUserPrivilegeMockMvc.perform(get("/api/sprint-j-user-privileges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSprintJUserPrivilege() throws Exception {
        // Initialize the database
        sprintJUserPrivilegeRepository.saveAndFlush(sprintJUserPrivilege);
        int databaseSizeBeforeUpdate = sprintJUserPrivilegeRepository.findAll().size();

        // Update the sprintJUserPrivilege
        SprintJUserPrivilege updatedSprintJUserPrivilege = sprintJUserPrivilegeRepository.findOne(sprintJUserPrivilege.getId());
        updatedSprintJUserPrivilege
                .createdDate(UPDATED_CREATED_DATE)
                .modifyDate(UPDATED_MODIFY_DATE)
                .type(UPDATED_TYPE);

        restSprintJUserPrivilegeMockMvc.perform(put("/api/sprint-j-user-privileges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSprintJUserPrivilege)))
            .andExpect(status().isOk());

        // Validate the SprintJUserPrivilege in the database
        List<SprintJUserPrivilege> sprintJUserPrivilegeList = sprintJUserPrivilegeRepository.findAll();
        assertThat(sprintJUserPrivilegeList).hasSize(databaseSizeBeforeUpdate);
        SprintJUserPrivilege testSprintJUserPrivilege = sprintJUserPrivilegeList.get(sprintJUserPrivilegeList.size() - 1);
        assertThat(testSprintJUserPrivilege.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSprintJUserPrivilege.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
        assertThat(testSprintJUserPrivilege.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingSprintJUserPrivilege() throws Exception {
        int databaseSizeBeforeUpdate = sprintJUserPrivilegeRepository.findAll().size();

        // Create the SprintJUserPrivilege

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSprintJUserPrivilegeMockMvc.perform(put("/api/sprint-j-user-privileges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintJUserPrivilege)))
            .andExpect(status().isCreated());

        // Validate the SprintJUserPrivilege in the database
        List<SprintJUserPrivilege> sprintJUserPrivilegeList = sprintJUserPrivilegeRepository.findAll();
        assertThat(sprintJUserPrivilegeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSprintJUserPrivilege() throws Exception {
        // Initialize the database
        sprintJUserPrivilegeRepository.saveAndFlush(sprintJUserPrivilege);
        int databaseSizeBeforeDelete = sprintJUserPrivilegeRepository.findAll().size();

        // Get the sprintJUserPrivilege
        restSprintJUserPrivilegeMockMvc.perform(delete("/api/sprint-j-user-privileges/{id}", sprintJUserPrivilege.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SprintJUserPrivilege> sprintJUserPrivilegeList = sprintJUserPrivilegeRepository.findAll();
        assertThat(sprintJUserPrivilegeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SprintJUserPrivilege.class);
    }
}
