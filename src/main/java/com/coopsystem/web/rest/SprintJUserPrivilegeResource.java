package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.SprintJUserPrivilege;

import com.coopsystem.repository.SprintJUserPrivilegeRepository;
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
 * REST controller for managing SprintJUserPrivilege.
 */
@RestController
@RequestMapping("/api")
public class SprintJUserPrivilegeResource {

    private final Logger log = LoggerFactory.getLogger(SprintJUserPrivilegeResource.class);

    private static final String ENTITY_NAME = "sprintJUserPrivilege";

    private final SprintJUserPrivilegeRepository sprintJUserPrivilegeRepository;

    public SprintJUserPrivilegeResource(SprintJUserPrivilegeRepository sprintJUserPrivilegeRepository) {
        this.sprintJUserPrivilegeRepository = sprintJUserPrivilegeRepository;
    }

    /**
     * POST  /sprint-j-user-privileges : Create a new sprintJUserPrivilege.
     *
     * @param sprintJUserPrivilege the sprintJUserPrivilege to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sprintJUserPrivilege, or with status 400 (Bad Request) if the sprintJUserPrivilege has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sprint-j-user-privileges")
    @Timed
    public ResponseEntity<SprintJUserPrivilege> createSprintJUserPrivilege(@Valid @RequestBody SprintJUserPrivilege sprintJUserPrivilege) throws URISyntaxException {
        log.debug("REST request to save SprintJUserPrivilege : {}", sprintJUserPrivilege);
        if (sprintJUserPrivilege.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sprintJUserPrivilege cannot already have an ID")).body(null);
        }
        SprintJUserPrivilege result = sprintJUserPrivilegeRepository.save(sprintJUserPrivilege);
        return ResponseEntity.created(new URI("/api/sprint-j-user-privileges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sprint-j-user-privileges : Updates an existing sprintJUserPrivilege.
     *
     * @param sprintJUserPrivilege the sprintJUserPrivilege to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sprintJUserPrivilege,
     * or with status 400 (Bad Request) if the sprintJUserPrivilege is not valid,
     * or with status 500 (Internal Server Error) if the sprintJUserPrivilege couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sprint-j-user-privileges")
    @Timed
    public ResponseEntity<SprintJUserPrivilege> updateSprintJUserPrivilege(@Valid @RequestBody SprintJUserPrivilege sprintJUserPrivilege) throws URISyntaxException {
        log.debug("REST request to update SprintJUserPrivilege : {}", sprintJUserPrivilege);
        if (sprintJUserPrivilege.getId() == null) {
            return createSprintJUserPrivilege(sprintJUserPrivilege);
        }
        SprintJUserPrivilege result = sprintJUserPrivilegeRepository.save(sprintJUserPrivilege);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sprintJUserPrivilege.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sprint-j-user-privileges : get all the sprintJUserPrivileges.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sprintJUserPrivileges in body
     */
    @GetMapping("/sprint-j-user-privileges")
    @Timed
    public List<SprintJUserPrivilege> getAllSprintJUserPrivileges() {
        log.debug("REST request to get all SprintJUserPrivileges");
        List<SprintJUserPrivilege> sprintJUserPrivileges = sprintJUserPrivilegeRepository.findAll();
        return sprintJUserPrivileges;
    }

    /**
     * GET  /sprint-j-user-privileges/:id : get the "id" sprintJUserPrivilege.
     *
     * @param id the id of the sprintJUserPrivilege to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sprintJUserPrivilege, or with status 404 (Not Found)
     */
    @GetMapping("/sprint-j-user-privileges/{id}")
    @Timed
    public ResponseEntity<SprintJUserPrivilege> getSprintJUserPrivilege(@PathVariable Long id) {
        log.debug("REST request to get SprintJUserPrivilege : {}", id);
        SprintJUserPrivilege sprintJUserPrivilege = sprintJUserPrivilegeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sprintJUserPrivilege));
    }

    /**
     * DELETE  /sprint-j-user-privileges/:id : delete the "id" sprintJUserPrivilege.
     *
     * @param id the id of the sprintJUserPrivilege to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sprint-j-user-privileges/{id}")
    @Timed
    public ResponseEntity<Void> deleteSprintJUserPrivilege(@PathVariable Long id) {
        log.debug("REST request to delete SprintJUserPrivilege : {}", id);
        sprintJUserPrivilegeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
