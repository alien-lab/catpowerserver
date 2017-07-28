package com.alienlab.catpower.service.impl;

import com.alibaba.fastjson.util.TypeUtils;
import com.alienlab.catpower.domain.*;
import com.alienlab.catpower.repository.*;
import com.alienlab.catpower.service.CourseSchedulingService;
import com.alienlab.catpower.service.CourseService;
import com.alienlab.catpower.service.LearnerInfoService;
import com.alienlab.catpower.service.LearnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing LearnerInfo.
 */
@Service
@Transactional
public class LearnerInfoServiceImpl implements LearnerInfoService{

    private final Logger log = LoggerFactory.getLogger(LearnerInfoServiceImpl.class);

    private final LearnerInfoRepository learnerInfoRepository;

    @Autowired
    LearnerService learnerService;

    @Autowired
    CourseSchedulingService courseSchedulingService;

    @Autowired
    LearnerRepository learnerRepository;

    @Autowired
    CourseSchedulingRepository courseSchedulingRepository;

    @Autowired
    CoachRepository coachRepository;

    @Autowired
    LearnerChargeRepository learnerChargeRepository;

    public LearnerInfoServiceImpl(LearnerInfoRepository learnerInfoRepository) {
        this.learnerInfoRepository = learnerInfoRepository;
    }

    /**
     * Save a learnerInfo.
     *
     * @param learnerInfo the entity to save
     * @return the persisted entity
     */
    @Override
    public LearnerInfo save(LearnerInfo learnerInfo) {
        log.debug("Request to save LearnerInfo : {}", learnerInfo);
        LearnerInfo result = learnerInfoRepository.save(learnerInfo);
        return result;
    }

    /**
     *  Get all the learnerInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LearnerInfo> findAll(Pageable pageable) {
        log.debug("Request to get all LearnerInfos");
        Page<LearnerInfo> result = learnerInfoRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one learnerInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public LearnerInfo findOne(Long id) {
        log.debug("Request to get LearnerInfo : {}", id);
        LearnerInfo learnerInfo = learnerInfoRepository.findOne(id);
        return learnerInfo;
    }

    /**
     *  Delete the  learnerInfo by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LearnerInfo : {}", id);
        learnerInfoRepository.delete(id);
    }

    @Override
    public LearnerInfo findLearnerInfoByLearnerIdAndCourseSchedulingId(Long learnerId, Long courseSchedulingId) throws Exception{
        Learner learner = learnerService.findOne(learnerId);
        if(learner == null){
            throw new Exception("没有对应的学员信息");
        }
        CourseScheduling courseScheduling = courseSchedulingService.findOne(courseSchedulingId);
        if (courseScheduling  == null){
            throw new Exception("没有找到对应的排课表");
        }
        return learnerInfoRepository.findLearnerInfoByLearnerAndCourseScheduling(learner,courseScheduling);
    }

    @Override
    public List<LearnerInfo> findLearnerInfoByLearnerId(Long learnerId) throws Exception {
        Learner learner = learnerService.findOne(learnerId);
        if(learner == null){
            throw new Exception("没有对应的学员信息");
        }
        return learnerInfoRepository.findLearnerInfoByLearner(learner);
    }

    @Override
    public LearnerInfo insertLearner(String exerciseData, String bodyTestData, String coachAdvice, Long learnerId, Long scheId) throws Exception {
        Learner learner = learnerService.findOne(learnerId);
        if(learner == null){
            throw new Exception("没有对应的学员信息");
        }
        CourseScheduling courseScheduling = courseSchedulingService.findOne(scheId);
        if (courseScheduling  == null){
            throw new Exception("没有找到对应的排课表");
        }
        LearnerInfo flag=learnerInfoRepository.findLearnerInfoByLearnerAndCourseScheduling(learner,courseScheduling);
        if (flag != null){
            throw new Exception("你已经填写此学员的建议！");
        }
        LearnerInfo learnerInfo=new LearnerInfo();
        ZonedDateTime dateTime = ZonedDateTime.now();
        learnerInfo.setTime(dateTime);
        learnerInfo.setExerciseData(exerciseData);
        learnerInfo.setBodytestData(bodyTestData);
        learnerInfo.setCoachAdvice(coachAdvice);
        learnerInfo.setLearner(learnerRepository.findOne(learnerId));
        learnerInfo.setCourseScheduling(courseSchedulingRepository.findOne(scheId));
        LearnerInfo result = learnerInfoRepository.save(learnerInfo);
        return result;
    }

    //根据教练ID查找当天教练下面所有未填写和已填写教练建议的学员
    @Override
    public List<LearnerInfo> getLearnerInfoBySche(Long coachId, ZonedDateTime startTime) throws Exception {
        List<LearnerInfo> learnerInfos = new ArrayList<LearnerInfo>();
        LearnerInfo learnerInfo ;
        List<CourseScheduling> courseSchedulings = new ArrayList<>();
        if (coachId == null){
            throw new Exception("参数解析异常!");
        }
        Coach coach = coachRepository.findOne(coachId);
        if (coach == null){
            throw new Exception("没有找到对应的教练信息！");
        }
        List<CourseScheduling> list = courseSchedulingRepository.findCourseSchedulingsByCoach(coach);
        if (list.size()==0){
            throw new Exception("没有该教练对应的拍班信息!");
        }
        for (CourseScheduling courseScheduling : list){
            ZonedDateTime date = courseScheduling.getStartTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Date d1 =TypeUtils.castToDate(date.format(formatter));
            Date d2 = TypeUtils.castToDate(startTime.format(formatter));
            if (d1.getTime() == d2.getTime()){
                courseSchedulings.add(courseScheduling);
                for (CourseScheduling course : courseSchedulings){
                    CourseScheduling coursche = courseSchedulingRepository.findOne(course.getId());
                    if (coursche!=null){
                        List<LearnerCharge> learnerCharges = learnerChargeRepository.findLearnerChargeByCoachAndCourseScheduling(coach,coursche);
                        for(LearnerCharge learnerCharge : learnerCharges){
                            Learner learner = learnerCharge.getLearner();
                            learnerInfo = learnerInfoRepository.findLearnerInfoByLearnerAndCourseScheduling(learner,coursche);
                            learnerInfos.add(learnerInfo);
                        }
                    }
                }
            }
        }
        return learnerInfos;
    }
}
