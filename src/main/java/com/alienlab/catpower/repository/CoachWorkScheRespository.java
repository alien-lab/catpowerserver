package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.domain.CoachWorkSche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 橘 on 2017/6/26.
 */
public interface CoachWorkScheRespository extends JpaRepository<CoachWorkSche,Long> {
    //根据教练id查询相对应的排版时间
    @Query("select a from CoachWorkSche a where a.coach = ?1")
    List<CoachWorkSche> findCoachWorkScheByCoach(Coach coach);
}
