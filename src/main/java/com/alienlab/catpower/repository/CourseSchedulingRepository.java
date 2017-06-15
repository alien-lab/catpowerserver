package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.CourseScheduling;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Spring Data JPA repository for the CourseScheduling entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseSchedulingRepository extends JpaRepository<CourseScheduling,Long> {

    List<CourseScheduling> findCourseSchedulingsByStartTimeBetween(ZonedDateTime startDate, ZonedDateTime endDate);
    Page<CourseScheduling> findCourseSchedulingsByStartTimeBetween(ZonedDateTime startDate, ZonedDateTime endDate, Pageable page);

}
