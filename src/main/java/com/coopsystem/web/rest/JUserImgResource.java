package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.JUserImg;

import com.coopsystem.repository.JUserImgRepository;
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
 * REST controller for managing JUserImg.
 */
@RestController
@RequestMapping("/api")
public class JUserImgResource {

    private final Logger log = LoggerFactory.getLogger(JUserImgResource.class);

    private static final String ENTITY_NAME = "jUserImg";

    private final JUserImgRepository jUserImgRepository;

    public JUserImgResource(JUserImgRepository jUserImgRepository) {
        this.jUserImgRepository = jUserImgRepository;
    }

    /**
     * POST  /j-user-imgs : Create a new jUserImg.
     *
     * @param jUserImg the jUserImg to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jUserImg, or with status 400 (Bad Request) if the jUserImg has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/j-user-imgs")
    @Timed
    public ResponseEntity<JUserImg> createJUserImg(@RequestBody JUserImg jUserImg) throws URISyntaxException {
        log.debug("REST request to save JUserImg : {}", jUserImg);
        if (jUserImg.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jUserImg cannot already have an ID")).body(null);
        }
        JUserImg result = jUserImgRepository.save(jUserImg);
        return ResponseEntity.created(new URI("/api/j-user-imgs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /j-user-imgs : Updates an existing jUserImg.
     *
     * @param jUserImg the jUserImg to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jUserImg,
     * or with status 400 (Bad Request) if the jUserImg is not valid,
     * or with status 500 (Internal Server Error) if the jUserImg couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/j-user-imgs")
    @Timed
    public ResponseEntity<JUserImg> updateJUserImg(@RequestBody JUserImg jUserImg) throws URISyntaxException {
        log.debug("REST request to update JUserImg : {}", jUserImg);
        if (jUserImg.getId() == null) {
            return createJUserImg(jUserImg);
        }
        JUserImg result = jUserImgRepository.save(jUserImg);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jUserImg.getId().toString()))
            .body(result);
    }

    /**
     * GET  /j-user-imgs : get all the jUserImgs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jUserImgs in body
     */
    @GetMapping("/j-user-imgs")
    @Timed
    public List<JUserImg> getAllJUserImgs() {
        log.debug("REST request to get all JUserImgs");
        List<JUserImg> jUserImgs = jUserImgRepository.findAll();
        return jUserImgs;
    }

    @GetMapping("/j-user-imgs/j-user/{id}")
    @Timed
    public JUserImg getJUserJImg(@PathVariable Long id) {
        log.debug("REST request to get all JUserImgs");
        return jUserImgRepository.findJImgOfJUser(id);
    }
    /**
     * GET  /j-user-imgs/:id : get the "id" jUserImg.
     *
     * @param id the id of the jUserImg to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jUserImg, or with status 404 (Not Found)
     */
    @GetMapping("/j-user-imgs/{id}")
    @Timed
    public ResponseEntity<JUserImg> getJUserImg(@PathVariable Long id) {
        log.debug("REST request to get JUserImg : {}", id);
        JUserImg jUserImg = jUserImgRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jUserImg));
    }

    @GetMapping("/j-user-imgByJuserId}")
    @Timed
    public ResponseEntity<JUserImg> getJUserImgByJUser(@RequestParam(value = "juserId", required = false)  Long juserId) {
        JUserImg jUserImg = jUserImgRepository.findJUserImgByJuserId(juserId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jUserImg));
    }

    /**
     * DELETE  /j-user-imgs/:id : delete the "id" jUserImg.
     *
     * @param id the id of the jUserImg to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/j-user-imgs/{id}")
    @Timed
    public ResponseEntity<Void> deleteJUserImg(@PathVariable Long id) {
        log.debug("REST request to delete JUserImg : {}", id);
        jUserImgRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
