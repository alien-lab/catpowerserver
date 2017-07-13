package com.alienlab.catpower.web.rest;
import com.alienlab.catpower.service.LearnerAppointmentService;
import com.alienlab.catpower.web.rest.util.ExecResult;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/learner-appointment/allRecord")
    public ResponseEntity getAppointment(@RequestParam Long learnerId){
        try {
            List list=learnerAppointmentService.getAppointment(learnerId);
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }
}
