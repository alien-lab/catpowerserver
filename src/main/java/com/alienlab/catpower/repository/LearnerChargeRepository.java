package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.Course;
import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.domain.Learner;
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

    //根据学员ID查询我的全部课程也可以通过这些查询一下签到记录
    @Query("select a from LearnerCharge a  where a.learner=?1 group by a.course order by chargeTime DESC")
    List<LearnerCharge> findLearnerChargeByLearner(Learner learner);

   //根据查询历史签到纪录
    @Query("select a from LearnerCharge a  where a.course=?1 and a.learner=?2 order by chargeTime DESC ")
    List<LearnerCharge> findLearnerChargeByCourseAndLearner(Course course,Learner learner);


}
