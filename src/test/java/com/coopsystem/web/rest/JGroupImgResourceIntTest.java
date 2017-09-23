package com.coopsystem.web.rest;

import com.coopsystem.CoopApp;

import com.coopsystem.domain.JGroupImg;
import com.coopsystem.repository.JGroupImgRepository;
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
 * Test class for the JGroupImgResource REST controller.
 *
 * @see JGroupImgResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoopApp.class)
public class JGroupImgResourceIntTest {

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    @Autowired
    private JGroupImgRepository jGroupImgRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJGroupImgMockMvc;

    private JGroupImg jGroupImg;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            JGroupImgResource jGroupImgResource = new JGroupImgResource(jGroupImgRepository);
        this.restJGroupImgMockMvc = MockMvcBuilders.standaloneSetup(jGroupImgResource)
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
    public static JGroupImg createEntity(EntityManager em) {
        JGroupImg jGroupImg = new JGroupImg()
                .content(DEFAULT_CONTENT)
                .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE);
        return jGroupImg;
    }

    @Before
    public void initTest() {
        jGroupImg = createEntity(em);
    }

    @Test
    @Transactional
    public void createJGroupImg() throws Exception {
        int databaseSizeBeforeCreate = jGroupImgRepository.findAll().size();

        // Create the JGroupImg

        restJGroupImgMockMvc.perform(post("/api/j-group-imgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jGroupImg)))
            .andExpect(status().isCreated());

        // Validate the JGroupImg in the database
        List<JGroupImg> jGroupImgList = jGroupImgRepository.findAll();
        assertThat(jGroupImgList).hasSize(databaseSizeBeforeCreate + 1);
        JGroupImg testJGroupImg = jGroupImgList.get(jGroupImgList.size() - 1);
        assertThat(testJGroupImg.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testJGroupImg.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createJGroupImgWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jGroupImgRepository.findAll().size();

        // Create the JGroupImg with an existing ID
        JGroupImg existingJGroupImg = new JGroupImg();
        existingJGroupImg.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJGroupImgMockMvc.perform(post("/api/j-group-imgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingJGroupImg)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<JGroupImg> jGroupImgList = jGroupImgRepository.findAll();
        assertThat(jGroupImgList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = jGroupImgRepository.findAll().size();
        // set the field null
        jGroupImg.setContent(null);

        // Create the JGroupImg, which fails.

        restJGroupImgMockMvc.perform(post("/api/j-group-imgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jGroupImg)))
            .andExpect(status().isBadRequest());

        List<JGroupImg> jGroupImgList = jGroupImgRepository.findAll();
        assertThat(jGroupImgList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJGroupImgs() throws Exception {
        // Initialize the database
        jGroupImgRepository.saveAndFlush(jGroupImg);

        // Get all the jGroupImgList
        restJGroupImgMockMvc.perform(get("/api/j-group-imgs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jGroupImg.getId().intValue())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }

    @Test
    @Transactional
    public void getJGroupImg() throws Exception {
        // Initialize the database
        jGroupImgRepository.saveAndFlush(jGroupImg);

        // Get the jGroupImg
        restJGroupImgMockMvc.perform(get("/api/j-group-imgs/{id}", jGroupImg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jGroupImg.getId().intValue()))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    public void getNonExistingJGroupImg() throws Exception {
        // Get the jGroupImg
        restJGroupImgMockMvc.perform(get("/api/j-group-imgs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJGroupImg() throws Exception {
        // Initialize the database
        jGroupImgRepository.saveAndFlush(jGroupImg);
        int databaseSizeBeforeUpdate = jGroupImgRepository.findAll().size();

        // Update the jGroupImg
        JGroupImg updatedJGroupImg = jGroupImgRepository.findOne(jGroupImg.getId());
        updatedJGroupImg
                .content(UPDATED_CONTENT)
                .contentContentType(UPDATED_CONTENT_CONTENT_TYPE);

        restJGroupImgMockMvc.perform(put("/api/j-group-imgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJGroupImg)))
            .andExpect(status().isOk());

        // Validate the JGroupImg in the database
        List<JGroupImg> jGroupImgList = jGroupImgRepository.findAll();
        assertThat(jGroupImgList).hasSize(databaseSizeBeforeUpdate);
        JGroupImg testJGroupImg = jGroupImgList.get(jGroupImgList.size() - 1);
        assertThat(testJGroupImg.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testJGroupImg.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingJGroupImg() throws Exception {
        int databaseSizeBeforeUpdate = jGroupImgRepository.findAll().size();

        // Create the JGroupImg

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJGroupImgMockMvc.perform(put("/api/j-group-imgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jGroupImg)))
            .andExpect(status().isCreated());

        // Validate the JGroupImg in the database
        List<JGroupImg> jGroupImgList = jGroupImgRepository.findAll();
        assertThat(jGroupImgList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJGroupImg() throws Exception {
        // Initialize the database
        jGroupImgRepository.saveAndFlush(jGroupImg);
        int databaseSizeBeforeDelete = jGroupImgRepository.findAll().size();

        // Get the jGroupImg
        restJGroupImgMockMvc.perform(delete("/api/j-group-imgs/{id}", jGroupImg.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JGroupImg> jGroupImgList = jGroupImgRepository.findAll();
        assertThat(jGroupImgList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JGroupImg.class);
    }
}
