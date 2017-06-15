package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.LearnerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * Service Interface for managing LearnerInfo.
 */
public interface LearnerInfoService {

    /**
     * Save a learnerInfo.
     *
     * @param learnerInfo the entity to save
     * @return the persisted entity
     */
    LearnerInfo save(LearnerInfo learnerInfo);

    /**
     *  Get all the learnerInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<LearnerInfo> findAll(Pageable pageable);

    /**
     *  Get the "id" learnerInfo.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    LearnerInfo findOne(Long id);

    /**
     *  Delete the "id" learnerInfo.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);


}
