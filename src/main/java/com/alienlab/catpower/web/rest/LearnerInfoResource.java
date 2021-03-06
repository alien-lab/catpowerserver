package com.alienlab.catpower.web.rest;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.alienlab.catpower.web.rest.util.ExecResult;
import com.codahale.metrics.annotation.Timed;
import com.alienlab.catpower.domain.LearnerInfo;
import com.alienlab.catpower.service.LearnerInfoService;
import com.alienlab.catpower.web.rest.util.HeaderUtil;
import com.alienlab.catpower.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiOperation;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @ApiOperation("找每节课后对应的教练建议")
    @PostMapping("/learner-infos/coachadvice")
    public ResponseEntity getCoachAdvices(@RequestBody Map map){
        try {
            Long learnerId = TypeUtils.castToLong(map.get("learnerId"));
            Long courseSchedulingId = TypeUtils.castToLong(map.get("courseSchedulingId"));
            LearnerInfo learnerInfo = learnerInfoService.findLearnerInfoByLearnerIdAndCourseSchedulingId(learnerId,courseSchedulingId);
            return ResponseEntity.ok().body(learnerInfo);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);

        }
    }


    @ApiOperation("查询学员的健身日志")
    @GetMapping("/learner-infos/fitlog/{learnerId}")
    public ResponseEntity getFitLog(@PathVariable Long learnerId){
        try {
            List<LearnerInfo> learnerInfo = learnerInfoService.findLearnerInfoByLearnerId(learnerId);
            return ResponseEntity.ok().body(learnerInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);

        }
    }

    @ApiOperation("插入学员健身信息")
    @PostMapping("/learner-infos/fitlog")
    public ResponseEntity insertLearnerInfo(@RequestBody String paramstr){
        JSONObject param= JSONObject.parseObject(paramstr);
        String exerciseData = param.getJSONObject("exerciseData").toJSONString();
        String bodyTestData = param.getJSONObject("bodyTestData").toJSONString();
        String coachAdvice = param.getJSONObject("coachAdvice").toJSONString();
        Long learnerId = param.getLong("learnerId");
        Long scheId = param.getLong("scheId");
        try {
            LearnerInfo learnerInfo = learnerInfoService.insertLearner(exerciseData,bodyTestData,coachAdvice,learnerId,scheId);
            return ResponseEntity.ok().body(learnerInfo);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);

        }
    }

    @ApiOperation("根据教练ID查询当天对应的已填写建议和未填写建议的学员")
    @GetMapping("/learner-infos/advicebyidandtime")
    public ResponseEntity findLearnerInfoByIdAndTime(@RequestParam Long coachId,@RequestParam String startTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d= sdf.parse(startTime);
            ZonedDateTime startDate = ZonedDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
            List<LearnerInfo> learnerInfo =  learnerInfoService.getLearnerInfoBySche(coachId,startDate);
            return ResponseEntity.ok().body(learnerInfo);
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

    }

}
