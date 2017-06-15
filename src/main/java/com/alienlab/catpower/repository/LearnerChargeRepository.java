package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.domain.LearnerCharge;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the LearnerCharge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LearnerChargeRepository extends JpaRepository<LearnerCharge,Long> {

    List<LearnerCharge> findLearnerChargesByCourseScheduling(CourseScheduling courseScheduling);

}
