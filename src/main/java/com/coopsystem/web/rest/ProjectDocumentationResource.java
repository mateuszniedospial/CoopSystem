package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.ProjectDocumentation;

import com.coopsystem.repository.DocumentationCatalogueRepository;
import com.coopsystem.repository.ProjectDocumentationRepository;
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
 * REST controller for managing ProjectDocumentation.
 */
@RestController
@RequestMapping("/api")
public class ProjectDocumentationResource {

    private final Logger log = LoggerFactory.getLogger(ProjectDocumentationResource.class);

    private static final String ENTITY_NAME = "projectDocumentation";

    private final ProjectDocumentationRepository projectDocumentationRepository;
    private final DocumentationCatalogueRepository documentationCatalogueRepository;

    public ProjectDocumentationResource(ProjectDocumentationRepository projectDocumentationRepository, DocumentationCatalogueRepository documentationCatalogueRepository) {
        this.projectDocumentationRepository = projectDocumentationRepository;
        this.documentationCatalogueRepository = documentationCatalogueRepository;
    }

    /**
     * POST  /project-documentations : Create a new projectDocumentation.
     *
     * @param projectDocumentation the projectDocumentation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectDocumentation, or with status 400 (Bad Request) if the projectDocumentation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-documentations")
    @Timed
    public ResponseEntity<ProjectDocumentation> createProjectDocumentation(@RequestBody ProjectDocumentation projectDocumentation) throws URISyntaxException {
        log.debug("REST request to save ProjectDocumentation : {}", projectDocumentation);
        if (projectDocumentation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new projectDocumentation cannot already have an ID")).body(null);
        } else if (projectDocumentationRepository.findByLabel(projectDocumentation.getLabel()) != null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists or label or data", "A new project documentation cannot duplicate labels.")).body(null);
        }

        if(projectDocumentation.getUnderCatalogue() == null){
            projectDocumentation.setUnderCatalogue(documentationCatalogueRepository.byParent());
        }


        ProjectDocumentation result = Timeout.setTimeout(projectDocumentationRepository, projectDocumentation, 90L);

        if(result == null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "timeout", "Operation took too long and could not be finished.")).body(null);
        } else {
            return ResponseEntity.created(new URI("/api/project-documentations/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
        }
//        ProjectDocumentation result = projectDocumentationRepository.save(projectDocumentation);
    }

    /**
     * PUT  /project-documentations : Updates an existing projectDocumentation.
     *
     * @param projectDocumentation the projectDocumentation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectDocumentation,
     * or with status 400 (Bad Request) if the projectDocumentation is not valid,
     * or with status 500 (Internal Server Error) if the projectDocumentation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-documentations")
    @Timed
    public ResponseEntity<ProjectDocumentation> updateProjectDocumentation(@RequestBody ProjectDocumentation projectDocumentation) throws URISyntaxException {
        log.debug("REST request to update ProjectDocumentation : {}", projectDocumentation);
        if (projectDocumentation.getId() == null) {
            return createProjectDocumentation(projectDocumentation);
        }

        ProjectDocumentation result = Timeout.setTimeout(projectDocumentationRepository, projectDocumentation, 90L);
        if(result == null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "timeout", "Operation took too long and could not be finished.")).body(result);
        } else {
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectDocumentation.getId().toString()))
                .body(result);
        }
//        ProjectDocumentation result = projectDocumentationRepository.save(projectDocumentation);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectDocumentation.getId().toString()))
//            .body(result);
    }

    /**
     * GET  /project-documentations : get all the projectDocumentations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projectDocumentations in body
     */
    @GetMapping("/project-documentations")
    @Timed
    public List<ProjectDocumentation> getAllProjectDocumentations() {
        log.debug("REST request to get all ProjectDocumentations");
        List<ProjectDocumentation> projectDocumentations = projectDocumentationRepository.findAll();
        return projectDocumentations;
    }

    @GetMapping("/project-documentations/byLabel/{label}")
    @Timed
    public ResponseEntity<ProjectDocumentation> getProjectDocumentationByLabel(@PathVariable String label) {
        log.debug("REST request to get Project Documentation by label: {}", label);
        ProjectDocumentation projectDocumentation = projectDocumentationRepository.findByLabel(label);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectDocumentation));
    }

    /**
     * GET  /project-documentations/:id : get the "id" projectDocumentation.
     *
     * @param id the id of the projectDocumentation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectDocumentation, or with status 404 (Not Found)
     */
    @GetMapping("/project-documentations/{id}")
    @Timed
    public ResponseEntity<ProjectDocumentation> getProjectDocumentation(@PathVariable Long id) {
        log.debug("REST request to get ProjectDocumentation : {}", id);
        ProjectDocumentation projectDocumentation = projectDocumentationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectDocumentation));
    }

    /**
     * DELETE  /project-documentations/:id : delete the "id" projectDocumentation.
     *
     * @param id the id of the projectDocumentation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-documentations/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjectDocumentation(@PathVariable Long id) {
        log.debug("REST request to delete ProjectDocumentation : {}", id);
        projectDocumentationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
