package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.service.LearnerChargeService;
import com.alienlab.catpower.domain.LearnerCharge;
import com.alienlab.catpower.repository.LearnerChargeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing LearnerCharge.
 */
@Service
@Transactional
public class LearnerChargeServiceImpl implements LearnerChargeService{

    private final Logger log = LoggerFactory.getLogger(LearnerChargeServiceImpl.class);
    
    private final LearnerChargeRepository learnerChargeRepository;

    public LearnerChargeServiceImpl(LearnerChargeRepository learnerChargeRepository) {
        this.learnerChargeRepository = learnerChargeRepository;
    }

    /**
     * Save a learnerCharge.
     *
     * @param learnerCharge the entity to save
     * @return the persisted entity
     */
    @Override
    public LearnerCharge save(LearnerCharge learnerCharge) {
        log.debug("Request to save LearnerCharge : {}", learnerCharge);
        LearnerCharge result = learnerChargeRepository.save(learnerCharge);
        return result;
    }

    /**
     *  Get all the learnerCharges.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LearnerCharge> findAll(Pageable pageable) {
        log.debug("Request to get all LearnerCharges");
        Page<LearnerCharge> result = learnerChargeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one learnerCharge by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public LearnerCharge findOne(Long id) {
        log.debug("Request to get LearnerCharge : {}", id);
        LearnerCharge learnerCharge = learnerChargeRepository.findOne(id);
        return learnerCharge;
    }

    /**
     *  Delete the  learnerCharge by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LearnerCharge : {}", id);
        learnerChargeRepository.delete(id);
    }
}
