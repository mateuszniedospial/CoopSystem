package com.coopsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coopsystem.domain.Attachment;

import com.coopsystem.repository.AttachmentRepository;
import com.coopsystem.service.TaskHistoryDiffService;
import com.coopsystem.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Attachment.
 */
@RestController
@RequestMapping("/api")
public class AttachmentResource {

    private final TaskHistoryDiffService taskHistoryDiffService;

    private final Logger log = LoggerFactory.getLogger(AttachmentResource.class);

    private static final String ENTITY_NAME = "attachment";

    private final AttachmentRepository attachmentRepository;

    public AttachmentResource(TaskHistoryDiffService taskHistoryDiffService, AttachmentRepository attachmentRepository) {
        this.taskHistoryDiffService = taskHistoryDiffService;
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * POST  /attachments : Create a new attachment.
     *
     * @param attachment the attachment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attachment, or with status 400 (Bad Request) if the attachment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attachments")
    @Timed
    public ResponseEntity<Attachment> createAttachment(@Valid @RequestBody Attachment attachment) throws URISyntaxException {
        log.debug("REST request to save Attachment : {}", attachment);
        if (attachment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new attachment cannot already have an ID")).body(null);
        }
        Attachment result = attachmentRepository.save(attachment);
        taskHistoryDiffService.createHistoryForAttachment(attachment, "add");
        return ResponseEntity.created(new URI("/api/attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attachments : Updates an existing attachment.
     *
     * @param attachment the attachment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attachment,
     * or with status 400 (Bad Request) if the attachment is not valid,
     * or with status 500 (Internal Server Error) if the attachment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attachments")
    @Timed
    public ResponseEntity<Attachment> updateAttachment(@Valid @RequestBody Attachment attachment) throws URISyntaxException {
        log.debug("REST request to update Attachment : {}", attachment);
        if (attachment.getId() == null) {
            return createAttachment(attachment);
        }
        Attachment result = attachmentRepository.save(attachment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attachment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attachments : get all the attachments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of attachments in body
     */
    @GetMapping("/attachments")
    @Timed
    public List<Attachment> getAllAttachments() {
        log.debug("REST request to get all Attachments");
        List<Attachment> attachments = attachmentRepository.findAll();
        return attachments;
    }

    @GetMapping("/attachments/byTaskId")
    @Timed
    public List<Attachment> getAttachmentsByTaskId(@RequestParam(value = "query", required = false)  Long taskId) {
        log.debug("REST request to get all Attachments");
        List<Attachment> attachments = attachmentRepository.findAttachmentByTaskId(taskId);
        attachments.forEach(it->{
            it.setContent(null);
            it.setTask(null);
        });
        return attachments;
    }

    /**
     * GET  /attachments/:id : get the "id" attachment.
     *
     * @param id the id of the attachment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attachment, or with status 404 (Not Found)
     */
    @GetMapping("/attachments/{id}")
    @Timed
    public ResponseEntity<Attachment> getAttachment(@PathVariable Long id) {
        log.debug("REST request to get Attachment : {}", id);
        Attachment attachment = attachmentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(attachment));
    }

    @GetMapping("/attachmentFile/{id}")
    public void getAttachmentFile(@PathVariable Long id, HttpServletResponse response) throws IOException {
        log.debug("REST request to get AttachmentFile : {}", id);
        Attachment attachment = attachmentRepository.findOne(id);
        response.addHeader("Content-disposition", "attachment;filename="+attachment.getName());
        response.setContentType(attachment.getContentContentType());
        response.getOutputStream().write(attachment.getContent());
        response.flushBuffer();
    }


    /**
     * DELETE  /attachments/:id : delete the "id" attachment.
     *
     * @param id the id of the attachment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attachments/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        log.debug("REST request to delete Attachment : {}", id);
        taskHistoryDiffService.createHistoryForAttachment(attachmentRepository.findOne(id), "remove");
        attachmentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
