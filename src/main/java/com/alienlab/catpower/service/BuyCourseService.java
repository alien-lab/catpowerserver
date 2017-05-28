package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.BuyCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}
