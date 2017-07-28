package com.alienlab.catpower.web.rest;
import com.alibaba.fastjson.util.TypeUtils;
import com.alienlab.catpower.domain.LearnerAppointment;
import com.alienlab.catpower.service.LearnerAppointmentService;
import com.alienlab.catpower.web.rest.util.ExecResult;
import com.alienlab.catpower.web.rest.util.HeaderUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing BuyCourse.
 */
@RestController
@RequestMapping("/api")
public class LearnerAppointmentResource {

    private final Logger log = LoggerFactory.getLogger(LearnerAppointmentResource.class);

    private static final String ENTITY_NAME = "learnerAppointment";

    private final LearnerAppointmentService learnerAppointmentService;



    public LearnerAppointmentResource(LearnerAppointmentService learnerAppointmentService) {
        this.learnerAppointmentService = learnerAppointmentService;
    }

    @ApiOperation("获取当前学员预约记录")
    @GetMapping("/learner-appointment/allRecord/{learnerId}")
    public ResponseEntity getAppointment(@PathVariable Long learnerId){
        try {
            Map map=learnerAppointmentService.getAppointment(learnerId);
            return ResponseEntity.ok().body(map);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation("学员预约")
    @PostMapping("/learner-appointment")
    public ResponseEntity createLearnerAppointment(@RequestBody Map map) throws URISyntaxException {
        Long buyCourseId = TypeUtils.castToLong(map.get("buyCourseId"));
        String t=TypeUtils.castToString(map.get("appointmentDate"));
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date d=null;
        try {
            d=sdf.parse(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ZonedDateTime appointmentDate = ZonedDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
        String appointmentMemo = TypeUtils.castToString(map.get("appointmentMemo"));
        LearnerAppointment result = null;
        try {
            result = learnerAppointmentService.save(buyCourseId,appointmentDate,"预约中",appointmentMemo);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

    }


    @ApiOperation(value = "学员取消预约信息")
    @DeleteMapping("/learner-appointment/{id}")
    public ResponseEntity<Void> deleteBuyCourse(@PathVariable Long id) {
        log.debug("REST request to delete BuyCourse : {}", id);
        learnerAppointmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @ApiOperation(value = "教练处理学员预约信息")
    @PutMapping("/learner-appointment")
    public ResponseEntity updateLearnerAppointment(@RequestBody Map map) {
        Long appointmentId = TypeUtils.castToLong(map.get("appointmentId"));
        String appointmentResult=TypeUtils.castToString(map.get("appointmentResult"));
        try {
            LearnerAppointment learnerAppointment=learnerAppointmentService.update(appointmentId,appointmentResult);
            return ResponseEntity.ok().body(learnerAppointment);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation("根据学员预约Id获取学员预约记录")
    @GetMapping("/learner-appointment/{learnerappointmentId}")
    public ResponseEntity getLearnerAppointmentById(@PathVariable Long learnerappointmentId){
        try {
            LearnerAppointment result=learnerAppointmentService.findLearnerAppointmentById(learnerappointmentId);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation("根据教练Id获取该教练的预约记录")
    @GetMapping("/learner-appointment/coach/{coachId}")
    public ResponseEntity getAppointmentByCoachId(@PathVariable Long coachId){
        try {
            Map map=learnerAppointmentService.getAppointmentByCoachId(coachId);
            return ResponseEntity.ok().body(map);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation("根据教练Id获取该教练的预约中的记录")
    @GetMapping("/learner-appointment/learnerbyidandtime")
    public ResponseEntity getAppointmentByIdAndTime(@RequestParam Long coachId,@RequestParam String appointmentDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(appointmentDate);
            ZonedDateTime  appointmentTime = ZonedDateTime.ofInstant(d.toInstant(),ZoneId.systemDefault());
            List result = learnerAppointmentService.findLearnerAppointmentByCoachIdAndAppointmentDate(coachId,appointmentTime);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }




}
