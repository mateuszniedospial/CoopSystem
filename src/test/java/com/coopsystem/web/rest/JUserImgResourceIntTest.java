package com.coopsystem.web.rest;

import com.coopsystem.CoopApp;

import com.coopsystem.domain.JUserImg;
import com.coopsystem.repository.JUserImgRepository;
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
 * Test class for the JUserImgResource REST controller.
 *
 * @see JUserImgResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoopApp.class)
public class JUserImgResourceIntTest {

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    @Autowired
    private JUserImgRepository jUserImgRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJUserImgMockMvc;

    private JUserImg jUserImg;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            JUserImgResource jUserImgResource = new JUserImgResource(jUserImgRepository);
        this.restJUserImgMockMvc = MockMvcBuilders.standaloneSetup(jUserImgResource)
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
    public static JUserImg createEntity(EntityManager em) {
        JUserImg jUserImg = new JUserImg()
                .content(DEFAULT_CONTENT)
                .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE);
        return jUserImg;
    }

    @Before
    public void initTest() {
        jUserImg = createEntity(em);
    }

    @Test
    @Transactional
    public void createJUserImg() throws Exception {
        int databaseSizeBeforeCreate = jUserImgRepository.findAll().size();

        // Create the JUserImg

        restJUserImgMockMvc.perform(post("/api/j-user-imgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jUserImg)))
            .andExpect(status().isCreated());

        // Validate the JUserImg in the database
        List<JUserImg> jUserImgList = jUserImgRepository.findAll();
        assertThat(jUserImgList).hasSize(databaseSizeBeforeCreate + 1);
        JUserImg testJUserImg = jUserImgList.get(jUserImgList.size() - 1);
        assertThat(testJUserImg.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testJUserImg.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createJUserImgWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jUserImgRepository.findAll().size();

        // Create the JUserImg with an existing ID
        JUserImg existingJUserImg = new JUserImg();
        existingJUserImg.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJUserImgMockMvc.perform(post("/api/j-user-imgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingJUserImg)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<JUserImg> jUserImgList = jUserImgRepository.findAll();
        assertThat(jUserImgList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJUserImgs() throws Exception {
        // Initialize the database
        jUserImgRepository.saveAndFlush(jUserImg);

        // Get all the jUserImgList
        restJUserImgMockMvc.perform(get("/api/j-user-imgs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jUserImg.getId().intValue())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }

    @Test
    @Transactional
    public void getJUserImg() throws Exception {
        // Initialize the database
        jUserImgRepository.saveAndFlush(jUserImg);

        // Get the jUserImg
        restJUserImgMockMvc.perform(get("/api/j-user-imgs/{id}", jUserImg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jUserImg.getId().intValue()))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    public void getNonExistingJUserImg() throws Exception {
        // Get the jUserImg
        restJUserImgMockMvc.perform(get("/api/j-user-imgs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJUserImg() throws Exception {
        // Initialize the database
        jUserImgRepository.saveAndFlush(jUserImg);
        int databaseSizeBeforeUpdate = jUserImgRepository.findAll().size();

        // Update the jUserImg
        JUserImg updatedJUserImg = jUserImgRepository.findOne(jUserImg.getId());
        updatedJUserImg
                .content(UPDATED_CONTENT)
                .contentContentType(UPDATED_CONTENT_CONTENT_TYPE);

        restJUserImgMockMvc.perform(put("/api/j-user-imgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJUserImg)))
            .andExpect(status().isOk());

        // Validate the JUserImg in the database
        List<JUserImg> jUserImgList = jUserImgRepository.findAll();
        assertThat(jUserImgList).hasSize(databaseSizeBeforeUpdate);
        JUserImg testJUserImg = jUserImgList.get(jUserImgList.size() - 1);
        assertThat(testJUserImg.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testJUserImg.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingJUserImg() throws Exception {
        int databaseSizeBeforeUpdate = jUserImgRepository.findAll().size();

        // Create the JUserImg

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJUserImgMockMvc.perform(put("/api/j-user-imgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jUserImg)))
            .andExpect(status().isCreated());

        // Validate the JUserImg in the database
        List<JUserImg> jUserImgList = jUserImgRepository.findAll();
        assertThat(jUserImgList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJUserImg() throws Exception {
        // Initialize the database
        jUserImgRepository.saveAndFlush(jUserImg);
        int databaseSizeBeforeDelete = jUserImgRepository.findAll().size();

        // Get the jUserImg
        restJUserImgMockMvc.perform(delete("/api/j-user-imgs/{id}", jUserImg.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JUserImg> jUserImgList = jUserImgRepository.findAll();
        assertThat(jUserImgList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JUserImg.class);
    }
}
