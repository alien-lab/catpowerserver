package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.Learner;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Learner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LearnerRepository extends JpaRepository<Learner,Long> {
    @Query("select a from Learner a where a.wechatUser.openId=?1")
    Learner findLearnerByOpenid(String openid);
    //根据学员名字查询
    List<Learner> findLearnerByLearneName(String learneName);
   /* List<Course> findCourseByLearnerId()*/

}
