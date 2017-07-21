package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.BuyCourse;
import com.alienlab.catpower.domain.Learner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service Interface for managing BuyCourse.
 */
public interface BuyCourseService {

    /**
     * Save a buyCourse.
     *
     * @param buyCourse the entity to save
     * @return the persisted entity
     */
    BuyCourse save(BuyCourse buyCourse);

    /**
     *  Get all the buyCourses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BuyCourse> findAll(Pageable pageable);

    /**
     *  Get the "id" buyCourse.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BuyCourse findOne(Long id);

    /**
     *  Delete the "id" buyCourse.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    Page<BuyCourse> getTodayData(Pageable page) throws Exception;
    //今日售课人数及总金额
    Map getTodayCountByDate(Date date) throws ParseException;

    //获取指定人购买的指定课程
    BuyCourse getCourseByLeanerAndCourse(Learner learner, Long courseId) throws Exception;

    /**
     * 根据学员Id获取该学员全部教练信息
     * @param learnerId
     * @return
     * @throws Exception
     */
    List getAllCoachByLearnerId(Long learnerId) throws Exception;

    //查询我的课程
    List<BuyCourse> findBuyCourseByLearnerId(Long learnerId) throws Exception;

    //查询我的可用的课程
    List<BuyCourse> findUseBuyCourseByLearnerId(Long learnerId) throws Exception;

    //查询我的不可用的课程
    List<BuyCourse> findNotUseBuyCourseByLearnerId(Long learnerId) throws Exception;

    //查询支付方式
    List<BuyCourse> getPaymentWay() throws Exception;

    //根据教练ID查询教练的所有课程
    List getCoachCourseByCoachId(Long coachId) throws Exception;

}
