package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.domain.BuyCourse;
import com.alienlab.catpower.domain.CoachWorkSche;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.domain.LearnerAppointment;
import com.alienlab.catpower.repository.BuyCourseRepository;
import com.alienlab.catpower.repository.LearnerAppointmentRepository;
import com.alienlab.catpower.repository.LearnerRepository;
import com.alienlab.catpower.service.BuyCourseService;
import com.alienlab.catpower.service.LearnerAppointmentService;
import com.alienlab.catpower.service.LearnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
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
    private final Logger log = LoggerFactory.getLogger(LearnerAppointmentService.class);


    @Autowired
    private LearnerAppointmentRepository learnerAppointmentRepository;
    @Autowired
    private BuyCourseRepository buyCourseRepository;

    @Override
    public LearnerAppointment save(Long buyCourseId, ZonedDateTime appointmentDate, String appointmentResult, String appointmentMemo) throws Exception{
        if (buyCourseId == null){
            throw new Exception("参数解析异常！");
        }
        BuyCourse buyCourse = buyCourseRepository.findOne(buyCourseId);
        if (buyCourse == null){
            throw new Exception("没有找到对应的买课信息");
        }
        LearnerAppointment appoint = learnerAppointmentRepository.findOne(buyCourseId);
        if (appoint!=null){
            throw new Exception("您已经预约该课程！");
        }
        LearnerAppointment learnerAppointment = new LearnerAppointment();
        learnerAppointment.setAppointmentDate(appointmentDate);
        learnerAppointment.setAppointmentMemo(appointmentMemo);
        learnerAppointment.setBuyCourse(buyCourse);
        learnerAppointment.setAppointmentResult(appointmentResult);
        learnerAppointmentRepository.save(learnerAppointment);
        return null;
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

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Course : {}", id);
        learnerAppointmentRepository.delete(id);
    }
}
