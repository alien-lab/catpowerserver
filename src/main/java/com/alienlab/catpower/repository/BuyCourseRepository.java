package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.BuyCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;


/**
 * Spring Data JPA repository for the BuyCourse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BuyCourseRepository extends JpaRepository<BuyCourse,Long> {

    Page<BuyCourse> findBuyCourseByBuyTimeBetweenOrderByBuyTimeDesc(ZonedDateTime butTime1,ZonedDateTime butTime2, Pageable page);
}
