package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.BuyCourse;
import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.domain.Learner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Spring Data JPA repository for the CourseScheduling entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseSchedulingRepository extends JpaRepository<CourseScheduling,Long> {

    List<CourseScheduling> findCourseSchedulingsByStartTimeBetween(ZonedDateTime startDate, ZonedDateTime endDate);
    //根据时间查询教练排课情况
    Page<CourseScheduling> findCourseSchedulingsByStartTimeBetween(ZonedDateTime startDate, ZonedDateTime endDate, Pageable pageable);
    //根据教练获取排课记录
    List<CourseScheduling> findCourseSchedulingsByCoach(Coach coach);
    //根据具体日期和时间查询当天排课情况
    @Query("select a from  CourseScheduling a where a.coach=?1 and a.startTime=?2")
    List<CourseScheduling> findCourseSchedulingsByCoachAndStartTime(Coach coach,ZonedDateTime startTime);

    //模糊查询
    @Query("select a from CourseScheduling a where (a.course.courseName like CONCAT('%',?1,'%')) or (a.coach.coachName like CONCAT('%',?1,'%')) or (a.status like CONCAT('%',?1,'%')) ")
    Page<CourseScheduling> findCourseSchedulingByCourseNameLike(String keyword,Pageable pageable);

    @Query("select a from CourseScheduling a where a.status in ('预约中','已预约') and a.learner=?1 and a.appointDate>=?2")
    CourseScheduling findAppointByLearner(Learner learner,String appointdate);

    @Query("select a from CourseScheduling a where a.appointDate=?1 and a.coach=?2 and a.status in ('预约中','已预约')")
    List<CourseScheduling> findByAppointDateAndCoach(String date,Coach coach);

    @Query("select a from CourseScheduling a where a.appointDate=?1 and a.coach=?2 and a.appointTime=?3 and a.status in ('预约中','已预约')")
    List<CourseScheduling> findByAppointDateAndCoachAndAppointTime(String date,Coach coach,String time);

    @Query("select a from CourseScheduling a where a.appointDate>=?2 and a.coach=?1 and a.status='已预约' order by a.appointDate,a.appointTime")
    List<CourseScheduling> findAllByCoach(Coach coach,String date);

    @Query("select a from CourseScheduling a where a.status='上课中' and a.learner=?1 order by a.startTime")
    List<CourseScheduling> findLearnerOnLineCourse(Learner learner);

    @Query("select a from CourseScheduling a where a.status='上课中' and a.coach=?1 order by a.startTime")
    List<CourseScheduling> findTeacherOnLineCourse(Coach coach);

}
