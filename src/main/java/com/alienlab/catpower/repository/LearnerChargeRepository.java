package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.*;
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

    //根据查询历史签到纪录
    @Query("select a from LearnerCharge a  where a.learner.id=?1 and a.courseScheduling.id=?2 ")
    LearnerCharge findLearnerChargeByLearnerIdAndScheId(Long learnerId,Long scheId);


    //根据教练和排课信息查找核销内容
    @Query("select a from LearnerCharge a  where a.coach=?1 and a.courseScheduling=?2")
    List<LearnerCharge> findLearnerChargeByCoachAndCourseScheduling(Coach coach ,CourseScheduling courseScheduling);
}
