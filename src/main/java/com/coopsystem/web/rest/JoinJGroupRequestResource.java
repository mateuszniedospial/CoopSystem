package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.JoinJGroupRequest;

import com.coopsystem.repository.JoinJGroupRequestRepository;
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
 * REST controller for managing JoinJGroupRequest.
 */
@RestController
@RequestMapping("/api")
public class JoinJGroupRequestResource {

    private final Logger log = LoggerFactory.getLogger(JoinJGroupRequestResource.class);

    private static final String ENTITY_NAME = "joinJGroupRequest";

    private final JoinJGroupRequestRepository joinJGroupRequestRepository;

    public JoinJGroupRequestResource(JoinJGroupRequestRepository joinJGroupRequestRepository) {
        this.joinJGroupRequestRepository = joinJGroupRequestRepository;
    }

    /**
     * POST  /join-j-group-requests : Create a new joinJGroupRequest.
     *
     * @param joinJGroupRequest the joinJGroupRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new joinJGroupRequest, or with status 400 (Bad Request) if the joinJGroupRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/join-j-group-requests")
    @Timed
    public ResponseEntity<JoinJGroupRequest> createJoinJGroupRequest(@Valid @RequestBody JoinJGroupRequest joinJGroupRequest) throws URISyntaxException {
        log.debug("REST request to save JoinJGroupRequest : {}", joinJGroupRequest);
        if (joinJGroupRequest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new joinJGroupRequest cannot already have an ID")).body(null);
        }
        JoinJGroupRequest result = joinJGroupRequestRepository.save(joinJGroupRequest);
        return ResponseEntity.created(new URI("/api/join-j-group-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /join-j-group-requests : Updates an existing joinJGroupRequest.
     *
     * @param joinJGroupRequest the joinJGroupRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated joinJGroupRequest,
     * or with status 400 (Bad Request) if the joinJGroupRequest is not valid,
     * or with status 500 (Internal Server Error) if the joinJGroupRequest couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/join-j-group-requests")
    @Timed
    public ResponseEntity<JoinJGroupRequest> updateJoinJGroupRequest(@RequestBody JoinJGroupRequest joinJGroupRequest) throws URISyntaxException {
        log.debug("REST request to update JoinJGroupRequest : {}", joinJGroupRequest);
        if (joinJGroupRequest.getId() == null) {
            return createJoinJGroupRequest(joinJGroupRequest);
        }
        JoinJGroupRequest result = joinJGroupRequestRepository.save(joinJGroupRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, joinJGroupRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /join-j-group-requests : get all the joinJGroupRequests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of joinJGroupRequests in body
     */
    @GetMapping("/join-j-group-requests")
    @Timed
    public List<JoinJGroupRequest> getAllJoinJGroupRequests() {
        log.debug("REST request to get all JoinJGroupRequests");
        List<JoinJGroupRequest> joinJGroupRequests = joinJGroupRequestRepository.findAll();
        return joinJGroupRequests;
    }

    @GetMapping("/join-j-group-requests/jgroup/{id}")
    @Timed
    public List<JoinJGroupRequest> getAllRequestsToSpecificJGroup(@PathVariable Long id) {
        log.debug("REST request to get all JoinJGroupRequests to specific Group of id: ", id);
        return joinJGroupRequestRepository.findAllRequestsToGroup(id);
    }
    /**
     * GET  /join-j-group-requests/:id : get the "id" joinJGroupRequest.
     *
     * @param id the id of the joinJGroupRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the joinJGroupRequest, or with status 404 (Not Found)
     */
    @GetMapping("/join-j-group-requests/{id}")
    @Timed
    public ResponseEntity<JoinJGroupRequest> getJoinJGroupRequest(@PathVariable Long id) {
        log.debug("REST request to get JoinJGroupRequest : {}", id);
        JoinJGroupRequest joinJGroupRequest = joinJGroupRequestRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(joinJGroupRequest));
    }

    /**
     * DELETE  /join-j-group-requests/:id : delete the "id" joinJGroupRequest.
     *
     * @param id the id of the joinJGroupRequest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/join-j-group-requests/{id}")
    @Timed
    public ResponseEntity<Void> deleteJoinJGroupRequest(@PathVariable Long id) {
        log.debug("REST request to delete JoinJGroupRequest : {}", id);
        joinJGroupRequestRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
