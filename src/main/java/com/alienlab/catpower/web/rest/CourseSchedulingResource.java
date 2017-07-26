package com.alienlab.catpower.web.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.domain.LearnerCharge;
import com.alienlab.catpower.service.CourseSchedulingService;
import com.alienlab.catpower.service.LearnerChargeService;
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
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CourseScheduling.
 */
@RestController
@RequestMapping("/api")
public class CourseSchedulingResource {

    private final Logger log = LoggerFactory.getLogger(CourseSchedulingResource.class);

    private static final String ENTITY_NAME = "courseScheduling";

    private final CourseSchedulingService courseSchedulingService;

    @Autowired
    LearnerChargeService learnerChargeService;

    public CourseSchedulingResource(CourseSchedulingService courseSchedulingService) {
        this.courseSchedulingService = courseSchedulingService;
    }

    /**
     * POST  /course-schedulings : Create a new courseScheduling.
     *
     * @param courseScheduling the courseScheduling to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courseScheduling, or with status 400 (Bad Request) if the courseScheduling has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-schedulings")
    @Timed
    public ResponseEntity<CourseScheduling> createCourseScheduling(@RequestBody CourseScheduling courseScheduling) throws URISyntaxException {
        log.debug("REST request to save CourseScheduling : {}", courseScheduling);
        if (courseScheduling.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new courseScheduling cannot already have an ID")).body(null);
        }
        CourseScheduling result = courseSchedulingService.save(courseScheduling);
        return ResponseEntity.created(new URI("/api/course-schedulings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /course-schedulings : Updates an existing courseScheduling.
     *
     * @param courseScheduling the courseScheduling to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseScheduling,
     * or with status 400 (Bad Request) if the courseScheduling is not valid,
     * or with status 500 (Internal Server Error) if the courseScheduling couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/course-schedulings")
    @Timed
    public ResponseEntity<CourseScheduling> updateCourseScheduling(@RequestBody CourseScheduling courseScheduling) throws URISyntaxException {
        log.debug("REST request to update CourseScheduling : {}", courseScheduling);
        if (courseScheduling.getId() == null) {
            return createCourseScheduling(courseScheduling);
        }
        CourseScheduling result = courseSchedulingService.save(courseScheduling);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseScheduling.getId().toString()))
            .body(result);
    }

    /**
     * GET  /course-schedulings : get all the courseSchedulings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courseSchedulings in body
     */
    @GetMapping("/course-schedulings")
    @Timed
    public ResponseEntity<List<CourseScheduling>> getAllCourseSchedulings(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CourseSchedulings");
        Page<CourseScheduling> page = courseSchedulingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-schedulings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /course-schedulings/:id : get the "id" courseScheduling.
     *
     * @param id the id of the courseScheduling to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseScheduling, or with status 404 (Not Found)
     */
    @ApiOperation("根据排课ID获取排课信息")
    @GetMapping("/course-schedulings/{id}")
    @Timed
    public ResponseEntity<CourseScheduling> getCourseScheduling(@PathVariable Long id) {
        log.debug("REST request to get CourseScheduling : {}", id);
        CourseScheduling courseScheduling = courseSchedulingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(courseScheduling));
    }

