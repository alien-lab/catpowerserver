package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.Coach;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Coach.
 */
public interface CoachService {

    /**
     * Save a coach.
     *
     * @param coach the entity to save
     * @return the persisted entity
     */
    Coach save(Coach coach);

    /**
     *  Get all the coaches.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Coach> findAll(Pageable pageable);

    /**
     *  Get the "id" coach.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Coach findOne(Long id);

    /**
     *  Delete the "id" coach.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *
     */
    List<Coach> getCoachByCoachId(Long id) throws Exception;
}
