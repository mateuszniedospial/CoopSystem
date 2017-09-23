package com.coopsystem.web.rest;

import com.coopsystem.CoopApp;

import com.coopsystem.domain.JoinJGroupRequest;
import com.coopsystem.repository.JoinJGroupRequestRepository;
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

/**
 * Test class for the JoinJGroupRequestResource REST controller.
 *
 * @see JoinJGroupRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoopApp.class)
public class JoinJGroupRequestResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private JoinJGroupRequestRepository joinJGroupRequestRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJoinJGroupRequestMockMvc;

    private JoinJGroupRequest joinJGroupRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            JoinJGroupRequestResource joinJGroupRequestResource = new JoinJGroupRequestResource(joinJGroupRequestRepository);
        this.restJoinJGroupRequestMockMvc = MockMvcBuilders.standaloneSetup(joinJGroupRequestResource)
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
    public static JoinJGroupRequest createEntity(EntityManager em) {
        JoinJGroupRequest joinJGroupRequest = new JoinJGroupRequest()
                .description(DEFAULT_DESCRIPTION)
                .createdDate(DEFAULT_CREATED_DATE);
        return joinJGroupRequest;
    }

    @Before
    public void initTest() {
        joinJGroupRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createJoinJGroupRequest() throws Exception {
        int databaseSizeBeforeCreate = joinJGroupRequestRepository.findAll().size();

        // Create the JoinJGroupRequest

        restJoinJGroupRequestMockMvc.perform(post("/api/join-j-group-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(joinJGroupRequest)))
            .andExpect(status().isCreated());

        // Validate the JoinJGroupRequest in the database
        List<JoinJGroupRequest> joinJGroupRequestList = joinJGroupRequestRepository.findAll();
        assertThat(joinJGroupRequestList).hasSize(databaseSizeBeforeCreate + 1);
        JoinJGroupRequest testJoinJGroupRequest = joinJGroupRequestList.get(joinJGroupRequestList.size() - 1);
        assertThat(testJoinJGroupRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testJoinJGroupRequest.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createJoinJGroupRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = joinJGroupRequestRepository.findAll().size();

        // Create the JoinJGroupRequest with an existing ID
        JoinJGroupRequest existingJoinJGroupRequest = new JoinJGroupRequest();
        existingJoinJGroupRequest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJoinJGroupRequestMockMvc.perform(post("/api/join-j-group-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingJoinJGroupRequest)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<JoinJGroupRequest> joinJGroupRequestList = joinJGroupRequestRepository.findAll();
        assertThat(joinJGroupRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJoinJGroupRequests() throws Exception {
        // Initialize the database
        joinJGroupRequestRepository.saveAndFlush(joinJGroupRequest);

        // Get all the joinJGroupRequestList
        restJoinJGroupRequestMockMvc.perform(get("/api/join-j-group-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(joinJGroupRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))));
    }

    @Test
    @Transactional
    public void getJoinJGroupRequest() throws Exception {
        // Initialize the database
        joinJGroupRequestRepository.saveAndFlush(joinJGroupRequest);

        // Get the joinJGroupRequest
        restJoinJGroupRequestMockMvc.perform(get("/api/join-j-group-requests/{id}", joinJGroupRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(joinJGroupRequest.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingJoinJGroupRequest() throws Exception {
        // Get the joinJGroupRequest
        restJoinJGroupRequestMockMvc.perform(get("/api/join-j-group-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJoinJGroupRequest() throws Exception {
        // Initialize the database
        joinJGroupRequestRepository.saveAndFlush(joinJGroupRequest);
        int databaseSizeBeforeUpdate = joinJGroupRequestRepository.findAll().size();

        // Update the joinJGroupRequest
        JoinJGroupRequest updatedJoinJGroupRequest = joinJGroupRequestRepository.findOne(joinJGroupRequest.getId());
        updatedJoinJGroupRequest
                .description(UPDATED_DESCRIPTION)
                .createdDate(UPDATED_CREATED_DATE);

        restJoinJGroupRequestMockMvc.perform(put("/api/join-j-group-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJoinJGroupRequest)))
            .andExpect(status().isOk());

        // Validate the JoinJGroupRequest in the database
        List<JoinJGroupRequest> joinJGroupRequestList = joinJGroupRequestRepository.findAll();
        assertThat(joinJGroupRequestList).hasSize(databaseSizeBeforeUpdate);
        JoinJGroupRequest testJoinJGroupRequest = joinJGroupRequestList.get(joinJGroupRequestList.size() - 1);
        assertThat(testJoinJGroupRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testJoinJGroupRequest.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingJoinJGroupRequest() throws Exception {
        int databaseSizeBeforeUpdate = joinJGroupRequestRepository.findAll().size();

        // Create the JoinJGroupRequest

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJoinJGroupRequestMockMvc.perform(put("/api/join-j-group-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(joinJGroupRequest)))
            .andExpect(status().isCreated());

        // Validate the JoinJGroupRequest in the database
        List<JoinJGroupRequest> joinJGroupRequestList = joinJGroupRequestRepository.findAll();
        assertThat(joinJGroupRequestList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJoinJGroupRequest() throws Exception {
        // Initialize the database
        joinJGroupRequestRepository.saveAndFlush(joinJGroupRequest);
        int databaseSizeBeforeDelete = joinJGroupRequestRepository.findAll().size();

        // Get the joinJGroupRequest
        restJoinJGroupRequestMockMvc.perform(delete("/api/join-j-group-requests/{id}", joinJGroupRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JoinJGroupRequest> joinJGroupRequestList = joinJGroupRequestRepository.findAll();
        assertThat(joinJGroupRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JoinJGroupRequest.class);
    }
}
