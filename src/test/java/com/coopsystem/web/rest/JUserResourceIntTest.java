//package com.myjir.web.rest;
//
//import com.myjir.CoopApp;
//
//import com.myjir.domain.JUser;
//import com.myjir.repository.JUserRepository;
//import com.myjir.web.rest.errors.ExceptionTranslator;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * Test class for the JUserResource REST controller.
// *
// * @see JUserResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = CoopApp.class)
//public class JUserResourceIntTest {
//
//    private static final Long DEFAULT_JHI_J_USER_ID = 1L;
//    private static final Long UPDATED_JHI_J_USER_ID = 2L;
//
//    @Autowired
//    private JUserRepository jUserRepository;
//
//    @Autowired
//    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
//
//    @Autowired
//    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
//
//    @Autowired
//    private ExceptionTranslator exceptionTranslator;
//
//    @Autowired
//    private EntityManager em;
//
//    private MockMvc restJUserMockMvc;
//
//    private JUser jUser;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//            JUserResource jUserResource = new JUserResource(jUserRepository);
//        this.restJUserMockMvc = MockMvcBuilders.standaloneSetup(jUserResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setControllerAdvice(exceptionTranslator)
//            .setMessageConverters(jacksonMessageConverter).build();
//    }
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static JUser createEntity(EntityManager em) {
////        JUser jUser = new JUser()
////                .jhiJUserId(DEFAULT_JHI_J_USER_ID);
////        return jUser;
//    }
//
//    @Before
//    public void initTest() {
//        jUser = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createJUser() throws Exception {
//        int databaseSizeBeforeCreate = jUserRepository.findAll().size();
//
//        // Create the JUser
//
//        restJUserMockMvc.perform(post("/api/j-users")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(jUser)))
//            .andExpect(status().isCreated());
//
//        // Validate the JUser in the database
//        List<JUser> jUserList = jUserRepository.findAll();
//        assertThat(jUserList).hasSize(databaseSizeBeforeCreate + 1);
//        JUser testJUser = jUserList.get(jUserList.size() - 1);
//        assertThat(testJUser.getUser()).isEqualTo(DEFAULT_JHI_J_USER_ID);
//    }
//
//    @Test
//    @Transactional
//    public void createJUserWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = jUserRepository.findAll().size();
//
//        // Create the JUser with an existing ID
//        JUser existingJUser = new JUser();
//        existingJUser.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restJUserMockMvc.perform(post("/api/j-users")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(existingJUser)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Alice in the database
//        List<JUser> jUserList = jUserRepository.findAll();
//        assertThat(jUserList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkJhiJUserIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = jUserRepository.findAll().size();
//        // set the field null
//        jUser.setUser(null);
//
//        // Create the JUser, which fails.
//
//        restJUserMockMvc.perform(post("/api/j-users")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(jUser)))
//            .andExpect(status().isBadRequest());
//
//        List<JUser> jUserList = jUserRepository.findAll();
//        assertThat(jUserList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllJUsers() throws Exception {
//        // Initialize the database
//        jUserRepository.saveAndFlush(jUser);
//
//        // Get all the jUserList
//        restJUserMockMvc.perform(get("/api/j-users?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(jUser.getId().intValue())))
//            .andExpect(jsonPath("$.[*].jhiJUserId").value(hasItem(DEFAULT_JHI_J_USER_ID.intValue())));
//    }
//
//    @Test
//    @Transactional
//    public void getJUser() throws Exception {
//        // Initialize the database
//        jUserRepository.saveAndFlush(jUser);
//
//        // Get the jUser
//        restJUserMockMvc.perform(get("/api/j-users/{id}", jUser.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(jUser.getId().intValue()))
//            .andExpect(jsonPath("$.jhiJUserId").value(DEFAULT_JHI_J_USER_ID.intValue()));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingJUser() throws Exception {
//        // Get the jUser
//        restJUserMockMvc.perform(get("/api/j-users/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateJUser() throws Exception {
////        // Initialize the database
////        jUserRepository.saveAndFlush(jUser);
////        int databaseSizeBeforeUpdate = jUserRepository.findAll().size();
////
////        // Update the jUser
//////        JUser updatedJUser = jUserRepository.findOne(jUser.getId());
//////        updatedJUser
//////                .jhiJUserId(UPDATED_JHI_J_USER_ID);
////
////        restJUserMockMvc.perform(put("/api/j-users")
////            .contentType(TestUtil.APPLICATION_JSON_UTF8)
////            .content(TestUtil.convertObjectToJsonBytes(updatedJUser)))
////            .andExpect(status().isOk());
////
////        // Validate the JUser in the database
////        List<JUser> jUserList = jUserRepository.findAll();
////        assertThat(jUserList).hasSize(databaseSizeBeforeUpdate);
////        JUser testJUser = jUserList.get(jUserList.size() - 1);
////        assertThat(testJUser.getUser()).isEqualTo(UPDATED_JHI_J_USER_ID);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingJUser() throws Exception {
//        int databaseSizeBeforeUpdate = jUserRepository.findAll().size();
//
//        // Create the JUser
//
//        // If the entity doesn't have an ID, it will be created instead of just being updated
//        restJUserMockMvc.perform(put("/api/j-users")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(jUser)))
//            .andExpect(status().isCreated());
//
//        // Validate the JUser in the database
//        List<JUser> jUserList = jUserRepository.findAll();
//        assertThat(jUserList).hasSize(databaseSizeBeforeUpdate + 1);
//    }
//
//    @Test
//    @Transactional
//    public void deleteJUser() throws Exception {
//        // Initialize the database
//        jUserRepository.saveAndFlush(jUser);
//        int databaseSizeBeforeDelete = jUserRepository.findAll().size();
//
//        // Get the jUser
//        restJUserMockMvc.perform(delete("/api/j-users/{id}", jUser.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<JUser> jUserList = jUserRepository.findAll();
//        assertThat(jUserList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(JUser.class);
//    }
//}
