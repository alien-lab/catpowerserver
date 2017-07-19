package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.BuyCourse;
import com.alienlab.catpower.domain.LearnerAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the LearnerAppointment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LearnerAppointmentRepository extends JpaRepository<LearnerAppointment,Long> {

    @Query("select a from LearnerAppointment a where a.buyCourse.learner.id=?1 order by a.appointmentDate DESC")
    List<LearnerAppointment> findAppointmentByLearner(Long learnerId);

    @Query("select a from LearnerAppointment a where a.buyCourse.learner.id=?1 and a.appointmentResult='预约中' order by a.appointmentDate DESC")
    List<LearnerAppointment> findAppointingByLearner(Long learnerId);

    @Query("select a from LearnerAppointment a where a.buyCourse.learner.id=?1 and a.appointmentResult='预约成功' order by a.appointmentDate DESC")
    List<LearnerAppointment> findAppointedByLearner(Long learnerId);



}
