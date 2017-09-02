package com.alienlab.catpower.service.impl;

import com.alibaba.fastjson.JSON;
import com.alienlab.catpower.domain.*;
import com.alienlab.catpower.repository.BuyCourseRepository;
import com.alienlab.catpower.repository.CourseSchedulingRepository;
import com.alienlab.catpower.repository.LearnerChargeRepository;
import com.alienlab.catpower.service.*;
import com.alienlab.catpower.web.wechat.service.WechatUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service Implementation for managing LearnerCharge.
 */
@Service
@Transactional
public class LearnerChargeServiceImpl implements LearnerChargeService{

    private final Logger log = LoggerFactory.getLogger(LearnerChargeServiceImpl.class);

    private final LearnerChargeRepository learnerChargeRepository;

    @Autowired
    CourseSchedulingService courseSchedulingService;

    @Autowired
    WechatUserService wechatUserService;

    @Autowired
    LearnerService learnerService;

    @Autowired
    BuyCourseService buyCourseService;
    @Autowired
    BuyCourseRepository buyCourseRepository;

    @Autowired
    CourseService courseService;

    @Autowired
    CourseSchedulingRepository courseSchedulingRepository;

    public LearnerChargeServiceImpl(LearnerChargeRepository learnerChargeRepository) {
        this.learnerChargeRepository = learnerChargeRepository;
    }

    /**
     * Save a learnerCharge.
     *
     * @param learnerCharge the entity to save
     * @return the persisted entity
     */
    @Override
    public LearnerCharge save(LearnerCharge learnerCharge) {
        log.debug("Request to save LearnerCharge : {}", learnerCharge);
        ZonedDateTime dateTime = ZonedDateTime.now();
        learnerCharge.setChargeTime(dateTime);
        LearnerCharge result = learnerChargeRepository.save(learnerCharge);
        return result;
    }

    /**
     *  Get all the learnerCharges.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LearnerCharge> findAll(Pageable pageable) {
        log.debug("Request to get all LearnerCharges");
        Page<LearnerCharge> result = learnerChargeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one learnerCharge by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public LearnerCharge findOne(Long id) {
        log.debug("Request to get LearnerCharge : {}", id);
        LearnerCharge learnerCharge = learnerChargeRepository.findOne(id);
        return learnerCharge;
    }

    /**
     *  Delete the  learnerCharge by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LearnerCharge : {}", id);
        learnerChargeRepository.delete(id);
    }

    @Override
    public List<LearnerCharge> getLeanersBySche(long scheId) throws Exception {
        CourseScheduling sche=courseSchedulingService.findOne(scheId);
        if(sche==null){
            throw new Exception("未找到编码为："+scheId+" 的排课信息");
        }
        List<LearnerCharge> result=learnerChargeRepository.findLearnerChargesByCourseScheduling(sche);
        return result;
    }

    @Override
    public List<LearnerCharge> getLeanersBySche(CourseScheduling sche) throws Exception {
        if(sche==null){
            throw new Exception("未找到排课信息");
        }
        List<LearnerCharge> result=learnerChargeRepository.findLearnerChargesByCourseScheduling(sche);
        return result;
    }

    @Override
    public LearnerCharge chargeCourse(String openid, Long scheId) throws Exception {

        Learner learner=learnerService.findByOpenid(openid);
        if(learner==null){
            throw new Exception("未找到学员注册信息，openid:"+openid);
        }
        CourseScheduling sche=courseSchedulingService.findOne(scheId);
        if(sche==null){
            throw new Exception("未找到编码为："+scheId+" 的排课信息");
        }
        return chargeCourse(learner,sche);
    }

    @Override
    public LearnerCharge chargeCourse(Learner learner, CourseScheduling sche) throws Exception {

        LearnerCharge learnerCharge = learnerChargeRepository.findLearnerChargeByLearnerIdAndScheId(learner.getId(),sche.getId());
        if (learnerCharge!=null){
            throw new Exception("您已经签到此次课程！");
        }
        //需要校验用户是否具有此门课程的课次
        BuyCourse buyCourse=buyCourseService.getCourseByLeanerAndCourse(learner,sche.getCourse().getId());
        if(buyCourse==null){
            throw new Exception("账户下没有找到此课程");
        }

        if(buyCourse.getRemainClass()>0){
            long remain=buyCourse.getRemainClass();
            remain=remain-1;
            LearnerCharge charge=new LearnerCharge();
            charge.setLearner(learner);
            Long count = sche.getSignInCount();
            count = count +1;
            sche.setSignInCount(count);
            charge.setCourseScheduling(sche);
            charge.setCourse(sche.getCourse());
            charge.setChargeTime(ZonedDateTime.now());
            charge.setCoach(sche.getCoach());
            charge.setBuyCourseId(String.valueOf(buyCourse.getId()));
            if(buyCourse.getCoach()==null){//卡券兑换的课程没有绑定教练，在签到时，绑定当前教练。
                buyCourse.setCoach(sche.getCoach());
            }
            charge.setRemainNumber(remain);
            System.out.println("charge course??>>>>>"+JSON.toJSONString(charge));
            charge=learnerChargeRepository.save(charge);
            buyCourse.setRemainClass(remain);
            buyCourse=buyCourseRepository.save(buyCourse);
            learner.setRecentlySignin(ZonedDateTime.now());
            if(learner.getFirstTotime()==null){
                learner.setFirstTotime(ZonedDateTime.now());
            }
            Long exp=learner.getExperience();
            if(exp==null)exp=0L;
            exp+=(long)(sche.getCourse().getCoursePrices()/sche.getCourse().getTotalClassHour()*1L);
            learner.setExperience(exp);
            learnerService.save(learner);
            courseSchedulingService.save(sche);
            return charge;
        }else{
            throw new Exception("课程课时已用完");
        }
    }

    @Override
    public boolean addLearnerCharge(Long id, ZonedDateTime chargeTime, String buyCourseId, String chargePeople, Long remainNumber, Long learner, Course course, Coach coach) {
        return false;
    }


    //查询全部已签到的课程
    @Override
    public List<LearnerCharge> findLeanerChargeByLearnerId(Long learnerId) throws Exception{
        Learner learner = learnerService.findOne(learnerId);
        if (learner==null){
            throw new Exception("没有找到该学员信息");
        }
        System.out.println("==========="+learner);

        return learnerChargeRepository.findLearnerChargeByLearner(learner);
    }

    //查询课程签到记录
    @Override
    public List<LearnerCharge> findLearnerChargeByCourseIdAndLearnerId(Long courseId, Long learnerId) {
        Learner learner = learnerService.findOne(learnerId);
        if (learner==null){
            try {
                throw new Exception("没有找到该学员信息");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Course course = courseService.findOne(courseId);
        if (course==null){
            try {
                throw new Exception("没有找到该学员信息");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return learnerChargeRepository.findLearnerChargeByCourseAndLearner(course,learner);
    }
}
