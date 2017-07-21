package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.domain.CoachEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the CoachEvaluate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoachEvaluateRepository extends JpaRepository<CoachEvaluate,Long> {
    //根据教练ID获取此教练的评价
    //@Query("select a from CoachEvaluate a where a.coach=?1")
    List<CoachEvaluate> findCoachEvaluateByCoach(Coach coach);

}
