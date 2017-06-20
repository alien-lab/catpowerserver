package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.CoachEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the CoachEvaluate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoachEvaluateRepository extends JpaRepository<CoachEvaluate,Long> {


}
