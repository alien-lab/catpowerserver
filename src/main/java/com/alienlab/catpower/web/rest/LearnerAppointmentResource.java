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
    public ResponseEntity<LearnerAppointment> createLearnerAppointment(@RequestBody Map map) throws URISyntaxException {
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
            result = learnerAppointmentService.save(buyCourseId,appointmentDate,appointmentMemo,"预约中");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.created(new URI("/api/learner-appointment/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
