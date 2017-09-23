package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.UGroup;

import com.coopsystem.repository.UGroupRepository;
import com.coopsystem.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UGroup.
 */
@RestController
@RequestMapping("/api")
public class UGroupResource {

    private final Logger log = LoggerFactory.getLogger(UGroupResource.class);

    private static final String ENTITY_NAME = "uGroup";

    private final UGroupRepository uGroupRepository;

    public UGroupResource(UGroupRepository uGroupRepository) {
        this.uGroupRepository = uGroupRepository;
    }

    /**
     * POST  /u-groups : Create a new uGroup.
     *
     * @param uGroup the uGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new uGroup, or with status 400 (Bad Request) if the uGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/u-groups")
    @Timed
    public ResponseEntity<UGroup> createUGroup(@RequestBody UGroup uGroup) throws URISyntaxException {
        log.debug("REST request to save UGroup : {}", uGroup);
        if (uGroup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new uGroup cannot already have an ID")).body(null);
        }
        UGroup result = uGroupRepository.save(uGroup);
        return ResponseEntity.created(new URI("/api/u-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /u-groups : Updates an existing uGroup.
     *
     * @param uGroup the uGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated uGroup,
     * or with status 400 (Bad Request) if the uGroup is not valid,
     * or with status 500 (Internal Server Error) if the uGroup couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/u-groups")
    @Timed
    public ResponseEntity<UGroup> updateUGroup(@RequestBody UGroup uGroup) throws URISyntaxException {
        log.debug("REST request to update UGroup : {}", uGroup);
        if (uGroup.getId() == null) {
            return createUGroup(uGroup);
        }
        UGroup result = uGroupRepository.save(uGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, uGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /u-groups : get all the uGroups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of uGroups in body
     */
    @GetMapping("/u-groups")
    @Timed
    public List<UGroup> getAllUGroups(@RequestParam(value="query", required=false) String param1) {
        if (param1 == null) {
            log.debug("REST request to get all UGroups");
            List<UGroup> uGroups = uGroupRepository.findAll();
            return uGroups;
        } else {
            UGroup uGroup = uGroupRepository.findUGroupByName(param1);
            return Arrays.asList(uGroup);
        }
    }

    @GetMapping("/u-groupsFirstStr")
    @Timed
    public List<UGroup> getUGroupsByFirstString(@RequestParam(value = "query", required = false) String param1) {
        List<UGroup> uGroups = new ArrayList<>();
        uGroupRepository.findAll().forEach(it -> {
            if (it.getName().contains(param1)) uGroups.add(it);
        });
        return uGroups;

    }
    /**
     * GET  /u-groups/:id : get the "id" uGroup.
     *
     * @param id the id of the uGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the uGroup, or with status 404 (Not Found)
     */
    @GetMapping("/u-groups/{id}")
    @Timed
    public ResponseEntity<UGroup> getUGroup(@PathVariable Long id) {
        log.debug("REST request to get UGroup : {}", id);
        UGroup uGroup = uGroupRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(uGroup));
    }

//    @GetMapping("/u-groupsByName")
//    private ResponseEntity<UGroup> getUGroupByName( @RequestParam(value="param1", required=false) String param1){
//        log.debug("REST request to get UGroup : {}", param1);
//        Optional<UGroup> uGroup = uGroupRepository.findUGroupByName(param1);
//        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(uGroup.get()));
//
//    }




    /**
     * DELETE  /u-groups/:id : delete the "id" uGroup.
     *
     * @param id the id of the uGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/u-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteUGroup(@PathVariable Long id) {
        log.debug("REST request to delete UGroup : {}", id);
        uGroupRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
