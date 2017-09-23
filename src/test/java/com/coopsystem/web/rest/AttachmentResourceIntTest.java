//package com.myjir.web.rest;
//
//import com.myjir.CoopApp;
//
//import com.myjir.domain.Attachment;
//import com.myjir.repository.AttachmentRepository;
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
//import org.springframework.util.Base64Utils;
//
//import javax.persistence.EntityManager;
//import java.time.Instant;
//import java.time.ZonedDateTime;
//import java.time.ZoneOffset;
//import java.time.ZoneId;
//import java.util.List;
//
//import static com.myjir.web.rest.TestUtil.sameInstant;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.myjir.domain.enumeration.LifeCycle;
///**
// * Test class for the AttachmentResource REST controller.
// *
// * @see AttachmentResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = CoopApp.class)
//public class AttachmentResourceIntTest {
//
//    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final ZonedDateTime DEFAULT_MODIFY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_MODIFY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final LifeCycle DEFAULT_LIFE_CYCLE = LifeCycle.ACTIVE;
//    private static final LifeCycle UPDATED_LIFE_CYCLE = LifeCycle.INACTIVE;
//
//    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
//    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(2, "1");
//    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
//    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";
//
//    private static final String DEFAULT_NAME = "AAAAAAAAAA";
//    private static final String UPDATED_NAME = "BBBBBBBBBB";
//
//    @Autowired
//    private AttachmentRepository attachmentRepository;
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
//    private MockMvc restAttachmentMockMvc;
//
//    private Attachment attachment;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//            AttachmentResource attachmentResource = new AttachmentResource(taskHistoryDiffService, attachmentRepository);
//        this.restAttachmentMockMvc = MockMvcBuilders.standaloneSetup(attachmentResource)
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
//    public static Attachment createEntity(EntityManager em) {
//        Attachment attachment = new Attachment()
//                .createdDate(DEFAULT_CREATED_DATE)
//                .modifyDate(DEFAULT_MODIFY_DATE)
//                .lifeCycle(DEFAULT_LIFE_CYCLE)
//                .content(DEFAULT_CONTENT)
//                .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
//                .name(DEFAULT_NAME);
//        return attachment;
//    }
//
//    @Before
//    public void initTest() {
//        attachment = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createAttachment() throws Exception {
//        int databaseSizeBeforeCreate = attachmentRepository.findAll().size();
//
//        // Create the Attachment
//
//        restAttachmentMockMvc.perform(post("/api/attachments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(attachment)))
//            .andExpect(status().isCreated());
//
//        // Validate the Attachment in the database
//        List<Attachment> attachmentList = attachmentRepository.findAll();
//        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate + 1);
//        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
//        assertThat(testAttachment.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
//        assertThat(testAttachment.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
//        assertThat(testAttachment.getLifeCycle()).isEqualTo(DEFAULT_LIFE_CYCLE);
//        assertThat(testAttachment.getContent()).isEqualTo(DEFAULT_CONTENT);
//        assertThat(testAttachment.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
//        assertThat(testAttachment.getName()).isEqualTo(DEFAULT_NAME);
//    }
//
//    @Test
//    @Transactional
//    public void createAttachmentWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = attachmentRepository.findAll().size();
//
//        // Create the Attachment with an existing ID
//        Attachment existingAttachment = new Attachment();
//        existingAttachment.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restAttachmentMockMvc.perform(post("/api/attachments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(existingAttachment)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Alice in the database
//        List<Attachment> attachmentList = attachmentRepository.findAll();
//        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkContentIsRequired() throws Exception {
//        int databaseSizeBeforeTest = attachmentRepository.findAll().size();
//        // set the field null
//        attachment.setContent(null);
//
//        // Create the Attachment, which fails.
//
//        restAttachmentMockMvc.perform(post("/api/attachments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(attachment)))
//            .andExpect(status().isBadRequest());
//
//        List<Attachment> attachmentList = attachmentRepository.findAll();
//        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllAttachments() throws Exception {
//        // Initialize the database
//        attachmentRepository.saveAndFlush(attachment);
//
//        // Get all the attachmentList
//        restAttachmentMockMvc.perform(get("/api/attachments?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(attachment.getId().intValue())))
//            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
//            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(sameInstant(DEFAULT_MODIFY_DATE))))
//            .andExpect(jsonPath("$.[*].lifeCycle").value(hasItem(DEFAULT_LIFE_CYCLE.toString())))
//            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
//            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))))
//            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
//    }
//
//    @Test
//    @Transactional
//    public void getAttachment() throws Exception {
//        // Initialize the database
//        attachmentRepository.saveAndFlush(attachment);
//
//        // Get the attachment
//        restAttachmentMockMvc.perform(get("/api/attachments/{id}", attachment.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(attachment.getId().intValue()))
//            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
//            .andExpect(jsonPath("$.modifyDate").value(sameInstant(DEFAULT_MODIFY_DATE)))
//            .andExpect(jsonPath("$.lifeCycle").value(DEFAULT_LIFE_CYCLE.toString()))
//            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
//            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)))
//            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingAttachment() throws Exception {
//        // Get the attachment
//        restAttachmentMockMvc.perform(get("/api/attachments/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateAttachment() throws Exception {
//        // Initialize the database
//        attachmentRepository.saveAndFlush(attachment);
//        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();
//
//        // Update the attachment
//        Attachment updatedAttachment = attachmentRepository.findOne(attachment.getId());
//        updatedAttachment
//                .createdDate(UPDATED_CREATED_DATE)
//                .modifyDate(UPDATED_MODIFY_DATE)
//                .lifeCycle(UPDATED_LIFE_CYCLE)
//                .content(UPDATED_CONTENT)
//                .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
//                .name(UPDATED_NAME);
//
//        restAttachmentMockMvc.perform(put("/api/attachments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedAttachment)))
//            .andExpect(status().isOk());
//
//        // Validate the Attachment in the database
//        List<Attachment> attachmentList = attachmentRepository.findAll();
//        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
//        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
//        assertThat(testAttachment.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
//        assertThat(testAttachment.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
//        assertThat(testAttachment.getLifeCycle()).isEqualTo(UPDATED_LIFE_CYCLE);
//        assertThat(testAttachment.getContent()).isEqualTo(UPDATED_CONTENT);
//        assertThat(testAttachment.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
//        assertThat(testAttachment.getName()).isEqualTo(UPDATED_NAME);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingAttachment() throws Exception {
//        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();
//
//        // Create the Attachment
//
//        // If the entity doesn't have an ID, it will be created instead of just being updated
//        restAttachmentMockMvc.perform(put("/api/attachments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(attachment)))
//            .andExpect(status().isCreated());
//
//        // Validate the Attachment in the database
//        List<Attachment> attachmentList = attachmentRepository.findAll();
//        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate + 1);
//    }
//
//    @Test
//    @Transactional
//    public void deleteAttachment() throws Exception {
//        // Initialize the database
//        attachmentRepository.saveAndFlush(attachment);
//        int databaseSizeBeforeDelete = attachmentRepository.findAll().size();
//
//        // Get the attachment
//        restAttachmentMockMvc.perform(delete("/api/attachments/{id}", attachment.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<Attachment> attachmentList = attachmentRepository.findAll();
//        assertThat(attachmentList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(Attachment.class);
//    }
//}
