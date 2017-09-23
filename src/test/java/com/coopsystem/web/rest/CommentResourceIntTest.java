//package com.myjir.web.rest;
//
//import com.myjir.CoopApp;
//
//import com.myjir.domain.Comment;
//import com.myjir.repository.CommentRepository;
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
///**
// * Test class for the CommentResource REST controller.
// *
// * @see CommentResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = CoopApp.class)
//public class CommentResourceIntTest {
//
//    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final ZonedDateTime DEFAULT_MODIFY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_MODIFY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
//    private static final String UPDATED_CONTENT = "BBBBBBBBBB";
//
//    @Autowired
//    private CommentRepository commentRepository;
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
//    private MockMvc restCommentMockMvc;
//
//    private Comment comment;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//            CommentResource commentResource = new CommentResource(commentRepository);
//        this.restCommentMockMvc = MockMvcBuilders.standaloneSetup(commentResource)
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
//    public static Comment createEntity(EntityManager em) {
//        Comment comment = new Comment()
//                .createdDate(DEFAULT_CREATED_DATE)
//                .modifyDate(DEFAULT_MODIFY_DATE)
//                .content(DEFAULT_CONTENT);
//        return comment;
//    }
//
//    @Before
//    public void initTest() {
//        comment = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createComment() throws Exception {
//        int databaseSizeBeforeCreate = commentRepository.findAll().size();
//
//        // Create the Comment
//
//        restCommentMockMvc.perform(post("/api/comments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(comment)))
//            .andExpect(status().isCreated());
//
//        // Validate the Comment in the database
//        List<Comment> commentList = commentRepository.findAll();
//        assertThat(commentList).hasSize(databaseSizeBeforeCreate + 1);
//        Comment testComment = commentList.get(commentList.size() - 1);
//        assertThat(testComment.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
//        assertThat(testComment.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
//        assertThat(testComment.getContent()).isEqualTo(DEFAULT_CONTENT);
//    }
//
//    @Test
//    @Transactional
//    public void createCommentWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = commentRepository.findAll().size();
//
//        // Create the Comment with an existing ID
//        Comment existingComment = new Comment();
//        existingComment.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restCommentMockMvc.perform(post("/api/comments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(existingComment)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Alice in the database
//        List<Comment> commentList = commentRepository.findAll();
//        assertThat(commentList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void getAllComments() throws Exception {
//        // Initialize the database
//        commentRepository.saveAndFlush(comment);
//
//        // Get all the commentList
//        restCommentMockMvc.perform(get("/api/comments?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(comment.getId().intValue())))
//            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
//            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(sameInstant(DEFAULT_MODIFY_DATE))))
//            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
//    }
//
//    @Test
//    @Transactional
//    public void getComment() throws Exception {
//        // Initialize the database
//        commentRepository.saveAndFlush(comment);
//
//        // Get the comment
//        restCommentMockMvc.perform(get("/api/comments/{id}", comment.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(comment.getId().intValue()))
//            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
//            .andExpect(jsonPath("$.modifyDate").value(sameInstant(DEFAULT_MODIFY_DATE)))
//            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingComment() throws Exception {
//        // Get the comment
//        restCommentMockMvc.perform(get("/api/comments/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateComment() throws Exception {
//        // Initialize the database
//        commentRepository.saveAndFlush(comment);
//        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
//
//        // Update the comment
//        Comment updatedComment = commentRepository.findOne(comment.getId());
//        updatedComment
//                .createdDate(UPDATED_CREATED_DATE)
//                .modifyDate(UPDATED_MODIFY_DATE)
//                .content(UPDATED_CONTENT);
//
//        restCommentMockMvc.perform(put("/api/comments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedComment)))
//            .andExpect(status().isOk());
//
//        // Validate the Comment in the database
//        List<Comment> commentList = commentRepository.findAll();
//        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
//        Comment testComment = commentList.get(commentList.size() - 1);
//        assertThat(testComment.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
//        assertThat(testComment.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
//        assertThat(testComment.getContent()).isEqualTo(UPDATED_CONTENT);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingComment() throws Exception {
//        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
//
//        // Create the Comment
//
//        // If the entity doesn't have an ID, it will be created instead of just being updated
//        restCommentMockMvc.perform(put("/api/comments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(comment)))
//            .andExpect(status().isCreated());
//
//        // Validate the Comment in the database
//        List<Comment> commentList = commentRepository.findAll();
//        assertThat(commentList).hasSize(databaseSizeBeforeUpdate + 1);
//    }
//
//    @Test
//    @Transactional
//    public void deleteComment() throws Exception {
//        // Initialize the database
//        commentRepository.saveAndFlush(comment);
//        int databaseSizeBeforeDelete = commentRepository.findAll().size();
//
//        // Get the comment
//        restCommentMockMvc.perform(delete("/api/comments/{id}", comment.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<Comment> commentList = commentRepository.findAll();
//        assertThat(commentList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(Comment.class);
//    }
//}
