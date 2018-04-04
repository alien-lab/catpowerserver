package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.BuyCourse;
import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service Interface for managing Learner.
 */
public interface LearnerService {

    /**
     * Save a learner.
     * 保存一名学员
     * @param learner the entity to save
     * @return the persisted entity
     */
    Learner save(Learner learner);

    /**
     *  Get all the learners.
     *  得到所有的学员
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Learner> findAll(Pageable pageable);

    /**
     *  Get the "id" learner.
     *  得到指定id的学员
     *  @param id the id of the entity
     *  @return the entity
     */
    Learner findOne(Long id);

    /**
     *  Delete the "id" learner.
     *  删除指定id的学员
     *  @param id the id of the entity
     */
    void delete(Long id);
    Map learnCountStatiscByDate(Date date) throws ParseException;

    Learner findByOpenid(String openid) throws ParseException;

    Learner findByPhone(String phone);

    QrInfo getLearnerBindQr(Long learnerId) throws Exception;
    QrInfo getLearnerBindQr(Learner learner) throws Exception;

    //绑定学员微信
    Learner bindWechatUser(String openid,Long learnerId) throws Exception;
    Learner bindWechatUser(WechatUser wechatUser,Learner learner) throws Exception;

    Map getLearnerIndexInfo(String openid) throws Exception;

    QrInfo getLearnerBindQr(String openid) throws Exception;

    //根据学员名字查询
    List<Learner> getLearnerBylearnerName(String learneName) throws Exception;

    //获取学员预约，一个学员只能存在一个有效预约
    CourseScheduling getLearnerAppoint(Learner learner);

    List<Coach> findTeachers(Learner learner);


    //根据教练和学员，找到学员账户下的课程
    BuyCourse getBuyCourseByCoachAndLearner(Learner learner, Coach coach);

}
