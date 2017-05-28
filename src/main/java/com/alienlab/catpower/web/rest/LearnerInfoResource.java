package com.alienlab.catpower.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.catpower.domain.LearnerInfo;
import com.alienlab.catpower.service.LearnerInfoService;
import com.alienlab.catpower.web.rest.util.HeaderUtil;
import com.alienlab.catpower.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
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
 * REST controller for managing LearnerInfo.
 */
@RestController
@RequestMapping("/api")
public class LearnerInfoResource {

    private final Logger log = LoggerFactory.getLogger(LearnerInfoResource.class);

    private static final String ENTITY_NAME = "learnerInfo";
        
    private final LearnerInfoService learnerInfoService;

    public LearnerInfoResource(LearnerInfoService learnerInfoService) {
        this.learnerInfoService = learnerInfoService;
    }

    /**
     * POST  /learner-infos : Create a new learnerInfo.
     *
     * @param learnerInfo the learnerInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new learnerInfo, or with status 400 (Bad Request) if the learnerInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/learner-infos")
    @Timed
    public ResponseEntity<LearnerInfo> createLearnerInfo(@RequestBody LearnerInfo learnerInfo) throws URISyntaxException {
        log.debug("REST request to save LearnerInfo : {}", learnerInfo);
        if (learnerInfo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new learnerInfo cannot already have an ID")).body(null);
        }
        LearnerInfo result = learnerInfoService.save(learnerInfo);
        return ResponseEntity.created(new URI("/api/learner-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /learner-infos : Updates an existing learnerInfo.
     *
     * @param learnerInfo the learnerInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated learnerInfo,
     * or with status 400 (Bad Request) if the learnerInfo is not valid,
     * or with status 500 (Internal Server Error) if the learnerInfo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/learner-infos")
    @Timed
    public ResponseEntity<LearnerInfo> updateLearnerInfo(@RequestBody LearnerInfo learnerInfo) throws URISyntaxException {
        log.debug("REST request to update LearnerInfo : {}", learnerInfo);
        if (learnerInfo.getId() == null) {
            return createLearnerInfo(learnerInfo);
        }
        LearnerInfo result = learnerInfoService.save(learnerInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, learnerInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /learner-infos : get all the learnerInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of learnerInfos in body
     */
    @GetMapping("/learner-infos")
    @Timed
    public ResponseEntity<List<LearnerInfo>> getAllLearnerInfos(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of LearnerInfos");
        Page<LearnerInfo> page = learnerInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/learner-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /learner-infos/:id : get the "id" learnerInfo.
     *
     * @param id the id of the learnerInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the learnerInfo, or with status 404 (Not Found)
     */
    @GetMapping("/learner-infos/{id}")
    @Timed
    public ResponseEntity<LearnerInfo> getLearnerInfo(@PathVariable Long id) {
        log.debug("REST request to get LearnerInfo : {}", id);
        LearnerInfo learnerInfo = learnerInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(learnerInfo));
    }

    /**
     * DELETE  /learner-infos/:id : delete the "id" learnerInfo.
     *
     * @param id the id of the learnerInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/learner-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteLearnerInfo(@PathVariable Long id) {
        log.debug("REST request to delete LearnerInfo : {}", id);
        learnerInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
