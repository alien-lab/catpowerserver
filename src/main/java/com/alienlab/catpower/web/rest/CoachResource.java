package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.service.CoachService;
import com.alienlab.catpower.web.rest.util.ExecResult;
import com.alienlab.catpower.web.rest.util.HeaderUtil;
import com.alienlab.catpower.web.rest.util.PaginationUtil;
import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing Coach.
 */
@RestController
@RequestMapping("/api")
public class CoachResource {

    private final Logger log = LoggerFactory.getLogger(CoachResource.class);

    private static final String ENTITY_NAME = "coach";

    private final CoachService coachService;

    public CoachResource(CoachService coachService) {
        this.coachService = coachService;
    }

    /**
     * POST  /coaches : Create a new coach.
     *
     * @param coach the coach to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coach, or with status 400 (Bad Request) if the coach has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/coaches")
    @Timed
    public ResponseEntity<Coach> createCoach(@RequestBody Coach coach) throws URISyntaxException {
        log.debug("REST request to save Coach : {}", coach);
        if (coach.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new coach cannot already have an ID")).body(null);
        }
        Coach result = coachService.save(coach);
        return ResponseEntity.created(new URI("/api/coaches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coaches : Updates an existing coach.
     *
     * @param coach the coach to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coach,
     * or with status 400 (Bad Request) if the coach is not valid,
     * or with status 500 (Internal Server Error) if the coach couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/coaches")
    @Timed
    public ResponseEntity<Coach> updateCoach(@RequestBody Coach coach) throws URISyntaxException {
        log.debug("REST request to update Coach : {}", coach);
        if (coach.getId() == null) {
            return createCoach(coach);
        }
        Coach result = coachService.save(coach);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, coach.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coaches : get all the coaches.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of coaches in body
     */
    @GetMapping("/coaches")
    @Timed
    public ResponseEntity<List<Coach>> getAllCoaches(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Coaches");
        Page<Coach> page = coachService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/coaches");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /coaches/:id : get the "id" coach.
     *
     * @param id the id of the coach to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coach, or with status 404 (Not Found)
     */
    @GetMapping("/coaches/{id}")
    @Timed
    public ResponseEntity<Coach> getCoach(@PathVariable Long id) {
        log.debug("REST request to get Coach : {}", id);
        Coach coach = coachService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(coach));
    }

    /**
     * DELETE  /coaches/:id : delete the "id" coach.
     *
     * @param id the id of the coach to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/coaches/{id}")
    @Timed
    public ResponseEntity<Void> deleteCoach(@PathVariable Long id) {
        log.debug("REST request to delete Coach : {}", id);
        coachService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     *根据教练ID查询教练的信息
     */
    @ApiOperation(value = "根据教练ID查询教练的信息")
    @GetMapping("/coaches/info/{id}")
    public ResponseEntity getCoachInfo(@PathVariable Long id){
        try {
           Map result = coachService.getCoachByCoachId(id);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er = new ExecResult(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation(value = "获取绑定教练二维码")
    @GetMapping("/coaches/qr/coach")
    public ResponseEntity getCoachQrcode(@RequestParam Coach coach){
        try{
            QrInfo qr=coachService.getCoachBindQr(coach);
            return ResponseEntity.ok(qr);
        }catch(Exception e){
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

}
