package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.WorkLog;

import com.coopsystem.repository.WorkLogRepository;
import com.coopsystem.service.ReportService;
import com.coopsystem.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WorkLog.
 */
@RestController
@RequestMapping("/api")
public class WorkLogResource {

    private final Logger log = LoggerFactory.getLogger(WorkLogResource.class);

    private static final String ENTITY_NAME = "workLog";

    private final WorkLogRepository workLogRepository;

    private final ReportService reportService;

    public WorkLogResource(WorkLogRepository workLogRepository, ReportService reportService) {
        this.workLogRepository = workLogRepository;
        this.reportService = reportService;
    }

    /**
     * POST  /work-logs : Create a new workLog.
     *
     * @param workLog the workLog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workLog, or with status 400 (Bad Request) if the workLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/work-logs")
    @Timed
    public ResponseEntity<WorkLog> createWorkLog(@Valid @RequestBody WorkLog workLog) throws URISyntaxException {
        log.debug("REST request to save WorkLog : {}", workLog);
        if (workLog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new workLog cannot already have an ID")).body(null);
        }
        reportService.updateWorkLogs(workLog);
        WorkLog result = workLogRepository.save(workLog);
        return ResponseEntity.created(new URI("/api/work-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-logs : Updates an existing workLog.
     *
     * @param workLog the workLog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workLog,
     * or with status 400 (Bad Request) if the workLog is not valid,
     * or with status 500 (Internal Server Error) if the workLog couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/work-logs")
    @Timed
    public ResponseEntity<WorkLog> updateWorkLog(@Valid @RequestBody WorkLog workLog) throws URISyntaxException {
        log.debug("REST request to update WorkLog : {}", workLog);
        if (workLog.getId() == null) {
            return createWorkLog(workLog);
        }
        WorkLog result = workLogRepository.save(workLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, workLog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-logs : get all the workLogs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of workLogs in body
     */
    @GetMapping("/work-logs")
    @Timed
    public List<WorkLog> getAllWorkLogs() {
        log.debug("REST request to get all WorkLogs");
        List<WorkLog> workLogs = workLogRepository.findAll();
        return workLogs;
    }

    @GetMapping("/work-logs/byTaskId")
    @Timed
    public List<WorkLog> getWorkLogsByTaskId(@RequestParam(value = "query", required = false)  Long taskId) {
        log.debug("REST request to get WorkLogs by taskId");
        List<WorkLog> workLogs = workLogRepository.findWorkLogByTaskId(taskId);
        workLogs.forEach(it->{
         it.setTask(null);
        });
        return workLogs;
    }

    @GetMapping("/work-logs/byJUserId")
    @Timed
    public List<WorkLog> getWorkLogsByJUserId(@RequestParam(value = "query", required = false)  Long jUserId) {
        log.debug("REST request to get WorkLogs by taskId");
        List<WorkLog> workLogs = workLogRepository.findWorkLogByJuserId(jUserId);
        return workLogs;
    }

    /**
     * GET  /work-logs/:id : get the "id" workLog.
     *
     * @param id the id of the workLog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workLog, or with status 404 (Not Found)
     */
    @GetMapping("/work-logs/{id}")
    @Timed
    public ResponseEntity<WorkLog> getWorkLog(@PathVariable Long id) {
        log.debug("REST request to get WorkLog : {}", id);
        WorkLog workLog = workLogRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(workLog));
    }

    /**
     * DELETE  /work-logs/:id : delete the "id" workLog.
     *
     * @param id the id of the workLog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/work-logs/{id}")
    @Timed
    public ResponseEntity<Void> deleteWorkLog(@PathVariable Long id) {
        log.debug("REST request to delete WorkLog : {}", id);
        workLogRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
