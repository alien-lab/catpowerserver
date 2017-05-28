package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.Learner;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Learner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LearnerRepository extends JpaRepository<Learner,Long> {

}
