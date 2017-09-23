package com.coopsystem.web.rest;

import com.coopsystem.CoopApp;

import com.coopsystem.domain.ProjectImg;
import com.coopsystem.repository.ProjectImgRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjectImgResource REST controller.
 *
 * @see ProjectImgResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoopApp.class)
public class ProjectImgResourceIntTest {

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    @Autowired
    private ProjectImgRepository projectImgRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectImgMockMvc;

    private ProjectImg projectImg;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            ProjectImgResource projectImgResource = new ProjectImgResource(projectImgRepository);
        this.restProjectImgMockMvc = MockMvcBuilders.standaloneSetup(projectImgResource)
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
    public static ProjectImg createEntity(EntityManager em) {
        ProjectImg projectImg = new ProjectImg()
                .content(DEFAULT_CONTENT)
                .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE);
        return projectImg;
    }

    @Before
    public void initTest() {
        projectImg = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectImg() throws Exception {
        int databaseSizeBeforeCreate = projectImgRepository.findAll().size();

        // Create the ProjectImg

        restProjectImgMockMvc.perform(post("/api/project-imgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectImg)))
            .andExpect(status().isCreated());

        // Validate the ProjectImg in the database
        List<ProjectImg> projectImgList = projectImgRepository.findAll();
        assertThat(projectImgList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectImg testProjectImg = projectImgList.get(projectImgList.size() - 1);
        assertThat(testProjectImg.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testProjectImg.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createProjectImgWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectImgRepository.findAll().size();

        // Create the ProjectImg with an existing ID
        ProjectImg existingProjectImg = new ProjectImg();
        existingProjectImg.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectImgMockMvc.perform(post("/api/project-imgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingProjectImg)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProjectImg> projectImgList = projectImgRepository.findAll();
        assertThat(projectImgList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectImgRepository.findAll().size();
        // set the field null
        projectImg.setContent(null);

        // Create the ProjectImg, which fails.

        restProjectImgMockMvc.perform(post("/api/project-imgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectImg)))
            .andExpect(status().isBadRequest());

        List<ProjectImg> projectImgList = projectImgRepository.findAll();
        assertThat(projectImgList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectImgs() throws Exception {
        // Initialize the database
        projectImgRepository.saveAndFlush(projectImg);

        // Get all the projectImgList
        restProjectImgMockMvc.perform(get("/api/project-imgs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectImg.getId().intValue())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }

    @Test
    @Transactional
    public void getProjectImg() throws Exception {
        // Initialize the database
        projectImgRepository.saveAndFlush(projectImg);

        // Get the projectImg
        restProjectImgMockMvc.perform(get("/api/project-imgs/{id}", projectImg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectImg.getId().intValue()))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    public void getNonExistingProjectImg() throws Exception {
        // Get the projectImg
        restProjectImgMockMvc.perform(get("/api/project-imgs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectImg() throws Exception {
        // Initialize the database
        projectImgRepository.saveAndFlush(projectImg);
        int databaseSizeBeforeUpdate = projectImgRepository.findAll().size();

        // Update the projectImg
        ProjectImg updatedProjectImg = projectImgRepository.findOne(projectImg.getId());
        updatedProjectImg
                .content(UPDATED_CONTENT)
                .contentContentType(UPDATED_CONTENT_CONTENT_TYPE);

        restProjectImgMockMvc.perform(put("/api/project-imgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProjectImg)))
            .andExpect(status().isOk());

        // Validate the ProjectImg in the database
        List<ProjectImg> projectImgList = projectImgRepository.findAll();
        assertThat(projectImgList).hasSize(databaseSizeBeforeUpdate);
        ProjectImg testProjectImg = projectImgList.get(projectImgList.size() - 1);
        assertThat(testProjectImg.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testProjectImg.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectImg() throws Exception {
        int databaseSizeBeforeUpdate = projectImgRepository.findAll().size();

        // Create the ProjectImg

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectImgMockMvc.perform(put("/api/project-imgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectImg)))
            .andExpect(status().isCreated());

        // Validate the ProjectImg in the database
        List<ProjectImg> projectImgList = projectImgRepository.findAll();
        assertThat(projectImgList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjectImg() throws Exception {
        // Initialize the database
        projectImgRepository.saveAndFlush(projectImg);
        int databaseSizeBeforeDelete = projectImgRepository.findAll().size();

        // Get the projectImg
        restProjectImgMockMvc.perform(delete("/api/project-imgs/{id}", projectImg.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectImg> projectImgList = projectImgRepository.findAll();
        assertThat(projectImgList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectImg.class);
    }
}
