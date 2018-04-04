package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.domain.Course;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.domain.LearnerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service Interface for managing LearnerInfo.
 */
public interface LearnerInfoService {

    /**
     * Save a learnerInfo.
     * 保存一条学员的信息
     * @param learnerInfo the entity to save
     * @return the persisted entity
     */
    LearnerInfo save(LearnerInfo learnerInfo);

    /**
     *  Get all the learnerInfos.
     *  得到所有的学员信息
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<LearnerInfo> findAll(Pageable pageable);

    /**
     *  Get the "id" learnerInfo.
     *  得到指定id的学员信息
     *  @param id the id of the entity
     *  @return the entity
     */
    LearnerInfo findOne(Long id);

    /**
     *  Delete the "id" learnerInfo.
     *  删除指定id的学员信息
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 根据学员ID回去学员的健身资料卡
     */

    //每节课的教练建议
    LearnerInfo findLearnerInfoByLearnerIdAndCourseSchedulingId(Long learnerId,Long courseSchedulingId) throws Exception;

    //每节课的教练建议
    List<LearnerInfo> findLearnerInfoByLearnerId(Long learnerId) throws Exception;

    LearnerInfo insertLearner(String exerciseData,String bodyTestData,String coachAdvice,Long learnerId,Long scheId) throws Exception;

    //根据教练ID查找当天教练下面所有未填写和已填写教练建议的学员
    List<LearnerInfo> getLearnerInfoBySche(Long coachId, ZonedDateTime startTime) throws Exception;

}
