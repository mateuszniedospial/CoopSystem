package com.coopsystem.web.rest;

import com.coopsystem.CoopApp;

import com.coopsystem.domain.ProjectDocumentation;
import com.coopsystem.repository.DocumentationCatalogueRepository;
import com.coopsystem.repository.ProjectDocumentationRepository;
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
 * Test class for the ProjectDocumentationResource REST controller.
 *
 * @see ProjectDocumentationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoopApp.class)
public class ProjectDocumentationResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final LifeCycle DEFAULT_LIFE_CYCLE = LifeCycle.ACTIVE;
    private static final LifeCycle UPDATED_LIFE_CYCLE = LifeCycle.INACTIVE;

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    @Autowired
    private ProjectDocumentationRepository projectDocumentationRepository;

    @Autowired
    private DocumentationCatalogueRepository documentationCatalogueRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectDocumentationMockMvc;

    private ProjectDocumentation projectDocumentation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            ProjectDocumentationResource projectDocumentationResource = new ProjectDocumentationResource(projectDocumentationRepository, documentationCatalogueRepository);
        this.restProjectDocumentationMockMvc = MockMvcBuilders.standaloneSetup(projectDocumentationResource)
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
    public static ProjectDocumentation createEntity(EntityManager em) {
        ProjectDocumentation projectDocumentation = new ProjectDocumentation()
                .createdDate(DEFAULT_CREATED_DATE)
                .modifyDate(DEFAULT_MODIFY_DATE)
                .version(DEFAULT_VERSION)
                .content(DEFAULT_CONTENT)
                .lifeCycle(DEFAULT_LIFE_CYCLE)
                .label(DEFAULT_LABEL)
                .data(DEFAULT_DATA);
        return projectDocumentation;
    }

    @Before
    public void initTest() {
        projectDocumentation = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectDocumentation() throws Exception {
        int databaseSizeBeforeCreate = projectDocumentationRepository.findAll().size();

        // Create the ProjectDocumentation

        restProjectDocumentationMockMvc.perform(post("/api/project-documentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectDocumentation)))
            .andExpect(status().isCreated());

        // Validate the ProjectDocumentation in the database
        List<ProjectDocumentation> projectDocumentationList = projectDocumentationRepository.findAll();
        assertThat(projectDocumentationList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectDocumentation testProjectDocumentation = projectDocumentationList.get(projectDocumentationList.size() - 1);
        assertThat(testProjectDocumentation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProjectDocumentation.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
        assertThat(testProjectDocumentation.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testProjectDocumentation.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testProjectDocumentation.getLifeCycle()).isEqualTo(DEFAULT_LIFE_CYCLE);
        assertThat(testProjectDocumentation.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testProjectDocumentation.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    public void createProjectDocumentationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectDocumentationRepository.findAll().size();

        // Create the ProjectDocumentation with an existing ID
        ProjectDocumentation existingProjectDocumentation = new ProjectDocumentation();
        existingProjectDocumentation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectDocumentationMockMvc.perform(post("/api/project-documentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingProjectDocumentation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProjectDocumentation> projectDocumentationList = projectDocumentationRepository.findAll();
        assertThat(projectDocumentationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProjectDocumentations() throws Exception {
        // Initialize the database
        projectDocumentationRepository.saveAndFlush(projectDocumentation);

        // Get all the projectDocumentationList
        restProjectDocumentationMockMvc.perform(get("/api/project-documentations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectDocumentation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(sameInstant(DEFAULT_MODIFY_DATE))))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].lifeCycle").value(hasItem(DEFAULT_LIFE_CYCLE.toString())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }

    @Test
    @Transactional
    public void getProjectDocumentation() throws Exception {
        // Initialize the database
        projectDocumentationRepository.saveAndFlush(projectDocumentation);

        // Get the projectDocumentation
        restProjectDocumentationMockMvc.perform(get("/api/project-documentations/{id}", projectDocumentation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectDocumentation.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifyDate").value(sameInstant(DEFAULT_MODIFY_DATE)))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.lifeCycle").value(DEFAULT_LIFE_CYCLE.toString()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectDocumentation() throws Exception {
        // Get the projectDocumentation
        restProjectDocumentationMockMvc.perform(get("/api/project-documentations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectDocumentation() throws Exception {
        // Initialize the database
        projectDocumentationRepository.saveAndFlush(projectDocumentation);
        int databaseSizeBeforeUpdate = projectDocumentationRepository.findAll().size();

        // Update the projectDocumentation
        ProjectDocumentation updatedProjectDocumentation = projectDocumentationRepository.findOne(projectDocumentation.getId());
        updatedProjectDocumentation
                .createdDate(UPDATED_CREATED_DATE)
                .modifyDate(UPDATED_MODIFY_DATE)
                .version(UPDATED_VERSION)
                .content(UPDATED_CONTENT)
                .lifeCycle(UPDATED_LIFE_CYCLE)
                .label(UPDATED_LABEL)
                .data(UPDATED_DATA);

        restProjectDocumentationMockMvc.perform(put("/api/project-documentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProjectDocumentation)))
            .andExpect(status().isOk());

        // Validate the ProjectDocumentation in the database
        List<ProjectDocumentation> projectDocumentationList = projectDocumentationRepository.findAll();
        assertThat(projectDocumentationList).hasSize(databaseSizeBeforeUpdate);
        ProjectDocumentation testProjectDocumentation = projectDocumentationList.get(projectDocumentationList.size() - 1);
        assertThat(testProjectDocumentation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProjectDocumentation.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
        assertThat(testProjectDocumentation.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testProjectDocumentation.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testProjectDocumentation.getLifeCycle()).isEqualTo(UPDATED_LIFE_CYCLE);
        assertThat(testProjectDocumentation.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testProjectDocumentation.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = projectDocumentationRepository.findAll().size();

        // Create the ProjectDocumentation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectDocumentationMockMvc.perform(put("/api/project-documentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectDocumentation)))
            .andExpect(status().isCreated());

        // Validate the ProjectDocumentation in the database
        List<ProjectDocumentation> projectDocumentationList = projectDocumentationRepository.findAll();
        assertThat(projectDocumentationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjectDocumentation() throws Exception {
        // Initialize the database
        projectDocumentationRepository.saveAndFlush(projectDocumentation);
        int databaseSizeBeforeDelete = projectDocumentationRepository.findAll().size();

        // Get the projectDocumentation
        restProjectDocumentationMockMvc.perform(delete("/api/project-documentations/{id}", projectDocumentation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectDocumentation> projectDocumentationList = projectDocumentationRepository.findAll();
        assertThat(projectDocumentationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectDocumentation.class);
    }
}
