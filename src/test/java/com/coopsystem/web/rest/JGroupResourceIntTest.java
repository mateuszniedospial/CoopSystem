package com.coopsystem.web.rest;

import com.coopsystem.CoopApp;

import com.coopsystem.domain.JGroup;
import com.coopsystem.repository.JGroupRepository;
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
import org.springframework.util.Base64Utils;

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

import com.coopsystem.domain.enumeration.LifeCycle;
/**
 * Test class for the JGroupResource REST controller.
 *
 * @see JGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoopApp.class)
public class JGroupResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";
    private static final String DEFAULT_DESCRIPTION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DESCRIPTION_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final LifeCycle DEFAULT_LIFE_CYCLE = LifeCycle.ACTIVE;
    private static final LifeCycle UPDATED_LIFE_CYCLE = LifeCycle.INACTIVE;

    @Autowired
    private JGroupRepository jGroupRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJGroupMockMvc;

    private JGroup jGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            JGroupResource jGroupResource = new JGroupResource(jGroupRepository);
        this.restJGroupMockMvc = MockMvcBuilders.standaloneSetup(jGroupResource)
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
    public static JGroup createEntity(EntityManager em) {
        JGroup jGroup = new JGroup()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .createdDate(DEFAULT_CREATED_DATE)
                .modifyDate(DEFAULT_MODIFY_DATE)
                .lifeCycle(DEFAULT_LIFE_CYCLE);
        return jGroup;
    }

    @Before
    public void initTest() {
        jGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createJGroup() throws Exception {
        int databaseSizeBeforeCreate = jGroupRepository.findAll().size();

        // Create the JGroup

        restJGroupMockMvc.perform(post("/api/j-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jGroup)))
            .andExpect(status().isCreated());

        // Validate the JGroup in the database
        List<JGroup> jGroupList = jGroupRepository.findAll();
        assertThat(jGroupList).hasSize(databaseSizeBeforeCreate + 1);
        JGroup testJGroup = jGroupList.get(jGroupList.size() - 1);
        assertThat(testJGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testJGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testJGroup.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testJGroup.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
        assertThat(testJGroup.getLifeCycle()).isEqualTo(DEFAULT_LIFE_CYCLE);
    }

    @Test
    @Transactional
    public void createJGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jGroupRepository.findAll().size();

        // Create the JGroup with an existing ID
        JGroup existingJGroup = new JGroup();
        existingJGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJGroupMockMvc.perform(post("/api/j-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingJGroup)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<JGroup> jGroupList = jGroupRepository.findAll();
        assertThat(jGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = jGroupRepository.findAll().size();
        // set the field null
        jGroup.setName(null);

        // Create the JGroup, which fails.

        restJGroupMockMvc.perform(post("/api/j-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jGroup)))
            .andExpect(status().isBadRequest());

        List<JGroup> jGroupList = jGroupRepository.findAll();
        assertThat(jGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLifeCycleIsRequired() throws Exception {
        int databaseSizeBeforeTest = jGroupRepository.findAll().size();
        // set the field null
        jGroup.setLifeCycle(null);

        // Create the JGroup, which fails.

        restJGroupMockMvc.perform(post("/api/j-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jGroup)))
            .andExpect(status().isBadRequest());

        List<JGroup> jGroupList = jGroupRepository.findAll();
        assertThat(jGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJGroups() throws Exception {
        // Initialize the database
        jGroupRepository.saveAndFlush(jGroup);

        // Get all the jGroupList
        restJGroupMockMvc.perform(get("/api/j-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(sameInstant(DEFAULT_MODIFY_DATE))))
            .andExpect(jsonPath("$.[*].lifeCycle").value(hasItem(DEFAULT_LIFE_CYCLE.toString())));
    }

    @Test
    @Transactional
    public void getJGroup() throws Exception {
        // Initialize the database
        jGroupRepository.saveAndFlush(jGroup);

        // Get the jGroup
        restJGroupMockMvc.perform(get("/api/j-groups/{id}", jGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifyDate").value(sameInstant(DEFAULT_MODIFY_DATE)))
            .andExpect(jsonPath("$.lifeCycle").value(DEFAULT_LIFE_CYCLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJGroup() throws Exception {
        // Get the jGroup
        restJGroupMockMvc.perform(get("/api/j-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJGroup() throws Exception {
        // Initialize the database
        jGroupRepository.saveAndFlush(jGroup);
        int databaseSizeBeforeUpdate = jGroupRepository.findAll().size();

        // Update the jGroup
        JGroup updatedJGroup = jGroupRepository.findOne(jGroup.getId());
        updatedJGroup
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .createdDate(UPDATED_CREATED_DATE)
                .modifyDate(UPDATED_MODIFY_DATE)
                .lifeCycle(UPDATED_LIFE_CYCLE);

        restJGroupMockMvc.perform(put("/api/j-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJGroup)))
            .andExpect(status().isOk());

        // Validate the JGroup in the database
        List<JGroup> jGroupList = jGroupRepository.findAll();
        assertThat(jGroupList).hasSize(databaseSizeBeforeUpdate);
        JGroup testJGroup = jGroupList.get(jGroupList.size() - 1);
        assertThat(testJGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testJGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testJGroup.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testJGroup.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
        assertThat(testJGroup.getLifeCycle()).isEqualTo(UPDATED_LIFE_CYCLE);
    }

    @Test
    @Transactional
    public void updateNonExistingJGroup() throws Exception {
        int databaseSizeBeforeUpdate = jGroupRepository.findAll().size();

        // Create the JGroup

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJGroupMockMvc.perform(put("/api/j-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jGroup)))
            .andExpect(status().isCreated());

        // Validate the JGroup in the database
        List<JGroup> jGroupList = jGroupRepository.findAll();
        assertThat(jGroupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJGroup() throws Exception {
        // Initialize the database
        jGroupRepository.saveAndFlush(jGroup);
        int databaseSizeBeforeDelete = jGroupRepository.findAll().size();

        // Get the jGroup
        restJGroupMockMvc.perform(delete("/api/j-groups/{id}", jGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JGroup> jGroupList = jGroupRepository.findAll();
        assertThat(jGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JGroup.class);
    }
}
