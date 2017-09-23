package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.Poker;

import com.coopsystem.repository.PokerRepository;
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
 * REST controller for managing Poker.
 */
@RestController
@RequestMapping("/api")
public class PokerResource {

    private final Logger log = LoggerFactory.getLogger(PokerResource.class);

    private static final String ENTITY_NAME = "poker";

    private final PokerRepository pokerRepository;

    public PokerResource(PokerRepository pokerRepository) {
        this.pokerRepository = pokerRepository;
    }

    /**
     * POST  /pokers : Create a new poker.
     *
     * @param poker the poker to create
     * @return the ResponseEntity with status 201 (Created) and with body the new poker, or with status 400 (Bad Request) if the poker has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pokers")
    @Timed
    public ResponseEntity<Poker> createPoker(@RequestBody Poker poker) throws URISyntaxException {
        log.debug("REST request to save Poker : {}", poker);
        if (poker.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new poker cannot already have an ID")).body(null);
        }
        Poker result = pokerRepository.save(poker);
        return ResponseEntity.created(new URI("/api/pokers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pokers : Updates an existing poker.
     *
     * @param poker the poker to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated poker,
     * or with status 400 (Bad Request) if the poker is not valid,
     * or with status 500 (Internal Server Error) if the poker couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pokers")
    @Timed
    public ResponseEntity<Poker> updatePoker(@RequestBody Poker poker) throws URISyntaxException {
        log.debug("REST request to update Poker : {}", poker);
        if (poker.getId() == null) {
            return createPoker(poker);
        }
        Poker result = pokerRepository.save(poker);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, poker.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pokers : get all the pokers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pokers in body
     */
    @GetMapping("/pokers")
    @Timed
    public List<Poker> getAllPokers() {
        log.debug("REST request to get all Pokers");
        List<Poker> pokers = pokerRepository.findAll();
        return pokers;
    }

    @GetMapping("/pokers/byTaskId")
    @Timed
    public ResponseEntity<Poker> getPokerByTaskId(@RequestParam(value = "query", required = false)  Long taskID) {
        log.debug("REST request to get all Pokers");
        Poker poker = pokerRepository.findTaskByTaskId(taskID);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(poker));
    }

    /**
     * GET  /pokers/:id : get the "id" poker.
     *
     * @param id the id of the poker to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the poker, or with status 404 (Not Found)
     */
    @GetMapping("/pokers/{id}")
    @Timed
    public ResponseEntity<Poker> getPoker(@PathVariable Long id) {
        log.debug("REST request to get Poker : {}", id);
        Poker poker = pokerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(poker));
    }

    /**
     * DELETE  /pokers/:id : delete the "id" poker.
     *
     * @param id the id of the poker to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pokers/{id}")
    @Timed
    public ResponseEntity<Void> deletePoker(@PathVariable Long id) {
        log.debug("REST request to delete Poker : {}", id);
        pokerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
