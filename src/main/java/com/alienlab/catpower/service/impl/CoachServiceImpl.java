package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.repository.CoachRepository;
import com.alienlab.catpower.service.CoachService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Service Implementation for managing Coach.
 */
@Service
@Transactional
public class CoachServiceImpl implements CoachService{

    private final Logger log = LoggerFactory.getLogger(CoachServiceImpl.class);

    private final CoachRepository coachRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    CoachService coachService;

    public CoachServiceImpl(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    /**
     * Save a coach.
     *
     * @param coach the entity to save
     * @return the persisted entity
     */
    @Override
    public Coach save(Coach coach) {
        log.debug("Request to save Coach : {}", coach);
        Coach result = coachRepository.save(coach);
        return result;
    }

    /**
     *  Get all the coaches.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Coach> findAll(Pageable pageable) {
        log.debug("Request to get all Coaches");
        Page<Coach> result = coachRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one coach by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Coach findOne(Long id) {
        log.debug("Request to get Coach : {}", id);
        Coach coach = coachRepository.findOne(id);
        return coach;
    }

    /**
     *  Delete the  coach by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Coach : {}", id);
        coachRepository.delete(id);
    }

    @Override
    public Map getCoachByCoachId(Long id) throws Exception {

       /* String sql = "SELECT a.complain,a.evaluation,b.coach_name,b.coach_phone,b.coach_introduce,b.coach_picture,c.course_id FROM `coach_evaluate` a,`coach` b, `buy_course` c\n" +
            "WHERE b.id='"+id+"' AND c.coach_id='"+id+"' AND a.coach_id=b.id AND b.id=c.coach_id ";
        */

        String sql1 = "SELECT * FROM `coach` a WHERE a.id='"+id+"'";
        String sql2 = "SELECT * FROM `coach_evaluate` b WHERE b.`coach_id`='"+id+"'";
        String sql3 = "SELECT DISTINCT c.course_name FROM course c,buy_course b WHERE b.coach_id = '"+id+"' AND b.`course_id` = c.id";

        Map<String ,Object> map = new HashMap<String ,Object>();
        map.put("coachBase",jdbcTemplate.queryForMap(sql1));
        map.put("courseCharge",jdbcTemplate.queryForMap(sql2));
        map.put("courseInfo",jdbcTemplate.queryForMap(sql3));
        return map;
    }
}
