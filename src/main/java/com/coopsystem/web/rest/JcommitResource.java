package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Strings;
import com.coopsystem.domain.Jcommit;

import com.coopsystem.repository.JcommitRepository;
import com.coopsystem.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Jcommit.
 */
@RestController
@RequestMapping("/api")
public class JcommitResource {

    private final Logger log = LoggerFactory.getLogger(JcommitResource.class);

    private static final String ENTITY_NAME = "jcommit";

    private final JcommitRepository jcommitRepository;

    public JcommitResource(JcommitRepository jcommitRepository) {
        this.jcommitRepository = jcommitRepository;
    }

    /**
     * POST  /jcommits : Create a new jcommit.
     *
     * @param jcommit the jcommit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jcommit, or with status 400 (Bad Request) if the jcommit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/jcommits")
    @Timed
    public ResponseEntity<Jcommit> createJcommit(@RequestBody Jcommit jcommit) throws URISyntaxException {
        log.debug("REST request to save Jcommit : {}", jcommit);
        if (jcommit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jcommit cannot already have an ID")).body(null);
        }
        Jcommit result = jcommitRepository.save(jcommit);
        return ResponseEntity.created(new URI("/api/jcommits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jcommits : Updates an existing jcommit.
     *
     * @param jcommit the jcommit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jcommit,
     * or with status 400 (Bad Request) if the jcommit is not valid,
     * or with status 500 (Internal Server Error) if the jcommit couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/jcommits")
    @Timed
    public ResponseEntity<Jcommit> updateJcommit(@RequestBody Jcommit jcommit) throws URISyntaxException {
        log.debug("REST request to update Jcommit : {}", jcommit);
        if (jcommit.getId() == null) {
            return createJcommit(jcommit);
        }
        Jcommit result = jcommitRepository.save(jcommit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jcommit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jcommits : get all the jcommits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jcommits in body
     */
    @GetMapping("/jcommits")
    @Timed
    public List<Jcommit> getAllJcommits() {
        log.debug("REST request to get all Jcommits");
        List<Jcommit> jcommits = jcommitRepository.findAllWithEagerRelationships();
        return jcommits;
    }

    @GetMapping("/jcommits/byTasks")
    @Timed
    public List<Jcommit> getJcommitByTaskId(@RequestParam(value = "query", required = false)  Long taskId) {
        log.debug("REST request to get all Jcommits");
        List<Jcommit> jcommits = jcommitRepository.findByTasksOrderByCommitDateDesc(taskId);

        jcommits.forEach(jcommit -> {
            jcommit.setAddedList(genn(jcommit.getAdded()));
            jcommit.setRemovedList(genn(jcommit.getRemoved()));
            jcommit.setModifiedList(genn(jcommit.getModified()));
        });
        //clear tasks
        jcommits.forEach(jcommit -> {
            jcommit.setTasks(null);
        });
        return jcommits;
    }

    private List<String> genn(String in) {
        in = in.substring(1, in.length()-1);
        if(Strings.isNullOrEmpty(in)){
            return null;
        }
        return Arrays.asList(in.split(","));
    }

    /**
     * GET  /jcommits/:id : get the "id" jcommit.
     *
     * @param id the id of the jcommit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jcommit, or with status 404 (Not Found)
     */
    @GetMapping("/jcommits/{id}")
    @Timed
    public ResponseEntity<Jcommit> getJcommit(@PathVariable Long id) {
        log.debug("REST request to get Jcommit : {}", id);
        Jcommit jcommit = jcommitRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jcommit));
    }

    /**
     * DELETE  /jcommits/:id : delete the "id" jcommit.
     *
     * @param id the id of the jcommit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/jcommits/{id}")
    @Timed
    public ResponseEntity<Void> deleteJcommit(@PathVariable Long id) {
        log.debug("REST request to delete Jcommit : {}", id);
        jcommitRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
