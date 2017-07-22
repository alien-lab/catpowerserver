package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.domain.LearnerAppointment;
import com.alienlab.catpower.repository.LearnerAppointmentRepository;
import com.alienlab.catpower.service.LearnerAppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LuDan on 2017/7/12.
 */
@Service
@Transactional
public class LearnerAppointmentServiceImpl implements LearnerAppointmentService {
    private final Logger log = LoggerFactory.getLogger(LearnerAppointmentService.class);


    @Autowired
    private LearnerAppointmentRepository learnerAppointmentRepository;

    @Override
    public LearnerAppointment save(LearnerAppointment learnerAppointment) {
        return learnerAppointmentRepository.save(learnerAppointment);
    }

    @Override
    public Map getAppointment(Long learnerId) throws Exception{
        List<LearnerAppointment> allAppointment=learnerAppointmentRepository.findAppointmentByLearner(learnerId);
        List<LearnerAppointment> appointing=learnerAppointmentRepository.findAppointingByLearner(learnerId);
        List<LearnerAppointment> appointed=learnerAppointmentRepository.findAppointedByLearner(learnerId);
        Map map=new HashMap();
        map.put("allAppointment",allAppointment);
        map.put("appointing",appointing);
        map.put("appointed",appointed);
        return map;

    }
}
