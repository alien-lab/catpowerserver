package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.domain.LearnerInfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the LearnerInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LearnerInfoRepository extends JpaRepository<LearnerInfo,Long> {
    //查询教练建议
    @Query("select a from LearnerInfo a  where a.learner=?1 and a.courseScheduling=?2")
    LearnerInfo findLearnerInfoByLearnerAndCourseScheduling(Learner leaner, CourseScheduling courseScheduling);

    //查询健康数据
    @Query("select a from LearnerInfo a  where a.learner=?1 order by time desc")
    List<LearnerInfo> findLearnerInfoByLearner(Learner leaner);

}
