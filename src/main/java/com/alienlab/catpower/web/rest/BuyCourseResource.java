package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.domain.BuyCourse;
import com.alienlab.catpower.service.BuyCourseService;
import com.alienlab.catpower.web.rest.util.ExecResult;
import com.alienlab.catpower.web.rest.util.HeaderUtil;
import com.alienlab.catpower.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing BuyCourse.
 */
@RestController
@RequestMapping("/api")
public class BuyCourseResource {

    private final Logger log = LoggerFactory.getLogger(BuyCourseResource.class);

    private static final String ENTITY_NAME = "buyCourse";

    private final BuyCourseService buyCourseService;

    public BuyCourseResource(BuyCourseService buyCourseService) {
        this.buyCourseService = buyCourseService;
    }

    /**
     * POST  /buy-courses : Create a new buyCourse.
     *
     * @param buyCourse the buyCourse to create
     * @return the ResponseEntity with status 201 (Created) and with body the new buyCourse, or with status 400 (Bad Request) if the buyCourse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/buy-courses")
    @Timed
    public ResponseEntity<BuyCourse> createBuyCourse(@RequestBody BuyCourse buyCourse) throws URISyntaxException {
        log.debug("REST request to save BuyCourse : {}", buyCourse);
        if (buyCourse.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new buyCourse cannot already have an ID")).body(null);
        }
        BuyCourse result = buyCourseService.save(buyCourse);
        return ResponseEntity.created(new URI("/api/buy-courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /buy-courses : Updates an existing buyCourse.
     *
     * @param buyCourse the buyCourse to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated buyCourse,
     * or with status 400 (Bad Request) if the buyCourse is not valid,
     * or with status 500 (Internal Server Error) if the buyCourse couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/buy-courses")
    @Timed
    public ResponseEntity<BuyCourse> updateBuyCourse(@RequestBody BuyCourse buyCourse) throws URISyntaxException {
        log.debug("REST request to update BuyCourse : {}", buyCourse);
        if (buyCourse.getId() == null) {
            return createBuyCourse(buyCourse);
        }
        BuyCourse result = buyCourseService.save(buyCourse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, buyCourse.getId().toString()))
            .body(result);
    }

    /**
     * GET  /buy-courses : get all the buyCourses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of buyCourses in body
     */
    @GetMapping("/buy-courses")
    @Timed
    public ResponseEntity<List<BuyCourse>> getAllBuyCourses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of BuyCourses");
        Page<BuyCourse> page = buyCourseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/buy-courses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /buy-courses/:id : get the "id" buyCourse.
     *
     * @param id the id of the buyCourse to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the buyCourse, or with status 404 (Not Found)
     */
    @GetMapping("/buy-courses/{id}")
    @Timed
    public ResponseEntity<BuyCourse> getBuyCourse(@PathVariable Long id) {
        log.debug("REST request to get BuyCourse : {}", id);
        BuyCourse buyCourse = buyCourseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(buyCourse));
    }

    /**
     * DELETE  /buy-courses/:id : delete the "id" buyCourse.
     *
     * @param id the id of the buyCourse to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/buy-courses/{id}")
    @Timed
    public ResponseEntity<Void> deleteBuyCourse(@PathVariable Long id) {
        log.debug("REST request to delete BuyCourse : {}", id);
        buyCourseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     *
     * @param index
     * @param size
     * @return
     */
    @ApiOperation(value="获取今日授课记录")
    @ApiImplicitParams({
        @ApiImplicitParam(name="index",value="分页页码",required = true,paramType = "query"),
        @ApiImplicitParam(name="size",value="分页长度",required = true,paramType = "query")
    })
    @GetMapping("/buy-courses/today")
    public ResponseEntity getTodayData(@RequestParam int index,@RequestParam int size) {
        try {
            Page<BuyCourse> result = buyCourseService.getTodayData(new PageRequest(index, size));
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er = new ExecResult(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }


    @ApiOperation("获取今日销售的数量及总金额")
    @GetMapping("/buy-courses/today/count")
    public ResponseEntity getCountToday(){
        try {
            Map result = buyCourseService.getTodayCountByDate(new Date());
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation("获取当前学员全部教练信息")
    @GetMapping("/buy-course/allCoach/{learnerId}")
    public ResponseEntity getAllCoach(@PathVariable Long learnerId){
        try {
            List coachList=buyCourseService.getAllCoachByLearnerId(learnerId);
            return ResponseEntity.ok().body(coachList);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation("查询我的全部课程、可用课程、完结课程")
    @GetMapping("/buy-courses/mycourse/{learnerId}")
    public ResponseEntity getMyCourse(@PathVariable Long learnerId){
        //查询全部课程
        List<BuyCourse> allCourse = null;
        List<BuyCourse> startCourse = null;
        List<BuyCourse> finishCourse = null;
        try {
            allCourse = buyCourseService.findBuyCourseByLearnerId(learnerId);
            startCourse = buyCourseService.findUseBuyCourseByLearnerId(learnerId);
            finishCourse = buyCourseService.findNotUseBuyCourseByLearnerId(learnerId);
            Map newMap = new HashMap();
            newMap.put("allCourse",allCourse);
            newMap.put("startCourse",startCourse);
            newMap.put("finishCourse",finishCourse);
            return ResponseEntity.ok().body(newMap);
        } catch (Exception e) {
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation(value = "获取所有的买课的支付方式")
    @GetMapping("/buy-courses/paymentWay")
    public ResponseEntity getAllPaymentWay(){
        try {
            List<BuyCourse> result = buyCourseService.getPaymentWay();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation(value = "根据教练ID获取教练的课程")
    @GetMapping("/buy-courses/courses/coachId")
    public ResponseEntity getCourseByCoachId(@RequestParam Long coachId){
        try {
            List result = buyCourseService.getCoachCourseByCoachId(coachId);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }
}
