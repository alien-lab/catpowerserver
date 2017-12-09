package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.service.LearnerService;
import com.alienlab.catpower.web.rest.util.ExecResult;
import com.alienlab.catpower.web.rest.util.HeaderUtil;
import com.alienlab.catpower.web.rest.util.PaginationUtil;
import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import com.alienlab.catpower.web.wechat.service.WechatUserService;
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
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing Learner.
 */
@RestController
@RequestMapping("/api")
public class LearnerResource {

    private final Logger log = LoggerFactory.getLogger(LearnerResource.class);

    private static final String ENTITY_NAME = "learner";

    private final LearnerService learnerService;

    private final WechatUserService wechatUserService;

    public LearnerResource(
        LearnerService learnerService,
        WechatUserService wechatUserService)
    {

        this.learnerService = learnerService;
        this.wechatUserService=wechatUserService;
    }

    /**
     * POST  /learners : Create a new learner.
     *
     * @param learner the learner to create
     * @return the ResponseEntity with status 201 (Created) and with body the new learner, or with status 400 (Bad Request) if the learner has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/learners")
    @Timed
    public ResponseEntity<Learner> createLearner(@RequestBody Learner learner) throws URISyntaxException {
        log.debug("REST request to save Learner : {}", learner);
        if (learner.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new learner cannot already have an ID")).body(null);
        }
        Learner result = learnerService.save(learner);
        return ResponseEntity.created(new URI("/api/learners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/learners/wechat/{openid}")
    @Timed
    public ResponseEntity regLearnerFromWechat(@RequestBody Learner learner,@PathVariable String openid) throws URISyntaxException {
        log.debug("REST request to save Learner : {}", learner);
        if (learner.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new learner cannot already have an ID")).body(null);
        }
        learner.setRegistTime(ZonedDateTime.now());

        WechatUser wuser=wechatUserService.findUserByOpenid(openid);
        try {
            Learner existLearner=learnerService.findByOpenid(openid);
            if(existLearner!=null){
                learner.setWechatUser(wuser);
                Learner result = learnerService.save(learner);
                return ResponseEntity.ok(existLearner);
            }else{
                learner.setWechatUser(wuser);
                Learner result = learnerService.save(learner);
                return ResponseEntity.created(new URI("/api/learners/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                    .body(result);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }

    }

    /**
     * PUT  /learners : Updates an existing learner.
     *
     * @param learner the learner to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated learner,
     * or with status 400 (Bad Request) if the learner is not valid,
     * or with status 500 (Internal Server Error) if the learner couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/learners")
    @Timed
    public ResponseEntity<Learner> updateLearner(@RequestBody Learner learner) throws URISyntaxException {
        log.debug("REST request to update Learner : {}", learner);
        if (learner.getId() == null) {
            return createLearner(learner);
        }
        Learner result = learnerService.save(learner);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, learner.getId().toString()))
            .body(result);
    }

    /**
     * GET  /learners : get all the learners.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of learners in body
     */
    @GetMapping("/learners")
    @Timed
    public ResponseEntity<List<Learner>> getAllLearners(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Learners");
        Page<Learner> page = learnerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/learners");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /learners/:id : get the "id" learner.
     *
     * @param id the id of the learner to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the learner, or with status 404 (Not Found)
     */
    @GetMapping("/learners/{id}")
    @Timed
    public ResponseEntity<Learner> getLearner(@PathVariable Long id) {
        log.debug("REST request to get Learner : {}", id);
        Learner learner = learnerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(learner));
    }

    /**
     * DELETE  /learners/:id : delete the "id" learner.
     *
     * @param id the id of the learner to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/learners/{id}")
    @Timed
    public ResponseEntity<Void> deleteLearner(@PathVariable Long id) {
        log.debug("REST request to delete Learner : {}", id);
        learnerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/learners/count/today")
    public ResponseEntity getStatiscToday(){
        try {
            Map result=learnerService.learnCountStatiscByDate(new Date());
            return ResponseEntity.ok().body(result);
        } catch (ParseException e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation("获取学员首页信息")
    @GetMapping("/learner-index/learnInfo/{openid}")
    public ResponseEntity getLearnerIndexInfo(@PathVariable String openid){
        try {
            Map map=learnerService.getLearnerIndexInfo(openid);
            return ResponseEntity.ok().body(map);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }
    @ApiOperation(value = "获取绑定二维码")
    @GetMapping("/learners/qr/learner")
    public ResponseEntity getScheQrcode(@RequestParam Learner learner){
        try{
            QrInfo qr=learnerService.getLearnerBindQr(learner);
            return ResponseEntity.ok(qr);
        }catch(Exception e){
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation(value = "根据学员姓名查询")
    @GetMapping("/learners/learnerName")
    public ResponseEntity getLearnerByLearnerName(@RequestParam String learnerName){
        try {
            List<Learner> learner = learnerService.getLearnerBylearnerName(learnerName);
            return ResponseEntity.ok(learner);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

}
