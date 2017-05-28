package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.LearnerCharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}
