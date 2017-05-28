package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.CoachEvaluate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CoachEvaluate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoachEvaluateRepository extends JpaRepository<CoachEvaluate,Long> {

}
