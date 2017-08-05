package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.BuyCourse;
import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.domain.Course;
import com.alienlab.catpower.domain.Learner;
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


    @Query("select a from BuyCourse a where a.learner.id=?1 group by a.coach.id")
    List<BuyCourse> findCoachByLearner(Long learnerId);

    @Query("select a.course from BuyCourse a where a.coach=?1 and a.learner.id=?2")
    List<BuyCourse> findCourseByCoach(Coach coach, Long learnerId);

    @Query("select a from BuyCourse a where a.learner.id=?1")
    List<BuyCourse> findCourseByLearner(Long learnerId);

    //查询我的课程
    @Query("select a from BuyCourse a where a.learner=?1 order by a.buyTime")
    List<BuyCourse> findBuyCourseByLearner(Learner learner);


    //查询支付方式
    @Query("select a from BuyCourse a group by a.paymentWay")
    List<BuyCourse> findBuyCourse();

    @Query("select a from BuyCourse a where a.coach.id=?1 group by a.learner.id")
    List<BuyCourse> getBuyCourseByCoachId(Long coachId);

    @Query("select a from BuyCourse a where a.coach=?1 ")
    List<BuyCourse> findBuyCourseByCoach(Coach coach);

    //模糊查询
    @Query("select a from BuyCourse a where (a.course.courseName like CONCAT('%',?1,'%')) or (a.learner.learneName like CONCAT('%',?1,'%')) or (a.coach.coachName like CONCAT('%',?1,'%')) ")
    List<BuyCourse> findBuyCourseByCourseNameLike(String keyword);

}
