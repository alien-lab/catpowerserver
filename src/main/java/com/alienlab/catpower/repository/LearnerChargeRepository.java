package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.LearnerCharge;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the LearnerCharge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LearnerChargeRepository extends JpaRepository<LearnerCharge,Long> {

}
