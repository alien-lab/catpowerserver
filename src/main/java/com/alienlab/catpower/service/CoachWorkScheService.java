package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.CoachWorkSche;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * user :Master Cll
 * time :2017/7/22
 */
public interface CoachWorkScheService {
    /**
     * Save a coachWorkSche.
     *
     * @param coachWorkSche the entity to save
     * @return the persisted entity
     */
    CoachWorkSche save(CoachWorkSche coachWorkSche);

    /**
     *  Get all the coachWorkSche.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CoachWorkSche> findAll(Pageable pageable);

    /**
     *  Get the "id" coachWorkSche.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CoachWorkSche findOne(Long id);

    /**
     *  Delete the "id" course.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    //根据教练ID查询对应的排版时间
    List<CoachWorkSche> findCoachWorkScheByCoachId(Long coachId) throws Exception;

    CoachWorkSche createCoachWorkSche(ZonedDateTime time,int wordWeekday,Long coachId) throws Exception;

}
