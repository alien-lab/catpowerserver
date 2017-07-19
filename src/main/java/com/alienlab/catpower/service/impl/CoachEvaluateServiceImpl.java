package com.alienlab.catpower.service.impl;
import com.alienlab.catpower.domain.CoachEvaluate;
import com.alienlab.catpower.domain.LearnerCharge;
import com.alienlab.catpower.repository.CoachEvaluateRepository;
import com.alienlab.catpower.repository.LearnerChargeRepository;
import com.alienlab.catpower.service.CoachEvaluateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing CoachEvaluate.
 */
@Service
@Transactional
public class CoachEvaluateServiceImpl implements CoachEvaluateService{

    private final Logger log = LoggerFactory.getLogger(CoachEvaluateServiceImpl.class);

    private final CoachEvaluateRepository coachEvaluateRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private LearnerChargeRepository learnerChargeRepository;

    public CoachEvaluateServiceImpl(CoachEvaluateRepository coachEvaluateRepository) {
        this.coachEvaluateRepository = coachEvaluateRepository;
    }

    /**
     * Save a coachEvaluate.
     *
     * @param coachEvaluate the entity to save
     * @return the persisted entity
     */
    @Override
    public CoachEvaluate save(CoachEvaluate coachEvaluate) {
        log.debug("Request to save CoachEvaluate : {}", coachEvaluate);
        CoachEvaluate result = coachEvaluateRepository.save(coachEvaluate);
        return result;
    }

    /**
     *  Get all the coachEvaluates.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CoachEvaluate> findAll(Pageable pageable) {
        log.debug("Request to get all CoachEvaluates");
        Page<CoachEvaluate> result = coachEvaluateRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one coachEvaluate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CoachEvaluate findOne(Long id) {
        log.debug("Request to get CoachEvaluate : {}", id);
        CoachEvaluate coachEvaluate = coachEvaluateRepository.findOne(id);
        return coachEvaluate;
    }

    /**
     *  Delete the  coachEvaluate by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CoachEvaluate : {}", id);
        coachEvaluateRepository.delete(id);
    }

    /**
     * Save a coachEvaluate.
     * @return the persisted entity
     */
    @Override
    public CoachEvaluate insert(Long serviceAttitude,Long speciality,Long like,String complain,Long evaluation,Long learnerId,Long scheId) {
        CoachEvaluate coachEvaluate=new CoachEvaluate();
        coachEvaluate.setServiceAttitude(serviceAttitude);
        coachEvaluate.setSpeciality(speciality);
        coachEvaluate.setLike(like);
        coachEvaluate.setComplain(complain);
        coachEvaluate.setEvaluation(evaluation);
        LearnerCharge learnerCharge=learnerChargeRepository.findLearnerChargeByLearnerIdAndScheId(learnerId,scheId);
        coachEvaluate.setLearner(learnerCharge.getLearner());
        coachEvaluate.setCoach(learnerCharge.getCourseScheduling().getCoach());
        coachEvaluate.setCourse(learnerCharge.getCourseScheduling().getCourse());
        coachEvaluate.setLearnerCharge(learnerCharge);
        CoachEvaluate result = coachEvaluateRepository.save(coachEvaluate);
        return result;
    }


}
