package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.BuyCourse;
import com.alienlab.catpower.domain.CoachWorkSche;
import com.alienlab.catpower.domain.Course;
import com.alienlab.catpower.domain.LearnerAppointment;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by LuDan on 2017/7/12.
 */
public interface LearnerAppointmentService {

    /**
     * Save a learnerAppointment
     * 保存一条学员的预约信息
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

    /**
     *  Delete the "id" course.
     *  删除指定id的学员预约信息
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 更新预约结果
     * @param appointmentResult
     * @return
     * @throws Exception
     */
    LearnerAppointment update(Long appointmentId,String appointmentResult) throws Exception;

    //根据预约时间和买课ID去查询预约信息
    LearnerAppointment findLearnerAppointmentByBuyCourseIdAndAppointmentDate(Long buyCourseId,ZonedDateTime appointmentDate)throws Exception;

    LearnerAppointment findLearnerAppointmentById(Long id) throws Exception;

    Map getAppointmentByCoachId(Long coachId) throws Exception;

    List findLearnerAppointmentByCoachIdAndAppointmentDate(Long coachId,ZonedDateTime appointmentDate) throws Exception;
}
