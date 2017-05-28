package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.service.LearnerService;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.repository.LearnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Learner.
 */
@Service
@Transactional
public class LearnerServiceImpl implements LearnerService{

    private final Logger log = LoggerFactory.getLogger(LearnerServiceImpl.class);
    
    private final LearnerRepository learnerRepository;

    public LearnerServiceImpl(LearnerRepository learnerRepository) {
        this.learnerRepository = learnerRepository;
    }

    /**
     * Save a learner.
     *
     * @param learner the entity to save
     * @return the persisted entity
     */
    @Override
    public Learner save(Learner learner) {
        log.debug("Request to save Learner : {}", learner);
        Learner result = learnerRepository.save(learner);
        return result;
    }

    /**
     *  Get all the learners.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Learner> findAll(Pageable pageable) {
        log.debug("Request to get all Learners");
        Page<Learner> result = learnerRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one learner by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Learner findOne(Long id) {
        log.debug("Request to get Learner : {}", id);
        Learner learner = learnerRepository.findOne(id);
        return learner;
    }

    /**
     *  Delete the  learner by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Learner : {}", id);
        learnerRepository.delete(id);
    }
}
