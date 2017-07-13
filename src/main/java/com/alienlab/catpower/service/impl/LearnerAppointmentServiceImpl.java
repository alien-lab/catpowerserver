package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.domain.BuyCourse;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.domain.LearnerAppointment;
import com.alienlab.catpower.repository.BuyCourseRepository;
import com.alienlab.catpower.repository.LearnerAppointmentRepository;
import com.alienlab.catpower.service.BuyCourseService;
import com.alienlab.catpower.service.LearnerAppointmentService;
import com.alienlab.catpower.service.LearnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LuDan on 2017/7/12.
 */
@Service
@Transactional
public class LearnerAppointmentServiceImpl implements LearnerAppointmentService {

    @Autowired
    private LearnerAppointmentRepository learnerAppointmentRepository;

    @Override
    public List getAppointment(Long learnerId) throws Exception{
        List<LearnerAppointment> learnerAppointments=learnerAppointmentRepository.findAppointmentByLearner(learnerId);
        return learnerAppointments;

    }
}
