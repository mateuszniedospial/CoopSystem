package com.coopsystem.web.rest;

import com.coopsystem.CoopApp;

import com.coopsystem.domain.DocumentationCatalogue;
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

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DocumentationCatalogueResource REST controller.
 *
 * @see DocumentationCatalogueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoopApp.class)
public class DocumentationCatalogueResourceIntTest {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    @Autowired
    private DocumentationCatalogueRepository documentationCatalogueRepository;

    @Autowired
    private ProjectDocumentationRepository projectDocumentationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDocumentationCatalogueMockMvc;

    private DocumentationCatalogue documentationCatalogue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            DocumentationCatalogueResource documentationCatalogueResource = new DocumentationCatalogueResource(documentationCatalogueRepository, projectDocumentationRepository);
        this.restDocumentationCatalogueMockMvc = MockMvcBuilders.standaloneSetup(documentationCatalogueResource)
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
    public static DocumentationCatalogue createEntity(EntityManager em) {
        DocumentationCatalogue documentationCatalogue = new DocumentationCatalogue()
                .label(DEFAULT_LABEL)
                .data(DEFAULT_DATA);
        return documentationCatalogue;
    }

    @Before
    public void initTest() {
        documentationCatalogue = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocumentationCatalogue() throws Exception {
        int databaseSizeBeforeCreate = documentationCatalogueRepository.findAll().size();

        // Create the DocumentationCatalogue

        restDocumentationCatalogueMockMvc.perform(post("/api/documentation-catalogues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentationCatalogue)))
            .andExpect(status().isCreated());

        // Validate the DocumentationCatalogue in the database
        List<DocumentationCatalogue> documentationCatalogueList = documentationCatalogueRepository.findAll();
        assertThat(documentationCatalogueList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentationCatalogue testDocumentationCatalogue = documentationCatalogueList.get(documentationCatalogueList.size() - 1);
        assertThat(testDocumentationCatalogue.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testDocumentationCatalogue.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    public void createDocumentationCatalogueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentationCatalogueRepository.findAll().size();

        // Create the DocumentationCatalogue with an existing ID
        DocumentationCatalogue existingDocumentationCatalogue = new DocumentationCatalogue();
        existingDocumentationCatalogue.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentationCatalogueMockMvc.perform(post("/api/documentation-catalogues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingDocumentationCatalogue)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DocumentationCatalogue> documentationCatalogueList = documentationCatalogueRepository.findAll();
        assertThat(documentationCatalogueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDocumentationCatalogues() throws Exception {
        // Initialize the database
        documentationCatalogueRepository.saveAndFlush(documentationCatalogue);

        // Get all the documentationCatalogueList
        restDocumentationCatalogueMockMvc.perform(get("/api/documentation-catalogues?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentationCatalogue.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }

    @Test
    @Transactional
    public void getDocumentationCatalogue() throws Exception {
        // Initialize the database
        documentationCatalogueRepository.saveAndFlush(documentationCatalogue);

        // Get the documentationCatalogue
        restDocumentationCatalogueMockMvc.perform(get("/api/documentation-catalogues/{id}", documentationCatalogue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(documentationCatalogue.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDocumentationCatalogue() throws Exception {
        // Get the documentationCatalogue
        restDocumentationCatalogueMockMvc.perform(get("/api/documentation-catalogues/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocumentationCatalogue() throws Exception {
        // Initialize the database
        documentationCatalogueRepository.saveAndFlush(documentationCatalogue);
        int databaseSizeBeforeUpdate = documentationCatalogueRepository.findAll().size();

        // Update the documentationCatalogue
        DocumentationCatalogue updatedDocumentationCatalogue = documentationCatalogueRepository.findOne(documentationCatalogue.getId());
        updatedDocumentationCatalogue
                .label(UPDATED_LABEL)
                .data(UPDATED_DATA);

        restDocumentationCatalogueMockMvc.perform(put("/api/documentation-catalogues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocumentationCatalogue)))
            .andExpect(status().isOk());

        // Validate the DocumentationCatalogue in the database
        List<DocumentationCatalogue> documentationCatalogueList = documentationCatalogueRepository.findAll();
        assertThat(documentationCatalogueList).hasSize(databaseSizeBeforeUpdate);
        DocumentationCatalogue testDocumentationCatalogue = documentationCatalogueList.get(documentationCatalogueList.size() - 1);
        assertThat(testDocumentationCatalogue.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testDocumentationCatalogue.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    public void updateNonExistingDocumentationCatalogue() throws Exception {
        int databaseSizeBeforeUpdate = documentationCatalogueRepository.findAll().size();

        // Create the DocumentationCatalogue

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDocumentationCatalogueMockMvc.perform(put("/api/documentation-catalogues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentationCatalogue)))
            .andExpect(status().isCreated());

        // Validate the DocumentationCatalogue in the database
        List<DocumentationCatalogue> documentationCatalogueList = documentationCatalogueRepository.findAll();
        assertThat(documentationCatalogueList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDocumentationCatalogue() throws Exception {
        // Initialize the database
        documentationCatalogueRepository.saveAndFlush(documentationCatalogue);
        int databaseSizeBeforeDelete = documentationCatalogueRepository.findAll().size();

        // Get the documentationCatalogue
        restDocumentationCatalogueMockMvc.perform(delete("/api/documentation-catalogues/{id}", documentationCatalogue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DocumentationCatalogue> documentationCatalogueList = documentationCatalogueRepository.findAll();
        assertThat(documentationCatalogueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentationCatalogue.class);
    }
}
