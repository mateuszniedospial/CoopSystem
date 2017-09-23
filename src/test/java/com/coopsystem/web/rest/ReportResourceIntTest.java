//package com.myjir.web.rest;
//
//import com.myjir.CoopApp;
//
//import com.myjir.domain.Report;
//import com.myjir.repository.ReportRepository;
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
// * Test class for the ReportResource REST controller.
// *
// * @see ReportResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = CoopApp.class)
//public class ReportResourceIntTest {
//
//    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final ZonedDateTime DEFAULT_MODIFY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_MODIFY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final byte[] DEFAULT_DATA = TestUtil.createByteArray(1, "0");
//    private static final byte[] UPDATED_DATA = TestUtil.createByteArray(2, "1");
//    private static final String DEFAULT_DATA_CONTENT_TYPE = "image/jpg";
//    private static final String UPDATED_DATA_CONTENT_TYPE = "image/png";
//
//    @Autowired
//    private ReportRepository reportRepository;
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
//    private MockMvc restReportMockMvc;
//
//    private Report report;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//            ReportResource reportResource = new ReportResource(reportRepository);
//        this.restReportMockMvc = MockMvcBuilders.standaloneSetup(reportResource)
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
//    public static Report createEntity(EntityManager em) {
//        Report report = new Report()
//                .createdDate(DEFAULT_CREATED_DATE)
//                .modifyDate(DEFAULT_MODIFY_DATE)
//                .data(DEFAULT_DATA)
//                .dataContentType(DEFAULT_DATA_CONTENT_TYPE);
//        return report;
//    }
//
//    @Before
//    public void initTest() {
//        report = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createNEWReport() throws Exception {
//        int databaseSizeBeforeCreate = reportRepository.findAll().size();
//
//        // Create the Report
//
//        restReportMockMvc.perform(post("/api/reports")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(report)))
//            .andExpect(status().isCreated());
//
//        // Validate the Report in the database
//        List<Report> reportList = reportRepository.findAll();
//        assertThat(reportList).hasSize(databaseSizeBeforeCreate + 1);
//        Report testReport = reportList.get(reportList.size() - 1);
//        assertThat(testReport.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
//        assertThat(testReport.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
//        assertThat(testReport.getData()).isEqualTo(DEFAULT_DATA);
//        assertThat(testReport.getDataContentType()).isEqualTo(DEFAULT_DATA_CONTENT_TYPE);
//    }
//
//    @Test
//    @Transactional
//    public void createReportWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = reportRepository.findAll().size();
//
//        // Create the Report with an existing ID
//        Report existingReport = new Report();
//        existingReport.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restReportMockMvc.perform(post("/api/reports")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(existingReport)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Alice in the database
//        List<Report> reportList = reportRepository.findAll();
//        assertThat(reportList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void getAllReports() throws Exception {
//        // Initialize the database
//        reportRepository.saveAndFlush(report);
//
//        // Get all the reportList
//        restReportMockMvc.perform(get("/api/reports?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
//            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
//            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(sameInstant(DEFAULT_MODIFY_DATE))))
//            .andExpect(jsonPath("$.[*].dataContentType").value(hasItem(DEFAULT_DATA_CONTENT_TYPE)))
//            .andExpect(jsonPath("$.[*].data").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA))));
//    }
//
//    @Test
//    @Transactional
//    public void getReport() throws Exception {
//        // Initialize the database
//        reportRepository.saveAndFlush(report);
//
//        // Get the report
//        restReportMockMvc.perform(get("/api/reports/{id}", report.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(report.getId().intValue()))
//            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
//            .andExpect(jsonPath("$.modifyDate").value(sameInstant(DEFAULT_MODIFY_DATE)))
//            .andExpect(jsonPath("$.dataContentType").value(DEFAULT_DATA_CONTENT_TYPE))
//            .andExpect(jsonPath("$.data").value(Base64Utils.encodeToString(DEFAULT_DATA)));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingReport() throws Exception {
//        // Get the report
//        restReportMockMvc.perform(get("/api/reports/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateReport() throws Exception {
//        // Initialize the database
//        reportRepository.saveAndFlush(report);
//        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
//
//        // Update the report
//        Report updatedReport = reportRepository.findOne(report.getId());
//        updatedReport
//                .createdDate(UPDATED_CREATED_DATE)
//                .modifyDate(UPDATED_MODIFY_DATE)
//                .data(UPDATED_DATA)
//                .dataContentType(UPDATED_DATA_CONTENT_TYPE);
//
//        restReportMockMvc.perform(put("/api/reports")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedReport)))
//            .andExpect(status().isOk());
//
//        // Validate the Report in the database
//        List<Report> reportList = reportRepository.findAll();
//        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
//        Report testReport = reportList.get(reportList.size() - 1);
//        assertThat(testReport.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
//        assertThat(testReport.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
//        assertThat(testReport.getData()).isEqualTo(UPDATED_DATA);
//        assertThat(testReport.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingReport() throws Exception {
//        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
//
//        // Create the Report
//
//        // If the entity doesn't have an ID, it will be created instead of just being updated
//        restReportMockMvc.perform(put("/api/reports")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(report)))
//            .andExpect(status().isCreated());
//
//        // Validate the Report in the database
//        List<Report> reportList = reportRepository.findAll();
//        assertThat(reportList).hasSize(databaseSizeBeforeUpdate + 1);
//    }
//
//    @Test
//    @Transactional
//    public void deleteReport() throws Exception {
//        // Initialize the database
//        reportRepository.saveAndFlush(report);
//        int databaseSizeBeforeDelete = reportRepository.findAll().size();
//
//        // Get the report
//        restReportMockMvc.perform(delete("/api/reports/{id}", report.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<Report> reportList = reportRepository.findAll();
//        assertThat(reportList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(Report.class);
//    }
//}
