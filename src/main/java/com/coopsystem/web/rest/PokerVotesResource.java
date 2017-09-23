package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.PokerVotes;

import com.coopsystem.repository.PokerVotesRepository;
import com.coopsystem.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing PokerVotes.
 */
@RestController
@RequestMapping("/api")
public class PokerVotesResource {

    private final Logger log = LoggerFactory.getLogger(PokerVotesResource.class);

    private static final String ENTITY_NAME = "pokerVotes";

    private final PokerVotesRepository pokerVotesRepository;

    public PokerVotesResource(PokerVotesRepository pokerVotesRepository) {
        this.pokerVotesRepository = pokerVotesRepository;
    }

    /**
     * POST  /poker-votes : Create a new pokerVotes.
     *
     * @param pokerVotes the pokerVotes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pokerVotes, or with status 400 (Bad Request) if the pokerVotes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/poker-votes")
    @Timed
    public ResponseEntity<PokerVotes> createPokerVotes(@RequestBody PokerVotes pokerVotes) throws URISyntaxException {
        log.debug("REST request to save PokerVotes : {}", pokerVotes);
        Collection<PokerVotes> votes = getJUserVoted(pokerVotes);
        if (pokerVotes.getId() != null || !votes.isEmpty() ) {
            pokerVotes.setId(votes.iterator().next().getId());
            return updatePokerVotes(pokerVotes);
        }
        PokerVotes result = pokerVotesRepository.save(pokerVotes);
        return ResponseEntity.created(new URI("/api/poker-votes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private Collection<PokerVotes> getJUserVoted(PokerVotes pokerVotes) {
        return pokerVotesRepository.findPokerVotesByPokerId(pokerVotes.getPoker().getId())
                .stream()
                    .filter((it)->
                         it.getJUser().getId().equals(pokerVotes.getJUser().getId()))
            .collect(Collectors.toList());
    }

    /**
     * PUT  /poker-votes : Updates an existing pokerVotes.
     *
     * @param pokerVotes the pokerVotes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pokerVotes,
     * or with status 400 (Bad Request) if the pokerVotes is not valid,
     * or with status 500 (Internal Server Error) if the pokerVotes couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/poker-votes")
    @Timed
    public ResponseEntity<PokerVotes> updatePokerVotes(@RequestBody PokerVotes pokerVotes) throws URISyntaxException {
        log.debug("REST request to update PokerVotes : {}", pokerVotes);
        if (pokerVotes.getId() == null) {
            return createPokerVotes(pokerVotes);
        }
        PokerVotes result = pokerVotesRepository.save(pokerVotes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pokerVotes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /poker-votes : get all the pokerVotes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pokerVotes in body
     */
    @GetMapping("/poker-votes")
    @Timed
    public List<PokerVotes> getAllPokerVotes() {
        log.debug("REST request to get all PokerVotes");
        List<PokerVotes> pokerVotes = pokerVotesRepository.findAll();
        return pokerVotes;
    }

    /**
     * GET  /poker-votes/:id : get the "id" pokerVotes.
     *
     * @param id the id of the pokerVotes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pokerVotes, or with status 404 (Not Found)
     */
    @GetMapping("/poker-votes/{id}")
    @Timed
    public ResponseEntity<PokerVotes> getPokerVotes(@PathVariable Long id) {
        log.debug("REST request to get PokerVotes : {}", id);
        PokerVotes pokerVotes = pokerVotesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pokerVotes));
    }

    @GetMapping("/poker-votes/byPokerId")
    @Timed
    public List<PokerVotes> getPokerVotesByPokerID(@RequestParam(value = "query", required = false)  Long pokerID) {
        log.debug("REST request to get PokerVotes by pokerID", pokerID);
        List<PokerVotes> pokerVotesList = pokerVotesRepository.findPokerVotesByPokerId(pokerID);
        pokerVotesList.forEach((vote)->{
            vote.setPoker(null);
        });
        return pokerVotesList;
    }
    /**
     * DELETE  /poker-votes/:id : delete the "id" pokerVotes.
     *
     * @param id the id of the pokerVotes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/poker-votes/{id}")
    @Timed
    public ResponseEntity<Void> deletePokerVotes(@PathVariable Long id) {
        log.debug("REST request to delete PokerVotes : {}", id);
        pokerVotesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @DeleteMapping("/poker-votes/byPokerId")
    @Timed
    public ResponseEntity<Void> deletePokerVotesByPokerId(@RequestParam(value = "query", required = false)  Long pokerID) {
        log.debug("REST request to delete PokerVotes  by pokerID: {}", pokerID);
        pokerVotesRepository.deleteByPokerId(pokerID);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, pokerID.toString())).build();
    }
}
