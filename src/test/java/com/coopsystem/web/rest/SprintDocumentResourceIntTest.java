package com.coopsystem.web.rest;

import com.coopsystem.CoopApp;

import com.coopsystem.domain.SprintDocument;
import com.coopsystem.repository.SprintDocumentRepository;
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

/**
 * Test class for the SprintDocumentResource REST controller.
 *
 * @see SprintDocumentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoopApp.class)
public class SprintDocumentResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private SprintDocumentRepository sprintDocumentRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSprintDocumentMockMvc;

    private SprintDocument sprintDocument;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            SprintDocumentResource sprintDocumentResource = new SprintDocumentResource(sprintDocumentRepository);
        this.restSprintDocumentMockMvc = MockMvcBuilders.standaloneSetup(sprintDocumentResource)
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
    public static SprintDocument createEntity(EntityManager em) {
        SprintDocument sprintDocument = new SprintDocument()
                .createdDate(DEFAULT_CREATED_DATE)
                .modifyDate(DEFAULT_MODIFY_DATE)
                .content(DEFAULT_CONTENT);
        return sprintDocument;
    }

    @Before
    public void initTest() {
        sprintDocument = createEntity(em);
    }

    @Test
    @Transactional
    public void createSprintDocument() throws Exception {
        int databaseSizeBeforeCreate = sprintDocumentRepository.findAll().size();

        // Create the SprintDocument

        restSprintDocumentMockMvc.perform(post("/api/sprint-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintDocument)))
            .andExpect(status().isCreated());

        // Validate the SprintDocument in the database
        List<SprintDocument> sprintDocumentList = sprintDocumentRepository.findAll();
        assertThat(sprintDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        SprintDocument testSprintDocument = sprintDocumentList.get(sprintDocumentList.size() - 1);
        assertThat(testSprintDocument.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSprintDocument.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
        assertThat(testSprintDocument.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createSprintDocumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sprintDocumentRepository.findAll().size();

        // Create the SprintDocument with an existing ID
        SprintDocument existingSprintDocument = new SprintDocument();
        existingSprintDocument.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSprintDocumentMockMvc.perform(post("/api/sprint-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSprintDocument)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SprintDocument> sprintDocumentList = sprintDocumentRepository.findAll();
        assertThat(sprintDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = sprintDocumentRepository.findAll().size();
        // set the field null
        sprintDocument.setContent(null);

        // Create the SprintDocument, which fails.

        restSprintDocumentMockMvc.perform(post("/api/sprint-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintDocument)))
            .andExpect(status().isBadRequest());

        List<SprintDocument> sprintDocumentList = sprintDocumentRepository.findAll();
        assertThat(sprintDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSprintDocuments() throws Exception {
        // Initialize the database
        sprintDocumentRepository.saveAndFlush(sprintDocument);

        // Get all the sprintDocumentList
        restSprintDocumentMockMvc.perform(get("/api/sprint-documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprintDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(sameInstant(DEFAULT_MODIFY_DATE))))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getSprintDocument() throws Exception {
        // Initialize the database
        sprintDocumentRepository.saveAndFlush(sprintDocument);

        // Get the sprintDocument
        restSprintDocumentMockMvc.perform(get("/api/sprint-documents/{id}", sprintDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sprintDocument.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifyDate").value(sameInstant(DEFAULT_MODIFY_DATE)))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSprintDocument() throws Exception {
        // Get the sprintDocument
        restSprintDocumentMockMvc.perform(get("/api/sprint-documents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSprintDocument() throws Exception {
        // Initialize the database
        sprintDocumentRepository.saveAndFlush(sprintDocument);
        int databaseSizeBeforeUpdate = sprintDocumentRepository.findAll().size();

        // Update the sprintDocument
        SprintDocument updatedSprintDocument = sprintDocumentRepository.findOne(sprintDocument.getId());
        updatedSprintDocument
                .createdDate(UPDATED_CREATED_DATE)
                .modifyDate(UPDATED_MODIFY_DATE)
                .content(UPDATED_CONTENT);

        restSprintDocumentMockMvc.perform(put("/api/sprint-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSprintDocument)))
            .andExpect(status().isOk());

        // Validate the SprintDocument in the database
        List<SprintDocument> sprintDocumentList = sprintDocumentRepository.findAll();
        assertThat(sprintDocumentList).hasSize(databaseSizeBeforeUpdate);
        SprintDocument testSprintDocument = sprintDocumentList.get(sprintDocumentList.size() - 1);
        assertThat(testSprintDocument.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSprintDocument.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
        assertThat(testSprintDocument.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void updateNonExistingSprintDocument() throws Exception {
        int databaseSizeBeforeUpdate = sprintDocumentRepository.findAll().size();

        // Create the SprintDocument

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSprintDocumentMockMvc.perform(put("/api/sprint-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintDocument)))
            .andExpect(status().isCreated());

        // Validate the SprintDocument in the database
        List<SprintDocument> sprintDocumentList = sprintDocumentRepository.findAll();
        assertThat(sprintDocumentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSprintDocument() throws Exception {
        // Initialize the database
        sprintDocumentRepository.saveAndFlush(sprintDocument);
        int databaseSizeBeforeDelete = sprintDocumentRepository.findAll().size();

        // Get the sprintDocument
        restSprintDocumentMockMvc.perform(delete("/api/sprint-documents/{id}", sprintDocument.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SprintDocument> sprintDocumentList = sprintDocumentRepository.findAll();
        assertThat(sprintDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SprintDocument.class);
    }
}
