package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.CourseScheduling;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CourseScheduling entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseSchedulingRepository extends JpaRepository<CourseScheduling,Long> {

}
