package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Course.
 */
public interface CourseService {

    /**
     * Save a course.
     *
     * @param course the entity to save
     * @return the persisted entity
     */
    Course save(Course course);

    /**
     *  Get all the courses.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Course> findAll(Pageable pageable);

    /**
     *  Get the "id" course.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Course findOne(Long id);

    /**
     *  Delete the "id" course.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
