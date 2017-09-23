package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.DocumentationCatalogue;

import com.coopsystem.domain.ProjectDocumentation;
import com.coopsystem.repository.DocumentationCatalogueRepository;
import com.coopsystem.repository.ProjectDocumentationRepository;
import com.coopsystem.web.rest.util.DocumentationNode;
import com.coopsystem.web.rest.util.HeaderUtil;
import com.coopsystem.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DocumentationCatalogue.
 */
@RestController
@RequestMapping("/api")
public class DocumentationCatalogueResource {

    private final Logger log = LoggerFactory.getLogger(DocumentationCatalogueResource.class);

    private static final String ENTITY_NAME = "documentationCatalogue";

    private final DocumentationCatalogueRepository documentationCatalogueRepository;
    private final ProjectDocumentationRepository projectDocumentationRepository;

    public DocumentationCatalogueResource(DocumentationCatalogueRepository documentationCatalogueRepository, ProjectDocumentationRepository projectDocumentationRepository) {
        this.documentationCatalogueRepository = documentationCatalogueRepository;
        this.projectDocumentationRepository = projectDocumentationRepository;
    }

    /**
     * POST  /documentation-catalogues : Create a new documentationCatalogue.
     *
     * @param documentationCatalogue the documentationCatalogue to create
     * @return the ResponseEntity with status 201 (Created) and with body the new documentationCatalogue, or with status 400 (Bad Request) if the documentationCatalogue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/documentation-catalogues")
    @Timed
    public ResponseEntity<DocumentationCatalogue> createDocumentationCatalogue(@RequestBody DocumentationCatalogue documentationCatalogue) throws URISyntaxException {
        log.debug("REST request to save DocumentationCatalogue : {}", documentationCatalogue);
        if (documentationCatalogue.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists or label or data", "A new documentationCatalogue cannot already have an ID")).body(null);
        } else if (documentationCatalogueRepository.findByLabel(documentationCatalogue.getLabel()) != null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists or label or data", "A new documentationCatalogue cannot duplicate labels.")).body(null);
        }

        DocumentationCatalogue parent = documentationCatalogueRepository.byParent();
        if(parent == null){
            DocumentationCatalogue root = new DocumentationCatalogue();
            root.setLabel("Documentation");
            root.setData("ROOT");
            root.setParent(null);
            root.setProject(documentationCatalogue.getProject());
            documentationCatalogueRepository.save(root);

            documentationCatalogue.setParent(root);
            DocumentationCatalogue result = documentationCatalogueRepository.save(documentationCatalogue);
            return ResponseEntity.created(new URI("/api/documentation-catalogues/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
        } else if (documentationCatalogue.getParent() == null){
            documentationCatalogue.setParent(documentationCatalogueRepository.byParent());
        }

        DocumentationCatalogue result = documentationCatalogueRepository.save(documentationCatalogue);
        return ResponseEntity.created(new URI("/api/documentation-catalogues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /documentation-catalogues : Updates an existing documentationCatalogue.
     *
     * @param documentationCatalogue the documentationCatalogue to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated documentationCatalogue,
     * or with status 400 (Bad Request) if the documentationCatalogue is not valid,
     * or with status 500 (Internal Server Error) if the documentationCatalogue couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/documentation-catalogues")
    @Timed
    public ResponseEntity<DocumentationCatalogue> updateDocumentationCatalogue(@RequestBody DocumentationCatalogue documentationCatalogue) throws URISyntaxException {
        log.debug("REST request to update DocumentationCatalogue : {}", documentationCatalogue);
        if (documentationCatalogue.getId() == null) {
            return createDocumentationCatalogue(documentationCatalogue);
        }

        DocumentationCatalogue result = documentationCatalogueRepository.save(documentationCatalogue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, documentationCatalogue.getId().toString()))
            .body(result);
    }

    /**
     * GET  /documentation-catalogues : get all the documentationCatalogues.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of documentationCatalogues in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/documentation-catalogues")
    @Timed
    public ResponseEntity<List<DocumentationCatalogue>> getAllDocumentationCatalogues(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DocumentationCatalogues");
        Page<DocumentationCatalogue> page = documentationCatalogueRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/documentation-catalogues");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /documentation-catalogues/:id : get the "id" documentationCatalogue.
     *
     * @param id the id of the documentationCatalogue to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the documentationCatalogue, or with status 404 (Not Found)
     */
    @GetMapping("/documentation-catalogues/{id}")
    @Timed
    public ResponseEntity<DocumentationCatalogue> getDocumentationCatalogue(@PathVariable Long id) {
        log.debug("REST request to get DocumentationCatalogue : {}", id);
        DocumentationCatalogue documentationCatalogue = documentationCatalogueRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(documentationCatalogue));
    }

    @GetMapping("/documentation-catalogues/byLabel/{label}")
    @Timed
    public ResponseEntity<DocumentationCatalogue> getDocumentationCatalogue(@PathVariable String label) {
        log.debug("REST request to get DocumentationCatalogue by label: {}", label);
        DocumentationCatalogue documentationCatalogue = documentationCatalogueRepository.findByLabel(label);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(documentationCatalogue));
    }

    @GetMapping("/project/{id}/documentation-catalogues/asJSON")
    @Timed
    public JSONObject getDocumentationCataloguesAsJSON(@PathVariable Long id) {
        log.debug("REST request to get all doc catalogues tree: {}", id);
        List<DocumentationCatalogue> catalogues = documentationCatalogueRepository.findAllProjectCatalogue(id);
        List<ProjectDocumentation> documentations = projectDocumentationRepository.findAllProjectDocumentations(id);
        DocumentationNode docRootNode = DocumentationNode.createDocNodeTree(catalogues, documentations);
        return DocumentationNode.buildJSON(docRootNode);
    }

    /**
     * DELETE  /documentation-catalogues/:id : delete the "id" documentationCatalogue.
     *
     * @param id the id of the documentationCatalogue to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/documentation-catalogues/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocumentationCatalogue(@PathVariable Long id) {
        log.debug("REST request to delete DocumentationCatalogue : {}", id);
        DocumentationCatalogue folder = documentationCatalogueRepository.findOne(id);
        if(folder.getParent() == null){
            throw new UnsupportedOperationException("Cannot delete root folder before all its content.");
        }

        List<DocumentationCatalogue> nestedFolders = documentationCatalogueRepository.findNestedFolders(id);
        nestedFolders.forEach(nF -> nF.setParent(folder.getParent()));
        nestedFolders.forEach(documentationCatalogueRepository::save);

        List<ProjectDocumentation> nestedDocs = projectDocumentationRepository.findAllByCatalogue(id);
        nestedDocs.forEach(nD -> nD.setUnderCatalogue(folder.getParent()));
        nestedDocs.forEach(projectDocumentationRepository::save);

        documentationCatalogueRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
