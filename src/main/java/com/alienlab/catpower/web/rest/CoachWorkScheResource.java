package com.alienlab.catpower.web.rest;

import com.alibaba.fastjson.util.TypeUtils;
import com.alienlab.catpower.domain.CoachWorkSche;
import com.alienlab.catpower.service.CoachWorkScheService;
import com.alienlab.catpower.web.rest.util.ExecResult;
import com.alienlab.catpower.web.rest.util.HeaderUtil;
import com.alienlab.catpower.web.rest.util.PaginationUtil;
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
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * user :Master Cll
 * time :2017/7/22
 */
@RestController
@RequestMapping("/api")
public class CoachWorkScheResource {

    private final Logger log = LoggerFactory.getLogger(CoachWorkScheResource.class);

    private static final String ENTITY_NAME = "coachWorkSche";

    private final CoachWorkScheService coachWorkScheService;

    public CoachWorkScheResource(CoachWorkScheService coachWorkScheService) {
        this.coachWorkScheService = coachWorkScheService;
    }

    /**
     * POST  /courses : Create a new course.
     *
     * @param coachWorkSche the course to create
     * @return the ResponseEntity with status 201 (Created) and with body the new course, or with status 400 (Bad Request) if the course has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/courseworksche")
    @Timed
    public ResponseEntity<CoachWorkSche> createCourse(@RequestBody CoachWorkSche coachWorkSche) throws URISyntaxException {
        log.debug("REST request to save Course : {}", coachWorkSche);
        if (coachWorkSche.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new course cannot already have an ID")).body(null);
        }
        CoachWorkSche result = coachWorkScheService.save(coachWorkSche);
        return ResponseEntity.created(new URI("/api/courseworksche/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /courses : Updates an existing course.
     *
     * @param coachWorkSche the course to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated course,
     * or with status 400 (Bad Request) if the course is not valid,
     * or with status 500 (Internal Server Error) if the course couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/courseworksche")
    @Timed
    public ResponseEntity<CoachWorkSche> updateCourse(@RequestBody CoachWorkSche coachWorkSche) throws URISyntaxException {
        log.debug("REST request to update Course : {}", coachWorkSche);
        if (coachWorkSche.getId() == null) {
            return createCourse(coachWorkSche);
        }
        CoachWorkSche result = coachWorkScheService.save(coachWorkSche);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, coachWorkSche.getId().toString()))
            .body(result);
    }

    /**
     * GET  /courses : get all the courses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courses in body
     */
    @GetMapping("/courseworksche")
    @Timed
    public ResponseEntity<List<CoachWorkSche>> getAllCourses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Courses");
        Page<CoachWorkSche> page = coachWorkScheService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/courseworksche");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /courses/:id : get the "id" course.
     *
     * @param id the id of the course to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the course, or with status 404 (Not Found)
     */
    @GetMapping("/courseworksche/{id}")
    @Timed
    public ResponseEntity<CoachWorkSche> getCourse(@PathVariable Long id) {
        log.debug("REST request to get Course : {}", id);
        CoachWorkSche coachWorkSche = coachWorkScheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(coachWorkSche));
    }

    /**
     * DELETE  /courses/:id : delete the "id" course.
     *
     * @param id the id of the course to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/courseworksche/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.debug("REST request to delete Course : {}", id);
        coachWorkScheService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @ApiOperation(value = "根据教练id查询对应的排班信息")
    @GetMapping("/courseworksche/info/{coachId}")
    public ResponseEntity getCoachWorkScheInfo(@PathVariable Long coachId){
        try {
            List<CoachWorkSche> list = coachWorkScheService.findCoachWorkScheByCoachId(coachId);
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }
    @ApiOperation(value = "获取所有的教练排班记录")
    @GetMapping("/coachworksches")
    public ResponseEntity getAllSche(){
        List<CoachWorkSche> result = coachWorkScheService.findAll();
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "根据教练排班日期查询")
    @GetMapping("/coachworksche/workDate")
    public ResponseEntity getCoachesByDate(@RequestParam String workDate){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        Date d=null;
        try {
            d=sdf.parse(workDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ZonedDateTime workZonedDate = ZonedDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
        try {
            List<CoachWorkSche> result = coachWorkScheService.getCoachesByWorkDate(workZonedDate);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);

        }
    }

    @ApiOperation("插入教练排班信息")
    @PostMapping("/courseworksche/info")
    public ResponseEntity insertLearnerInfo(@RequestBody Map map){
        int workWeekday= TypeUtils.castToInt(map.get("workWeekday"));
        Long coachId = TypeUtils.castToLong(map.get("coachId"));
        String time=TypeUtils.castToString(map.get("time"));
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date d=null;
        try {
            d=sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ZonedDateTime workTime = ZonedDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
        try {
            CoachWorkSche result=coachWorkScheService.createCoachWorkSche(workTime,workWeekday,coachId);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);

        }
    }

    @ApiOperation("查询一个月教练的排班时间")
    @GetMapping("/courseworksche/coach")
    public ResponseEntity getCoachByMonth(@RequestParam String firstTime,@RequestParam String finalTime){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date d1=null;
        Date d2=null;
        try {
            d1=sdf.parse(firstTime);
            d2=sdf.parse(finalTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ZonedDateTime firstDay = ZonedDateTime.ofInstant(d1.toInstant(), ZoneId.systemDefault());
        ZonedDateTime finalDay = ZonedDateTime.ofInstant(d2.toInstant(), ZoneId.systemDefault());
        try {
            List result=coachWorkScheService.getCoachByTime(firstDay,finalDay);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);

        }
    }

}
