package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.Learner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Learner.
 */
public interface LearnerService {

    /**
     * Save a learner.
     *
     * @param learner the entity to save
     * @return the persisted entity
     */
    Learner save(Learner learner);

    /**
     *  Get all the learners.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Learner> findAll(Pageable pageable);

    /**
     *  Get the "id" learner.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Learner findOne(Long id);

    /**
     *  Delete the "id" learner.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
