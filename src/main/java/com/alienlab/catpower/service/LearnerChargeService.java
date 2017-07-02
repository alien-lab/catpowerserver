package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.domain.LearnerCharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing LearnerCharge.
 */
public interface LearnerChargeService {

    /**
     * Save a learnerCharge.
     *
     * @param learnerCharge the entity to save
     * @return the persisted entity
     */
    LearnerCharge save(LearnerCharge learnerCharge);

    /**
     *  Get all the learnerCharges.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<LearnerCharge> findAll(Pageable pageable);

    /**
     *  Get the "id" learnerCharge.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    LearnerCharge findOne(Long id);

    /**
     *  Delete the "id" learnerCharge.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<LearnerCharge> getLeanersBySche(long scheId) throws Exception;

    List<LearnerCharge> getLeanersBySche(CourseScheduling sche) throws Exception;

    LearnerCharge chargeCourse(String openid,Long scheId)throws Exception;

    LearnerCharge chargeCourse(Learner learner, CourseScheduling sche) throws Exception;
}
