package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.DocumentationHistory;

import com.coopsystem.domain.enumeration.LifeCycle;
import com.coopsystem.repository.DocumentationHistoryRepository;
import com.coopsystem.web.rest.util.HeaderUtil;
import com.coopsystem.web.rest.util.Timeout;
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
 * REST controller for managing DocumentationHistory.
 */
@RestController
@RequestMapping("/api")
public class DocumentationHistoryResource {

    private final Logger log = LoggerFactory.getLogger(DocumentationHistoryResource.class);

    private static final String ENTITY_NAME = "documentationHistory";

    private final DocumentationHistoryRepository documentationHistoryRepository;

    public DocumentationHistoryResource(DocumentationHistoryRepository documentationHistoryRepository) {
        this.documentationHistoryRepository = documentationHistoryRepository;
    }

    /**
     * POST  /documentation-histories : Create a new documentationHistory.
     *
     * @param documentationHistory the documentationHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new documentationHistory, or with status 400 (Bad Request) if the documentationHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/documentation-histories")
    @Timed
    public ResponseEntity<DocumentationHistory> createDocumentationHistory(@RequestBody DocumentationHistory documentationHistory) throws URISyntaxException {
        log.debug("REST request to save DocumentationHistory : {}", documentationHistory);
        if (documentationHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new documentationHistory cannot already have an ID")).body(null);
        }
        documentationHistory.setLifeCycle(LifeCycle.TERMINATED);

        DocumentationHistory result = Timeout.setTimeout(documentationHistoryRepository, documentationHistory, 90L);
        if(result == null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "timeout", "Operation took too long and could not be finished.")).body(null);
        } else {
            return ResponseEntity.created(new URI("/api/documentation-histories/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
        }
//        DocumentationHistory result = documentationHistoryRepository.save(documentationHistory);
//        return ResponseEntity.created(new URI("/api/documentation-histories/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
//            .body(result);
    }

    /**
     * PUT  /documentation-histories : Updates an existing documentationHistory.
     *
     * @param documentationHistory the documentationHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated documentationHistory,
     * or with status 400 (Bad Request) if the documentationHistory is not valid,
     * or with status 500 (Internal Server Error) if the documentationHistory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/documentation-histories")
    @Timed
    public ResponseEntity<DocumentationHistory> updateDocumentationHistory(@RequestBody DocumentationHistory documentationHistory) throws URISyntaxException {
        log.debug("REST request to update DocumentationHistory : {}", documentationHistory);
        if (documentationHistory.getId() == null) {
            return createDocumentationHistory(documentationHistory);
        }

        DocumentationHistory result = Timeout.setTimeout(documentationHistoryRepository, documentationHistory, 90L);
        if(result == null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "timeout", "Operation took too long and could not be finished.")).body(null);
        } else {
            return ResponseEntity.created(new URI("/api/documentation-histories/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
        }

//        DocumentationHistory result = documentationHistoryRepository.save(documentationHistory);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, documentationHistory.getId().toString()))
//            .body(result);
    }

    /**
     * GET  /documentation-histories : get all the documentationHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of documentationHistories in body
     */
    @GetMapping("/documentation-histories")
    @Timed
    public List<DocumentationHistory> getAllDocumentationHistories() {
        log.debug("REST request to get all DocumentationHistories");
        List<DocumentationHistory> documentationHistories = documentationHistoryRepository.findAll();
        return documentationHistories;
    }

    /**
     * GET  /documentation-histories/:id : get the "id" documentationHistory.
     *
     * @param id the id of the documentationHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the documentationHistory, or with status 404 (Not Found)
     */
    @GetMapping("/documentation-histories/{id}")
    @Timed
    public ResponseEntity<DocumentationHistory> getDocumentationHistory(@PathVariable Long id) {
        log.debug("REST request to get DocumentationHistory : {}", id);
        DocumentationHistory documentationHistory = documentationHistoryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(documentationHistory));
    }

    @GetMapping("/documentation-histories/document/{id}")
    @Timed
    public List<DocumentationHistory> getAllVersionsOfDocument(@PathVariable Long id) {
        log.debug("REST request to get all versions of Document : {}", id);
        return documentationHistoryRepository.findAllVersionsOfDocument(id);
    }

    /**
     * DELETE  /documentation-histories/:id : delete the "id" documentationHistory.
     *
     * @param id the id of the documentationHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/documentation-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocumentationHistory(@PathVariable Long id) {
        log.debug("REST request to delete DocumentationHistory : {}", id);
        documentationHistoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
