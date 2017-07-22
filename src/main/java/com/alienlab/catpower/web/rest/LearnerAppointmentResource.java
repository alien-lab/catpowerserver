package com.alienlab.catpower.web.rest;
import com.alienlab.catpower.domain.LearnerAppointment;
import com.alienlab.catpower.domain.LearnerCharge;
import com.alienlab.catpower.service.LearnerAppointmentService;
import com.alienlab.catpower.web.rest.util.ExecResult;
import com.alienlab.catpower.web.rest.util.HeaderUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
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
    public ResponseEntity<LearnerAppointment> createLearnerCharge(@RequestBody LearnerAppointment learnerAppointment) throws URISyntaxException {
        log.debug("REST request to save LearnerCharge : {}", learnerAppointment);
        if (learnerAppointment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new learnerCharge cannot already have an ID")).body(null);
        }
        LearnerAppointment result = learnerAppointmentService.save(learnerAppointment);
        return ResponseEntity.created(new URI("/api/learner-appointment/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
