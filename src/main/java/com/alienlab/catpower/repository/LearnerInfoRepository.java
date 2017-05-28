package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.LearnerInfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the LearnerInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LearnerInfoRepository extends JpaRepository<LearnerInfo,Long> {

}
