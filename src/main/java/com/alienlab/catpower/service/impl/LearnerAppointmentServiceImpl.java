package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.domain.*;
import com.alienlab.catpower.repository.*;
import com.alienlab.catpower.service.BuyCourseService;
import com.alienlab.catpower.service.LearnerAppointmentService;
import com.alienlab.catpower.service.LearnerService;
import com.alienlab.catpower.web.wechat.service.WechatMessageService;
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
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private WechatMessageService wechatMessageService;

    @Override
    public LearnerAppointment save(Long buyCourseId, ZonedDateTime appointmentDate, String appointmentResult, String appointmentMemo) throws Exception{
        if (buyCourseId == null){
            throw new Exception("参数解析异常！");
        }
        BuyCourse buyCourse = buyCourseRepository.findOne(buyCourseId);
        if (buyCourse == null){
            throw new Exception("没有找到对应的买课信息");
        }
        LearnerAppointment appointment = learnerAppointmentRepository.findLearnerAppointmentByAppointmentDateAndBuyCourse(appointmentDate,buyCourse);
        if (appointment != null){
            throw new Exception("您已经预约此课程!");
        }
        LearnerAppointment learnerAppointment = new LearnerAppointment();
        learnerAppointment.setAppointmentDate(appointmentDate);
        learnerAppointment.setAppointmentMemo(appointmentMemo);
        learnerAppointment.setBuyCourse(buyCourse);
        learnerAppointment.setAppointmentResult(appointmentResult);
        LearnerAppointment learnerAppoint=learnerAppointmentRepository.save(learnerAppointment);
        if (learnerAppoint!=null){
            wechatMessageService.sendAppointMsg(learnerAppoint.getId());
        }
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

    @Override
    public LearnerAppointment update(Long appointmentId,String appointmentResult) throws Exception {
        if (appointmentId==null || appointmentResult==null){
            throw new Exception("参数解析异常！");
        }
        LearnerAppointment learnerAppointment=learnerAppointmentRepository.findOne(appointmentId);
        if (learnerAppointment==null){
            throw new Exception("没有找到对应的预约信息");
        }
        learnerAppointment.setAppointmentResult(appointmentResult);
        LearnerAppointment result=learnerAppointmentRepository.save(learnerAppointment);
        if (result!=null){
            wechatMessageService.sendAppointResultMsg(result.getId());
        }
        return result;
    }

    @Override
    public LearnerAppointment findLearnerAppointmentByBuyCourseIdAndAppointmentDate(Long buyCourseId, ZonedDateTime appointmentDate) throws Exception{
        if (buyCourseId==null){
            throw new Exception("参数解析异常！");
        }
        BuyCourse buyCourse = buyCourseRepository.findOne(buyCourseId);
        if (buyCourse==null){
            throw new Exception("没有购买对应的课程！");
        }
        return learnerAppointmentRepository.findLearnerAppointmentByAppointmentDateAndBuyCourse(appointmentDate,buyCourse);
    }

    @Override
    public LearnerAppointment findLearnerAppointmentById(Long id) throws Exception {
        if (id == null){
           throw new Exception("参数解析异常！");
        }
        LearnerAppointment learnerAppointment=learnerAppointmentRepository.findOne(id);
        if (learnerAppointment==null){
            throw new Exception("没有对应的预约记录！");
        }
        return learnerAppointment;
    }

    @Override
    public Map getAppointmentByCoachId(Long coachId) throws Exception {
        Map map = new HashMap();
        List<LearnerAppointment> allAppointment=learnerAppointmentRepository.findAppointmentByCoachId(coachId);
        List appointing=new ArrayList();
        List appointed=new ArrayList();
        for (LearnerAppointment learnerAppointment:allAppointment){
            if (learnerAppointment.getAppointmentResult().equals("预约中")){
                appointing.add(learnerAppointment);
            }
            if (learnerAppointment.getAppointmentResult().equals("预约成功") || learnerAppointment.getAppointmentResult().equals("已约满")){
                appointed.add(learnerAppointment);
            }
        }
        map.put("allAppointment",allAppointment);
        map.put("appointing",appointing);
        map.put("appointed",appointed);
        return map;
    }

    //根据教练ID获取对应预约他的课进行中的预约信息
    @Override
    public List findLearnerAppointmentByCoachIdAndAppointmentDate(Long coachId, ZonedDateTime appointmentDate) throws Exception {
        List appointments = new ArrayList();
        if (coachId==null){
            throw new Exception("参数解析异常！");
        }
        Coach coach = coachRepository.findOne(coachId);
        if (coach==null){
            throw new Exception("该教练信息为空！");
        }
        List<BuyCourse> buyCourse = buyCourseRepository.findBuyCourseByCoach(coach);
        if (buyCourse.size()==0){
            throw new Exception("没找到对应的买课信息！");
        }
        for (BuyCourse buy : buyCourse){
            LearnerAppointment appointment = learnerAppointmentRepository.findLearnerAppointmentByAppointmentDateAndBuyCourse(appointmentDate,buy);
            if (appointment != null){
                System.out.println(appointment);
                String result = appointment.getAppointmentResult();
                if (result.equals("预约中")){
                    appointments.add(appointment);
                }
            }

        }
        return appointments;
    }
}
