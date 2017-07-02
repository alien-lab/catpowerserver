package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.domain.BuyCourse;
import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.service.BuyCourseService;
import com.alienlab.catpower.service.CourseSchedulingService;
import com.alienlab.catpower.service.LearnerChargeService;
import com.alienlab.catpower.domain.LearnerCharge;
import com.alienlab.catpower.repository.LearnerChargeRepository;
import com.alienlab.catpower.service.LearnerService;
import com.alienlab.catpower.web.wechat.service.WechatUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.text.ParseException;
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
            charge.setCourseScheduling(sche);
            charge.setCourse(sche.getCourse());
            charge.setChargeTime(ZonedDateTime.now());
            charge.setCoach(sche.getCoach());
            charge.setRemainNumber(remain);
            charge=learnerChargeRepository.save(charge);
            buyCourse.setRemainClass(remain);
            buyCourse=buyCourseService.save(buyCourse);
            learner.setRecentlySignin(ZonedDateTime.now());
            if(learner.getFirstTotime()==null){
                learner.setFirstTotime(ZonedDateTime.now());
            }
            long exp=learner.getExperience();
            exp+=sche.getCourse().getCoursePrices()/sche.getCourse().getTotalClassHour();
            learner.setExperience(exp);
            learnerService.save(learner);
            return charge;
        }else{
            throw new Exception("课程课时已用完");
        }
    }
}
