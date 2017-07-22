package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.CoachWorkSche;
import com.alienlab.catpower.domain.Course;
import com.alienlab.catpower.domain.LearnerAppointment;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Created by LuDan on 2017/7/12.
 */
public interface LearnerAppointmentService {

    /**
     * Save a learnerAppointment
     * @return the persisted entity
     */
    LearnerAppointment save(Long buyCourseId, ZonedDateTime appointmentDate,String appointmentResult, String appointmentMemo) throws Exception;

    /**
     * 根据学员ID获取该学员全部预约信息
     * @param learnerId
     * @return
     * @throws Exception
     */
    Map getAppointment(Long learnerId) throws Exception;



}
