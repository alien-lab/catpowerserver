package com.alienlab.catpower.service;

import java.util.List;

/**
 * Created by LuDan on 2017/7/12.
 */
public interface LearnerAppointmentService {

    /**
     * 根据学员ID获取该学员全部预约信息
     * @param learnerId
     * @return
     * @throws Exception
     */
    List getAppointment(Long learnerId) throws Exception;

}
