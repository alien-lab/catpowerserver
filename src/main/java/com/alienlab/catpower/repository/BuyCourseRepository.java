package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.BuyCourse;
import com.alienlab.catpower.domain.Course;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.domain.LearnerCharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Spring Data JPA repository for the BuyCourse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BuyCourseRepository extends JpaRepository<BuyCourse,Long> {
    Page<BuyCourse> findBuyCourseByBuyTimeBetweenOrderByBuyTimeDesc(ZonedDateTime butTime1,ZonedDateTime butTime2, Pageable page);
    @Query("select a from BuyCourse a where a.course=?2 and a.learner=?1 and a.status='正常'")
    BuyCourse findBuyCourseByLearnerAndCourse(Learner learner, Course course);


    //查询我的课程
    List<BuyCourse> findBuyCourseByLearner(Learner learner);

    //查询我的可用的课程
    List<BuyCourse> findUseBuyCourseByLearner(Learner learner);

    //查询我的不可用的课程
    List<BuyCourse> findNotUseBuyCourseByLearner(Learner learner);


}
