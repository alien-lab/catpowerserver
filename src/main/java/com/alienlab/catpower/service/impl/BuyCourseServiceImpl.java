package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.service.BuyCourseService;
import com.alienlab.catpower.domain.BuyCourse;
import com.alienlab.catpower.repository.BuyCourseRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Service Implementation for managing BuyCourse.
 */
@Service
@Transactional
public class BuyCourseServiceImpl implements BuyCourseService{

    private final Logger log = LoggerFactory.getLogger(BuyCourseServiceImpl.class);

    private final BuyCourseRepository buyCourseRepository;

    public BuyCourseServiceImpl(BuyCourseRepository buyCourseRepository) {
        this.buyCourseRepository = buyCourseRepository;
    }

    /**
     * Save a buyCourse.
     *
     * @param buyCourse the entity to save
     * @return the persisted entity
     */
    @Override
    public BuyCourse save(BuyCourse buyCourse) {
        log.debug("Request to save BuyCourse : {}", buyCourse);
        BuyCourse result = buyCourseRepository.save(buyCourse);
        return result;
    }

    /**
     *  Get all the buyCourses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BuyCourse> findAll(Pageable pageable) {
        log.debug("Request to get all BuyCourses");
        Page<BuyCourse> result = buyCourseRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one buyCourse by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BuyCourse findOne(Long id) {
        log.debug("Request to get BuyCourse : {}", id);
        BuyCourse buyCourse = buyCourseRepository.findOne(id);
        return buyCourse;
    }

    /**
     *  Delete the  buyCourse by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BuyCourse : {}", id);
        buyCourseRepository.delete(id);
    }

    @Override
    public Page<BuyCourse> getTodayData(Pageable page) throws Exception{
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today=format.format(new Date());
        String t1=today+"T00:00:00Z";
        String t2=today+"T23:59:59Z";

        return buyCourseRepository.findBuyCourseByBuyTimeBetweenOrderByBuyTimeDesc(
            ZonedDateTime.parse(t1),ZonedDateTime.parse(t2)
            ,page);
    }
}
