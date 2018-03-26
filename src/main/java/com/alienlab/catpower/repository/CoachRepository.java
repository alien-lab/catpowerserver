package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Coach entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoachRepository extends JpaRepository<Coach,Long> {
    //根据openID查找教练信息
    @Query("select a from Coach a where a.coachWechatopenid = ?1")
    Coach findCoachByCoachWechatopenid(String coachWechatopenid);

    Coach findCoachByCoachPhone(String phone);
}
