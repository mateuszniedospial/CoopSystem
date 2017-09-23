package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.JGroup;
import com.coopsystem.domain.Sprint;

import com.coopsystem.domain.enumeration.SprintLifeCycle;
import com.coopsystem.repository.SprintRepository;
import com.coopsystem.service.ReportService;
import com.coopsystem.service.TaskService;
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
 * REST controller for managing Sprint.
 */
@RestController
@RequestMapping("/api")
public class SprintResource {

    private final Logger log = LoggerFactory.getLogger(SprintResource.class);

    private static final String ENTITY_NAME = "sprint";

    private final SprintRepository sprintRepository;

    private final ReportService reportService;

    private final TaskService taskService;

    public SprintResource(SprintRepository sprintRepository, ReportService reportService, TaskService taskService) {
        this.sprintRepository = sprintRepository;
        this.reportService = reportService;
        this.taskService = taskService;
    }

    /**
     * POST  /sprints : Create a new sprint.
     *
     * @param sprint the sprint to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sprint, or with status 400 (Bad Request) if the sprint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sprints")
    @Timed
    public ResponseEntity<Sprint> createSprint(@Valid @RequestBody Sprint sprint) throws URISyntaxException {
        log.debug("REST request to save Sprint : {}", sprint);
        if (sprint.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sprint cannot already have an ID")).body(null);
        }
        Sprint result = sprintRepository.save(sprint);
        //fetch previous sprint  -
//        Sprint previousSprint = new Sprint(); previousSprint.setId(null);// change this
        reportService.createNEWReport(sprint,null);
        return ResponseEntity.created(new URI("/api/sprints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sprints : Updates an existing sprint.
     *
     * @param sprint the sprint to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sprint,
     * or with status 400 (Bad Request) if the sprint is not valid,
     * or with status 500 (Internal Server Error) if the sprint couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sprints")
    @Timed
    public ResponseEntity<Sprint> updateSprint(@Valid @RequestBody Sprint sprint) throws URISyntaxException {
        log.debug("REST request to update Sprint : {}", sprint);
        if (sprint.getId() == null) {
            return createSprint(sprint);
        }
        reportService.updateReportIfSprintStarted(sprint, sprintRepository.findOne(sprint.getId()));
        Sprint result = sprintRepository.save(sprint);
        if (sprint.getLifeCycle().equals(SprintLifeCycle.OUTDATED)) {
            taskService.removeTaskFromSprint(sprint.getId());
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sprint.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sprints : get all the sprints.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sprints in body
     */
    @GetMapping("/sprints")
    @Timed
    public List<Sprint> getAllSprints() {
        log.debug("REST request to get all Sprints");
        List<Sprint> sprints = sprintRepository.findAll();
        return sprints;
    }

    @GetMapping("/sprints/byJGroupId")
    @Timed
    public List<Sprint> getActiveSprintsByGroupId(@RequestParam(value = "query", required = false)  Long jGroupId) {
        log.debug("REST request to get Sprints by group id");
        List<Sprint> sprints = sprintRepository.findSprintsByJGroupIdAndLifeCycleOrderByCreatedDateDesc(jGroupId, SprintLifeCycle.ACTIVE);
        List<Sprint> sprints1 = sprintRepository.findSprintsByJGroupIdAndLifeCycleOrderByCreatedDateDesc(jGroupId, SprintLifeCycle.FUTURE);
        sprints.addAll(sprints1);
        clearJGroupInResponse(sprints);
        return sprints;
    }

    private void clearJGroupInResponse(List<Sprint> sprints) {
        sprints.forEach((sprint)->{
            JGroup jGroup = new JGroup();
            jGroup.setId(sprint.getJGroup().getId());
            sprint.setJGroup(jGroup);
        });
    }


    /**
     * GET  /sprints/:id : get the "id" sprint.
     *
     * @param id the id of the sprint to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sprint, or with status 404 (Not Found)
     */
    @GetMapping("/sprints/{id}")
    @Timed
    public ResponseEntity<Sprint> getSprint(@PathVariable Long id) {
        log.debug("REST request to get Sprint : {}", id);
        Sprint sprint = sprintRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sprint));
    }

    /**
     * DELETE  /sprints/:id : delete the "id" sprint.
     *
     * @param id the id of the sprint to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sprints/{id}")
    @Timed
    public ResponseEntity<Void> deleteSprint(@PathVariable Long id) {
        log.debug("REST request to delete Sprint : {}", id);
        sprintRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
