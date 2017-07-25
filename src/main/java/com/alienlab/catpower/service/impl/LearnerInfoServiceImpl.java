package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.repository.CourseSchedulingRepository;
import com.alienlab.catpower.repository.LearnerRepository;
import com.alienlab.catpower.service.CourseSchedulingService;
import com.alienlab.catpower.service.CourseService;
import com.alienlab.catpower.service.LearnerInfoService;
import com.alienlab.catpower.domain.LearnerInfo;
import com.alienlab.catpower.repository.LearnerInfoRepository;
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
}
