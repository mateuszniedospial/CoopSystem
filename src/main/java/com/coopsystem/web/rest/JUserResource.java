package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.JUser;

import com.coopsystem.repository.JUserRepository;
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
 * REST controller for managing JUser.
 */
@RestController
@RequestMapping("/api")
public class JUserResource {

    private final Logger log = LoggerFactory.getLogger(JUserResource.class);

    private static final String ENTITY_NAME = "jUser";

    private final JUserRepository jUserRepository;

    public JUserResource(JUserRepository jUserRepository) {
        this.jUserRepository = jUserRepository;
    }

    /**
     * POST  /j-users : Create a new jUser.
     *
     * @param jUser the jUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jUser, or with status 400 (Bad Request) if the jUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/j-users")
    @Timed
    public ResponseEntity<JUser> createJUser(@Valid @RequestBody JUser jUser) throws URISyntaxException {
        log.debug("REST request to save JUser : {}", jUser);
        if (jUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jUser cannot already have an ID")).body(null);
        }
        JUser result = jUserRepository.save(jUser);
        return ResponseEntity.created(new URI("/api/j-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /j-users : Updates an existing jUser.
     *
     * @param jUser the jUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jUser,
     * or with status 400 (Bad Request) if the jUser is not valid,
     * or with status 500 (Internal Server Error) if the jUser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/j-users")
    @Timed
    public ResponseEntity<JUser> updateJUser(@Valid @RequestBody JUser jUser) throws URISyntaxException {
        log.debug("REST request to update JUser : {}", jUser);
        if (jUser.getId() == null) {
            return createJUser(jUser);
        }
        JUser result = jUserRepository.save(jUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /j-users : get all the jUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jUsers in body
     */
    @GetMapping("/j-users")
    @Timed
    public List<JUser> getAllJUsers() {
        log.debug("REST request to get all JUsers");
        List<JUser> jUsers = jUserRepository.findAll();
        return jUsers;
    }

    /**
     * GET  /j-users/:id : get the "id" jUser.
     *
     * @param id the id of the jUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jUser, or with status 404 (Not Found)
     */
    @GetMapping("/j-users/{id}")
    @Timed
    public ResponseEntity<JUser> getJUser(@PathVariable Long id) {
        log.debug("REST request to get JUser : {}", id);
        JUser jUser = jUserRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jUser));
    }

    @GetMapping("/j-users/jhi-users/{id}")
    @Timed
    public ResponseEntity<JUser> getJUserUsingJhiUser(@PathVariable Long id) {
        log.debug("REST request to get JUser : {}", id);
        JUser jUser = jUserRepository.findJUserUsingJhiUser(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jUser));
    }

    @GetMapping("/j-userBjhi")
    @Timed
    public ResponseEntity<JUser> getJUserByJhiId(@RequestParam(value = "jhiId", required = true) Long id) {
        log.debug("REST request to get JUser : {}", id);
        JUser jUser = jUserRepository.findJUserUsingJhiUser(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jUser));
    }

    /**
     * DELETE  /j-users/:id : delete the "id" jUser.
     *
     * @param id the id of the jUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/j-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteJUser(@PathVariable Long id) {
        log.debug("REST request to delete JUser : {}", id);
        jUserRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