    /**
     * DELETE  /course-schedulings/:id : delete the "id" courseScheduling.
     *
     * @param id the id of the courseScheduling to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/course-schedulings/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourseScheduling(@PathVariable Long id) {
        log.debug("REST request to delete CourseScheduling : {}", id);
        courseSchedulingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @ApiOperation("获取今日教练排课数据")
    @GetMapping("/course-schedulings/today")
    public ResponseEntity getScheToday(){
        SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmss");
        Date base=new Date();
        String sd=sf.format(base);
        sd=sd.substring(0,8)+"000000";
        Date d1= null,d2=null;
        try {
            d1 = sf.parse(sd);
            d2=new Date(d1.getTime()+1000*60*60*24);
            ZonedDateTime zd1=ZonedDateTime.ofInstant(d1.toInstant(), ZoneId.systemDefault());
            ZonedDateTime zd2=ZonedDateTime.ofInstant(d2.toInstant(), ZoneId.systemDefault());
            List<CourseScheduling> sches=courseSchedulingService.getScheByDate(zd1,zd2);
            JSONArray result=new JSONArray();
            if(sches!=null&&sches.size()>0){
                for(CourseScheduling sche:sches){
                    JSONObject item=new JSONObject();
                    item.put("sche",sche);
                    List<LearnerCharge> learners=learnerChargeService.getLeanersBySche(sche);
                    item.put("learners",learners);
                    result.add(item);
                }
            }
            return ResponseEntity.ok().body(result);
        } catch (ParseException e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }


    }


    @ApiOperation("获取今日教练排课分页数据")
    @GetMapping("/course-schedulings/today/{index}/{size}")
    public ResponseEntity getScheTodayPage(@PathVariable int index,@PathVariable int size){
        SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmss");
        Date base=new Date();
        String sd=sf.format(base);
        sd=sd.substring(0,8)+"000000";
        Date d1= null,d2=null;
        try {
            d1 = sf.parse(sd);
            d2=new Date(d1.getTime()+1000*60*60*24);
            ZonedDateTime zd1=ZonedDateTime.ofInstant(d1.toInstant(), ZoneId.systemDefault());
            ZonedDateTime zd2=ZonedDateTime.ofInstant(d2.toInstant(), ZoneId.systemDefault());
            Page<CourseScheduling> sches=courseSchedulingService.getScheByDate(zd1,zd2,index,size);

            return ResponseEntity.ok().body(sches);
        } catch (ParseException e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }


    }
    /**
     *
     *  根据id更新上课的状态
     */
    @ApiOperation(value = "根据id更新上课的状态 ")
    @PutMapping("/course-schedulings/courseStatus/{id}")
    public ResponseEntity updateCourseStatus(@PathVariable Long id,@RequestBody String status){
        try {
            return ResponseEntity.ok().body(courseSchedulingService.updateCourseScheduling(id,status));
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }
    @ApiOperation(value = "获取排课签到二维码")
    @GetMapping("/course-schedulings/qr/scheId")
    public ResponseEntity getScheQrcode(@RequestParam Long scheId){
        try{
            QrInfo qr=courseSchedulingService.getScheQrcode(scheId);
            return ResponseEntity.ok(qr);
        }catch(Exception e){
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation(value = "根据教练的ID获取教练的排课记录")
    @GetMapping("/course-schedulings/courseScheByCoachId")
    public ResponseEntity getCourseSche(@RequestParam Long coachId){
        try {
            List<CourseScheduling> result = courseSchedulingService.getcourseSche(coachId);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation(value = "获取全部教练的排课记录")
    @GetMapping("/course-schedulings/courseScheduling")
    public ResponseEntity getcourseScheduling(){
        try {
            List list = courseSchedulingService.getCourseScheduling();
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation(value = "获取指定日期指定教练的排班记录")
    @GetMapping("/course-schedulings/courseSchedulingBytimeAndId")
    public ResponseEntity getCourseScheByIdAndTime(@RequestParam Long coachId,@RequestParam String startTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ss");
        Date d1 = null;
        Date d2 = null;
        ZonedDateTime start = null;
        List<CourseScheduling> result = new ArrayList<>();
        try {
            d1 = sdf.parse(startTime);
            List<CourseScheduling> result1 = courseSchedulingService.getcourseSche(coachId);
            if (result1==null){
                throw new Exception("该教练没有拍班信息！");
            }
            for (CourseScheduling scheduling :result1){
                String startTime2 = TypeUtils.castToString(scheduling.getStartTime());
                d2 = sdf.parse(startTime2);
                if (d1.getTime() == d2.getTime()){
                    result.add(scheduling);
                }
            }
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);

        }
    }


}
