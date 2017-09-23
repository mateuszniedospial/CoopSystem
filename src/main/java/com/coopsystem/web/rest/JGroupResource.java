package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.JGroup;

import com.coopsystem.repository.JGroupRepository;
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
 * REST controller for managing JGroup.
 */
@RestController
@RequestMapping("/api")
public class JGroupResource {

    private final Logger log = LoggerFactory.getLogger(JGroupResource.class);

    private static final String ENTITY_NAME = "jGroup";

    private final JGroupRepository jGroupRepository;

    public JGroupResource(JGroupRepository jGroupRepository) {
        this.jGroupRepository = jGroupRepository;
    }

    /**
     * POST  /j-groups : Create a new jGroup.
     *
     * @param jGroup the jGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jGroup, or with status 400 (Bad Request) if the jGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/j-groups")
    @Timed
    public ResponseEntity<JGroup> createJGroup(@Valid @RequestBody JGroup jGroup) throws URISyntaxException {
        log.debug("REST request to save JGroup : {}", jGroup);
        if (jGroup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jGroup cannot already have an ID")).body(null);
        }
        JGroup result = jGroupRepository.save(jGroup);
        return ResponseEntity.created(new URI("/api/j-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /j-groups : Updates an existing jGroup.
     *
     * @param jGroup the jGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jGroup,
     * or with status 400 (Bad Request) if the jGroup is not valid,
     * or with status 500 (Internal Server Error) if the jGroup couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/j-groups")
    @Timed
    public ResponseEntity<JGroup> updateJGroup(@Valid @RequestBody JGroup jGroup) throws URISyntaxException {
        log.debug("REST request to update JGroup : {}", jGroup);
        if (jGroup.getId() == null) {
            return createJGroup(jGroup);
        }
        JGroup result = jGroupRepository.save(jGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /j-groups : get all the jGroups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jGroups in body
     */
    @GetMapping("/j-groups")
    @Timed
    public List<JGroup> getAllJGroups() {
        log.debug("REST request to get all JGroups");
        List<JGroup> jGroups = jGroupRepository.findAllWithEagerRelationships();
        return jGroups;
    }

    @GetMapping("/j-groups/byJUserId")
    @Timed
    public List<JGroup> getGroupsByJUserId(@RequestParam(value = "query", required = false)  Long juserId) {
        log.debug("REST request to get all JGroups");
        List<JGroup> jGroups = jGroupRepository.findJGroupsByJUserId(juserId);
        return jGroups;
    }
    /**
     * GET  /j-groups/:id : get the "id" jGroup.
     *
     * @param id the id of the jGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jGroup, or with status 404 (Not Found)
     */
    @GetMapping("/j-groups/{id}")
    @Timed
    public ResponseEntity<JGroup> getJGroup(@PathVariable Long id) {
        log.debug("REST request to get JGroup : {}", id);
        JGroup jGroup = jGroupRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jGroup));
    }

    @GetMapping("/j-groups/j-users/{id}")
    @Timed
    public List<JGroup> getJUserJGroups(@PathVariable Long id) {
        log.debug("REST request to get JGroup : {}", id);
        List<JGroup> jGroups = jGroupRepository.findAllJUserJGroups(id);
        return jGroups;
    }

    /**
     * DELETE  /j-groups/:id : delete the "id" jGroup.
     *
     * @param id the id of the jGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/j-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteJGroup(@PathVariable Long id) {
        log.debug("REST request to delete JGroup : {}", id);
        jGroupRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
