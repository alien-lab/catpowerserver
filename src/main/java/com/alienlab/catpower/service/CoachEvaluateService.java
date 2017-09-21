package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.CoachEvaluate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing CoachEvaluate.
 */
public interface CoachEvaluateService {

    /**
     * Save a coachEvaluate.
     * 保存对教练的评价
     * @param coachEvaluate the entity to save
     * @return the persisted entity
     */
    CoachEvaluate save(CoachEvaluate coachEvaluate);

    /**
     *  Get all the coachEvaluates.
     *  得到所有的对教练的评价
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CoachEvaluate> findAll(Pageable pageable);

    /**
     *  Get the "id" coachEvaluate.
     *  根据id获取对教练的评价
     *  @param id the id of the entity
     *  @return the entity
     */
    CoachEvaluate findOne(Long id);

    /**
     *  Delete the "id" coachEvaluate.
     *  根据id删除对教练的评价
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *
     * @param coachId
     * @return
     */

    /**
     * Save a coachEvaluate.
     * 插入一条对教练的评价
     * @return the persisted entity
     */
    CoachEvaluate insert(Long serviceAttitude,Long speciality,Long like,String complain,Long evaluation,Long learnerId,Long scheId) throws Exception;

    /**
     * 根据教练ID获取此教练的评价
     */
    List<CoachEvaluate> getCoachEvaluateByCoachId(Long coachId) throws Exception;

    CoachEvaluate getCoachEvaluateByLearnerId(Long scheId,Long learnerId) throws Exception;
}
