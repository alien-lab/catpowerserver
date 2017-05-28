package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.service.CoachService;
import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.repository.CoachRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Coach.
 */
@Service
@Transactional
public class CoachServiceImpl implements CoachService{

    private final Logger log = LoggerFactory.getLogger(CoachServiceImpl.class);
    
    private final CoachRepository coachRepository;

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
}
