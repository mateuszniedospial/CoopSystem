package com.coopsystem.web.rest;

import com.coopsystem.CoopApp;

import com.coopsystem.domain.DocumentationHistory;
import com.coopsystem.repository.DocumentationHistoryRepository;
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
 * Test class for the DocumentationHistoryResource REST controller.
 *
 * @see DocumentationHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoopApp.class)
public class DocumentationHistoryResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final ZonedDateTime DEFAULT_MODIFY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final LifeCycle DEFAULT_LIFE_CYCLE = LifeCycle.ACTIVE;
    private static final LifeCycle UPDATED_LIFE_CYCLE = LifeCycle.INACTIVE;

    @Autowired
    private DocumentationHistoryRepository documentationHistoryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDocumentationHistoryMockMvc;

    private DocumentationHistory documentationHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            DocumentationHistoryResource documentationHistoryResource = new DocumentationHistoryResource(documentationHistoryRepository);
        this.restDocumentationHistoryMockMvc = MockMvcBuilders.standaloneSetup(documentationHistoryResource)
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
    public static DocumentationHistory createEntity(EntityManager em) {
        DocumentationHistory documentationHistory = new DocumentationHistory()
                .createdDate(DEFAULT_CREATED_DATE)
                .content(DEFAULT_CONTENT)
                .version(DEFAULT_VERSION)
                .modifyDate(DEFAULT_MODIFY_DATE)
                .lifeCycle(DEFAULT_LIFE_CYCLE);
        return documentationHistory;
    }

    @Before
    public void initTest() {
        documentationHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocumentationHistory() throws Exception {
        int databaseSizeBeforeCreate = documentationHistoryRepository.findAll().size();

        // Create the DocumentationHistory

        restDocumentationHistoryMockMvc.perform(post("/api/documentation-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentationHistory)))
            .andExpect(status().isCreated());

        // Validate the DocumentationHistory in the database
        List<DocumentationHistory> documentationHistoryList = documentationHistoryRepository.findAll();
        assertThat(documentationHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentationHistory testDocumentationHistory = documentationHistoryList.get(documentationHistoryList.size() - 1);
        assertThat(testDocumentationHistory.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDocumentationHistory.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testDocumentationHistory.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testDocumentationHistory.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
        assertThat(testDocumentationHistory.getLifeCycle()).isEqualTo(DEFAULT_LIFE_CYCLE);
    }

    @Test
    @Transactional
    public void createDocumentationHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentationHistoryRepository.findAll().size();

        // Create the DocumentationHistory with an existing ID
        DocumentationHistory existingDocumentationHistory = new DocumentationHistory();
        existingDocumentationHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentationHistoryMockMvc.perform(post("/api/documentation-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingDocumentationHistory)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DocumentationHistory> documentationHistoryList = documentationHistoryRepository.findAll();
        assertThat(documentationHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDocumentationHistories() throws Exception {
        // Initialize the database
        documentationHistoryRepository.saveAndFlush(documentationHistory);

        // Get all the documentationHistoryList
        restDocumentationHistoryMockMvc.perform(get("/api/documentation-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentationHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(sameInstant(DEFAULT_MODIFY_DATE))))
            .andExpect(jsonPath("$.[*].lifeCycle").value(hasItem(DEFAULT_LIFE_CYCLE.toString())));
    }

    @Test
    @Transactional
    public void getDocumentationHistory() throws Exception {
        // Initialize the database
        documentationHistoryRepository.saveAndFlush(documentationHistory);

        // Get the documentationHistory
        restDocumentationHistoryMockMvc.perform(get("/api/documentation-histories/{id}", documentationHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(documentationHistory.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.modifyDate").value(sameInstant(DEFAULT_MODIFY_DATE)))
            .andExpect(jsonPath("$.lifeCycle").value(DEFAULT_LIFE_CYCLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDocumentationHistory() throws Exception {
        // Get the documentationHistory
        restDocumentationHistoryMockMvc.perform(get("/api/documentation-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocumentationHistory() throws Exception {
        // Initialize the database
        documentationHistoryRepository.saveAndFlush(documentationHistory);
        int databaseSizeBeforeUpdate = documentationHistoryRepository.findAll().size();

        // Update the documentationHistory
        DocumentationHistory updatedDocumentationHistory = documentationHistoryRepository.findOne(documentationHistory.getId());
        updatedDocumentationHistory
                .createdDate(UPDATED_CREATED_DATE)
                .content(UPDATED_CONTENT)
                .version(UPDATED_VERSION)
                .modifyDate(UPDATED_MODIFY_DATE)
                .lifeCycle(UPDATED_LIFE_CYCLE);

        restDocumentationHistoryMockMvc.perform(put("/api/documentation-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocumentationHistory)))
            .andExpect(status().isOk());

        // Validate the DocumentationHistory in the database
        List<DocumentationHistory> documentationHistoryList = documentationHistoryRepository.findAll();
        assertThat(documentationHistoryList).hasSize(databaseSizeBeforeUpdate);
        DocumentationHistory testDocumentationHistory = documentationHistoryList.get(documentationHistoryList.size() - 1);
        assertThat(testDocumentationHistory.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDocumentationHistory.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testDocumentationHistory.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testDocumentationHistory.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
        assertThat(testDocumentationHistory.getLifeCycle()).isEqualTo(UPDATED_LIFE_CYCLE);
    }

    @Test
    @Transactional
    public void updateNonExistingDocumentationHistory() throws Exception {
        int databaseSizeBeforeUpdate = documentationHistoryRepository.findAll().size();

        // Create the DocumentationHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDocumentationHistoryMockMvc.perform(put("/api/documentation-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentationHistory)))
            .andExpect(status().isCreated());

        // Validate the DocumentationHistory in the database
        List<DocumentationHistory> documentationHistoryList = documentationHistoryRepository.findAll();
        assertThat(documentationHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDocumentationHistory() throws Exception {
        // Initialize the database
        documentationHistoryRepository.saveAndFlush(documentationHistory);
        int databaseSizeBeforeDelete = documentationHistoryRepository.findAll().size();

        // Get the documentationHistory
        restDocumentationHistoryMockMvc.perform(delete("/api/documentation-histories/{id}", documentationHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DocumentationHistory> documentationHistoryList = documentationHistoryRepository.findAll();
        assertThat(documentationHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentationHistory.class);
    }
}
