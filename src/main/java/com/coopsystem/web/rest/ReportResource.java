package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.Report;

import com.coopsystem.domain.ReportContent;
import com.coopsystem.repository.ReportRepository;
import com.coopsystem.repository.TaskRepository;
import com.coopsystem.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Report.
 */
@RestController
@RequestMapping("/api")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    private static final String ENTITY_NAME = "report";

    private final ReportRepository reportRepository;
    private final TaskRepository taskRepository;

    public ReportResource(ReportRepository reportRepository, TaskRepository taskRepository) {
        this.reportRepository = reportRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * POST  /reports : Create a new report.
     *
     * @param report the report to create
     * @return the ResponseEntity with status 201 (Created) and with body the new report, or with status 400 (Bad Request) if the report has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reports")
    @Timed
    public ResponseEntity<Report> createReport(@RequestBody Report report) throws URISyntaxException {
        log.debug("REST request to save Report : {}", report);
        if (report.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new report cannot already have an ID")).body(null);
        }
        report.setData(report.getReportContent().serializable());
        Report result = reportRepository.save(report);
        return ResponseEntity.created(new URI("/api/reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reports : Updates an existing report.
     *
     * @param report the report to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated report,
     * or with status 400 (Bad Request) if the report is not valid,
     * or with status 500 (Internal Server Error) if the report couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reports")
    @Timed
    public ResponseEntity<Report> updateReport(@RequestBody Report report) throws URISyntaxException {
        log.debug("REST request to update Report : {}", report);
        if (report.getId() == null) {
            return createReport(report);
        }
        report.setData(report.getReportContent().serializable());
        Report result = reportRepository.save(report);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, report.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reports : get all the reports.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reports in body
     */
    @GetMapping("/reports")
    @Timed
    public List<Report> getAllReports() {
        log.debug("REST request to get all Reports");
        List<Report> reports = reportRepository.findAll();
        reports.forEach(it -> {
            it.setReportContent(ReportContent.deserializable(it.getData()));
        });
        reports.forEach(report -> {
            if (report.getReportContent().getTasksIdAddedInSprint() != null)
                report.getReportContent().getTasksIdAddedInSprint().forEach(it -> {
                    report.getReportContent().getTasksAddedInSprint().add(taskRepository.findOne(it));
                });
            if (report.getReportContent().getTasksIdDone() != null)
                report.getReportContent().getTasksIdDone().forEach(it -> {
                    report.getReportContent().getTasksDone().add(taskRepository.findOne(it));
                });
            if (report.getReportContent().getTasksIdUnDone() != null)
                report.getReportContent().getTasksIdUnDone().forEach(it -> {
                    report.getReportContent().getTasksUnDone().add(taskRepository.findOne(it));
                });
        });
        return reports;
    }

    /**
     * GET  /reports/:id : get the "id" report.
     *
     * @param id the id of the report to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the report, or with status 404 (Not Found)
     */
    @GetMapping("/reports/{id}")
    @Timed
    public ResponseEntity<Report> getReport(@PathVariable Long id) {
        log.debug("REST request to get Report : {}", id);
        Report report = reportRepository.findOne(id);
        if(report != null){
            report.setReportContent(ReportContent.deserializable(report.getData()));
            if (report.getReportContent().getTasksIdAddedInSprint() != null)
                report.getReportContent().getTasksIdAddedInSprint().forEach(it -> {
                    report.getReportContent().getTasksAddedInSprint().add(taskRepository.findOne(it));
                });
            if (report.getReportContent().getTasksIdDone() != null)
                report.getReportContent().getTasksIdDone().forEach(it -> {
                    report.getReportContent().getTasksDone().add(taskRepository.findOne(it));
                });
            if (report.getReportContent().getTasksIdUnDone() != null)
                report.getReportContent().getTasksIdUnDone().forEach(it -> {
                    report.getReportContent().getTasksUnDone().add(taskRepository.findOne(it));
                });
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(report));
    }

    /**
     * DELETE  /reports/:id : delete the "id" report.
     *
     * @param id the id of the report to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        log.debug("REST request to delete Report : {}", id);
        reportRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/reports/getBySprintId")
    @Timed
    public ResponseEntity<Report> getReportBySprintId(@RequestParam(value = "query", required = false) Long sprintId) {
        log.debug("REST request to get Report BY SPRINTID : {}", sprintId);
        Report report = reportRepository.findOneBySprintId(sprintId);
        if(report != null){
            report.setReportContent(ReportContent.deserializable(report.getData()));
            if (report.getReportContent().getTasksIdAddedInSprint() != null)
                report.getReportContent().getTasksIdAddedInSprint().forEach(it -> {
                    report.getReportContent().getTasksAddedInSprint().add(taskRepository.findOne(it));
                });
            if (report.getReportContent().getTasksIdDone() != null)
                report.getReportContent().getTasksIdDone().forEach(it -> {
                    report.getReportContent().getTasksDone().add(taskRepository.findOne(it));
                });
            if (report.getReportContent().getTasksIdUnDone() != null)
                report.getReportContent().getTasksIdUnDone().forEach(it -> {
                    report.getReportContent().getTasksUnDone().add(taskRepository.findOne(it));
                });
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(report));
    }
}
