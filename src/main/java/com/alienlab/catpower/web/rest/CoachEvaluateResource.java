package com.alienlab.catpower.web.rest;

import com.alibaba.fastjson.util.TypeUtils;
import com.alienlab.catpower.domain.LearnerCharge;
import com.alienlab.catpower.repository.LearnerChargeRepository;
import com.alienlab.catpower.web.rest.util.ExecResult;
import com.codahale.metrics.annotation.Timed;
import com.alienlab.catpower.domain.CoachEvaluate;
import com.alienlab.catpower.service.CoachEvaluateService;
import com.alienlab.catpower.web.rest.util.HeaderUtil;
import com.alienlab.catpower.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing CoachEvaluate.
 */
@RestController
@RequestMapping("/api")
public class CoachEvaluateResource {

    private final Logger log = LoggerFactory.getLogger(CoachEvaluateResource.class);

    private static final String ENTITY_NAME = "coachEvaluate";

    private final CoachEvaluateService coachEvaluateService;

    public CoachEvaluateResource(CoachEvaluateService coachEvaluateService) {
        this.coachEvaluateService = coachEvaluateService;
    }

    /**
     * POST  /coach-evaluates : Create a new coachEvaluate.
     *
     * @param coachEvaluate the coachEvaluate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coachEvaluate, or with status 400 (Bad Request) if the coachEvaluate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/coach-evaluates")
    @Timed
    public ResponseEntity<CoachEvaluate> createCoachEvaluate(@RequestBody CoachEvaluate coachEvaluate) throws URISyntaxException {
        log.debug("REST request to save CoachEvaluate : {}", coachEvaluate);
        if (coachEvaluate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new coachEvaluate cannot already have an ID")).body(null);
        }
        CoachEvaluate result = coachEvaluateService.save(coachEvaluate);
        return ResponseEntity.created(new URI("/api/coach-evaluates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coach-evaluates : Updates an existing coachEvaluate.
     *
     * @param coachEvaluate the coachEvaluate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coachEvaluate,
     * or with status 400 (Bad Request) if the coachEvaluate is not valid,
     * or with status 500 (Internal Server Error) if the coachEvaluate couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/coach-evaluates")
    @Timed
    public ResponseEntity<CoachEvaluate> updateCoachEvaluate(@RequestBody CoachEvaluate coachEvaluate) throws URISyntaxException {
        log.debug("REST request to update CoachEvaluate : {}", coachEvaluate);
        if (coachEvaluate.getId() == null) {
            return createCoachEvaluate(coachEvaluate);
        }
        CoachEvaluate result = coachEvaluateService.save(coachEvaluate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, coachEvaluate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coach-evaluates : get all the coachEvaluates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of coachEvaluates in body
     */
    @GetMapping("/coach-evaluates")
    @Timed
    public ResponseEntity<List<CoachEvaluate>> getAllCoachEvaluates(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CoachEvaluates");
        Page<CoachEvaluate> page = coachEvaluateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/coach-evaluates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /coach-evaluates/:id : get the "id" coachEvaluate.
     *
     * @param id the id of the coachEvaluate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coachEvaluate, or with status 404 (Not Found)
     */
    @GetMapping("/coach-evaluates/{id}")
    @Timed
    public ResponseEntity<CoachEvaluate> getCoachEvaluate(@PathVariable Long id) {
        log.debug("REST request to get CoachEvaluate : {}", id);
        CoachEvaluate coachEvaluate = coachEvaluateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(coachEvaluate));
    }

    /**
     * DELETE  /coach-evaluates/:id : delete the "id" coachEvaluate.
     *
     * @param id the id of the coachEvaluate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/coach-evaluates/{id}")
    @Timed
    public ResponseEntity<Void> deleteCoachEvaluate(@PathVariable Long id) {
        log.debug("REST request to delete CoachEvaluate : {}", id);
        coachEvaluateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @ApiOperation("学员对教练进行评价")
    @PostMapping("/coach-evaluates-learner")
    public ResponseEntity insertCoachEvaluate(@RequestBody Map param){
        Long serviceAttitude = TypeUtils.castToLong(param.get("serviceAttitude"));
        Long speciality = TypeUtils.castToLong(param.get("speciality"));
        Long like =  TypeUtils.castToLong(param.get("like"));
        String complain = TypeUtils.castToString(param.get("complain"));
        Long evaluation = TypeUtils.castToLong(param.get("evaluation"));
        Long   learnerId = TypeUtils.castToLong(param.get("learnerId"));
        Long scheId = TypeUtils.castToLong(param.get("scheId"));
        try {
            CoachEvaluate result = coachEvaluateService.insert(serviceAttitude,speciality,like,complain,evaluation,learnerId,scheId);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }

    }

}
