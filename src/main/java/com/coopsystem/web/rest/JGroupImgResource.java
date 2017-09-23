package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.JGroupImg;

import com.coopsystem.repository.JGroupImgRepository;
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
 * REST controller for managing JGroupImg.
 */
@RestController
@RequestMapping("/api")
public class JGroupImgResource {

    private final Logger log = LoggerFactory.getLogger(JGroupImgResource.class);

    private static final String ENTITY_NAME = "jGroupImg";

    private final JGroupImgRepository jGroupImgRepository;

    public JGroupImgResource(JGroupImgRepository jGroupImgRepository) {
        this.jGroupImgRepository = jGroupImgRepository;
    }

    /**
     * POST  /j-group-imgs : Create a new jGroupImg.
     *
     * @param jGroupImg the jGroupImg to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jGroupImg, or with status 400 (Bad Request) if the jGroupImg has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/j-group-imgs")
    @Timed
    public ResponseEntity<JGroupImg> createJGroupImg(@Valid @RequestBody JGroupImg jGroupImg) throws URISyntaxException {
        log.debug("REST request to save JGroupImg : {}", jGroupImg);
        if (jGroupImg.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jGroupImg cannot already have an ID")).body(null);
        }
        JGroupImg result = jGroupImgRepository.save(jGroupImg);
        return ResponseEntity.created(new URI("/api/j-group-imgs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /j-group-imgs : Updates an existing jGroupImg.
     *
     * @param jGroupImg the jGroupImg to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jGroupImg,
     * or with status 400 (Bad Request) if the jGroupImg is not valid,
     * or with status 500 (Internal Server Error) if the jGroupImg couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/j-group-imgs")
    @Timed
    public ResponseEntity<JGroupImg> updateJGroupImg(@Valid @RequestBody JGroupImg jGroupImg) throws URISyntaxException {
        log.debug("REST request to update JGroupImg : {}", jGroupImg);
        if (jGroupImg.getId() == null) {
            return createJGroupImg(jGroupImg);
        }
        JGroupImg result = jGroupImgRepository.save(jGroupImg);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jGroupImg.getId().toString()))
            .body(result);
    }

    /**
     * GET  /j-group-imgs : get all the jGroupImgs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jGroupImgs in body
     */
    @GetMapping("/j-group-imgs")
    @Timed
    public List<JGroupImg> getAllJGroupImgs() {
        log.debug("REST request to get all JGroupImgs");
        List<JGroupImg> jGroupImgs = jGroupImgRepository.findAll();
        return jGroupImgs;
    }

    /**
     * GET  /j-group-imgs/:id : get the "id" jGroupImg.
     *
     * @param id the id of the jGroupImg to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jGroupImg, or with status 404 (Not Found)
     */
    @GetMapping("/j-group-imgs/{id}")
    @Timed
    public ResponseEntity<JGroupImg> getJGroupImg(@PathVariable Long id) {
        log.debug("REST request to get JGroupImg : {}", id);
        JGroupImg jGroupImg = jGroupImgRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jGroupImg));
    }

    @GetMapping("/j-group-img/j-group/{id}")
    @Timed
    public JGroupImg getJImgOfJGroup(@PathVariable Long id) {
        log.debug("REST request to get specific jGroup JGroupImg");
        return jGroupImgRepository.findJImgOfJGroup(id);
    }

    /**
     * DELETE  /j-group-imgs/:id : delete the "id" jGroupImg.
     *
     * @param id the id of the jGroupImg to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/j-group-imgs/{id}")
    @Timed
    public ResponseEntity<Void> deleteJGroupImg(@PathVariable Long id) {
        log.debug("REST request to delete JGroupImg : {}", id);
        jGroupImgRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
