package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.ProjectImg;

import com.coopsystem.repository.ProjectImgRepository;
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
 * REST controller for managing ProjectImg.
 */
@RestController
@RequestMapping("/api")
public class ProjectImgResource {

    private final Logger log = LoggerFactory.getLogger(ProjectImgResource.class);

    private static final String ENTITY_NAME = "projectImg";

    private final ProjectImgRepository projectImgRepository;

    public ProjectImgResource(ProjectImgRepository projectImgRepository) {
        this.projectImgRepository = projectImgRepository;
    }

    /**
     * POST  /project-imgs : Create a new projectImg.
     *
     * @param projectImg the projectImg to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectImg, or with status 400 (Bad Request) if the projectImg has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-imgs")
    @Timed
    public ResponseEntity<ProjectImg> createProjectImg(@Valid @RequestBody ProjectImg projectImg) throws URISyntaxException {
        log.debug("REST request to save ProjectImg : {}", projectImg);
        if (projectImg.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new projectImg cannot already have an ID")).body(null);
        }
        ProjectImg result = projectImgRepository.save(projectImg);
        return ResponseEntity.created(new URI("/api/project-imgs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-imgs : Updates an existing projectImg.
     *
     * @param projectImg the projectImg to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectImg,
     * or with status 400 (Bad Request) if the projectImg is not valid,
     * or with status 500 (Internal Server Error) if the projectImg couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-imgs")
    @Timed
    public ResponseEntity<ProjectImg> updateProjectImg(@Valid @RequestBody ProjectImg projectImg) throws URISyntaxException {
        log.debug("REST request to update ProjectImg : {}", projectImg);
        if (projectImg.getId() == null) {
            return createProjectImg(projectImg);
        }
        ProjectImg result = projectImgRepository.save(projectImg);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectImg.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-imgs : get all the projectImgs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projectImgs in body
     */
    @GetMapping("/project-imgs")
    @Timed
    public List<ProjectImg> getAllProjectImgs() {
        log.debug("REST request to get all ProjectImgs");
        List<ProjectImg> projectImgs = projectImgRepository.findAll();
        return projectImgs;
    }

    /**
     * GET  /project-imgs/:id : get the "id" projectImg.
     *
     * @param id the id of the projectImg to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectImg, or with status 404 (Not Found)
     */
    @GetMapping("/project-imgs/{id}")
    @Timed
    public ResponseEntity<ProjectImg> getProjectImg(@PathVariable Long id) {
        log.debug("REST request to get ProjectImg : {}", id);
        ProjectImg projectImg = projectImgRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectImg));
    }

    @GetMapping("/project-img/project/{id}")
    @Timed
    public ResponseEntity<ProjectImg> getImgOfProject(@PathVariable Long id) {
        log.debug("REST request to get ProjectImg of Project : {}", id);
        ProjectImg projectImg = projectImgRepository.findImgOfProject(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectImg));
    }

    /**
     * DELETE  /project-imgs/:id : delete the "id" projectImg.
     *
     * @param id the id of the projectImg to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-imgs/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjectImg(@PathVariable Long id) {
        log.debug("REST request to delete ProjectImg : {}", id);
        projectImgRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
