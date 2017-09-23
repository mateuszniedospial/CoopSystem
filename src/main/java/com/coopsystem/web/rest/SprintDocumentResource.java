package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.SprintDocument;

import com.coopsystem.repository.SprintDocumentRepository;
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
 * REST controller for managing SprintDocument.
 */
@RestController
@RequestMapping("/api")
public class SprintDocumentResource {

    private final Logger log = LoggerFactory.getLogger(SprintDocumentResource.class);

    private static final String ENTITY_NAME = "sprintDocument";

    private final SprintDocumentRepository sprintDocumentRepository;

    public SprintDocumentResource(SprintDocumentRepository sprintDocumentRepository) {
        this.sprintDocumentRepository = sprintDocumentRepository;
    }

    /**
     * POST  /sprint-documents : Create a new sprintDocument.
     *
     * @param sprintDocument the sprintDocument to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sprintDocument, or with status 400 (Bad Request) if the sprintDocument has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sprint-documents")
    @Timed
    public ResponseEntity<SprintDocument> createSprintDocument(@Valid @RequestBody SprintDocument sprintDocument) throws URISyntaxException {
        log.debug("REST request to save SprintDocument : {}", sprintDocument);
        if (sprintDocument.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sprintDocument cannot already have an ID")).body(null);
        }
        SprintDocument result = sprintDocumentRepository.save(sprintDocument);
        return ResponseEntity.created(new URI("/api/sprint-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sprint-documents : Updates an existing sprintDocument.
     *
     * @param sprintDocument the sprintDocument to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sprintDocument,
     * or with status 400 (Bad Request) if the sprintDocument is not valid,
     * or with status 500 (Internal Server Error) if the sprintDocument couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sprint-documents")
    @Timed
    public ResponseEntity<SprintDocument> updateSprintDocument(@Valid @RequestBody SprintDocument sprintDocument) throws URISyntaxException {
        log.debug("REST request to update SprintDocument : {}", sprintDocument);
        if (sprintDocument.getId() == null) {
            return createSprintDocument(sprintDocument);
        }
        SprintDocument result = sprintDocumentRepository.save(sprintDocument);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sprintDocument.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sprint-documents : get all the sprintDocuments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sprintDocuments in body
     */
    @GetMapping("/sprint-documents")
    @Timed
    public List<SprintDocument> getAllSprintDocuments() {
        log.debug("REST request to get all SprintDocuments");
        List<SprintDocument> sprintDocuments = sprintDocumentRepository.findAll();
        return sprintDocuments;
    }

    /**
     * GET  /sprint-documents/:id : get the "id" sprintDocument.
     *
     * @param id the id of the sprintDocument to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sprintDocument, or with status 404 (Not Found)
     */
    @GetMapping("/sprint-documents/{id}")
    @Timed
    public ResponseEntity<SprintDocument> getSprintDocument(@PathVariable Long id) {
        log.debug("REST request to get SprintDocument : {}", id);
        SprintDocument sprintDocument = sprintDocumentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sprintDocument));
    }

    /**
     * DELETE  /sprint-documents/:id : delete the "id" sprintDocument.
     *
     * @param id the id of the sprintDocument to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sprint-documents/{id}")
    @Timed
    public ResponseEntity<Void> deleteSprintDocument(@PathVariable Long id) {
        log.debug("REST request to delete SprintDocument : {}", id);
        sprintDocumentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
