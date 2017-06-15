package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.CourseScheduling;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service Interface for managing CourseScheduling.
 */
public interface CourseSchedulingService {

    /**
     * Save a courseScheduling.
     *
     * @param courseScheduling the entity to save
     * @return the persisted entity
     */
    CourseScheduling save(CourseScheduling courseScheduling);

    /**
     *  Get all the courseSchedulings.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CourseScheduling> findAll(Pageable pageable);

    /**
     *  Get the "id" courseScheduling.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CourseScheduling findOne(Long id);

    /**
     *  Delete the "id" courseScheduling.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<CourseScheduling> getScheByDate(ZonedDateTime startDate, ZonedDateTime endDate) throws Exception;
}
