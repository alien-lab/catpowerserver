package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.CoachWorkSche;
import com.alienlab.catpower.domain.Course;
import com.alienlab.catpower.domain.LearnerAppointment;

import java.util.Map;

/**
 * Created by LuDan on 2017/7/12.
 */
public interface LearnerAppointmentService {

    /**
     * Save a learnerAppointment.
     *
     * @param learnerAppointment the entity to save
     * @return the persisted entity
     */
    LearnerAppointment save(LearnerAppointment learnerAppointment);

    /**
     * 根据学员ID获取该学员全部预约信息
     * @param learnerId
     * @return
     * @throws Exception
     */
    Map getAppointment(Long learnerId) throws Exception;

}
