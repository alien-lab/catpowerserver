package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.service.CourseAtlasService;
import com.alienlab.catpower.domain.CourseAtlas;
import com.alienlab.catpower.repository.CourseAtlasRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing CourseAtlas.
 */
@Service
@Transactional
public class CourseAtlasServiceImpl implements CourseAtlasService{

    private final Logger log = LoggerFactory.getLogger(CourseAtlasServiceImpl.class);
    
    private final CourseAtlasRepository courseAtlasRepository;

    public CourseAtlasServiceImpl(CourseAtlasRepository courseAtlasRepository) {
        this.courseAtlasRepository = courseAtlasRepository;
    }

    /**
     * Save a courseAtlas.
     *
     * @param courseAtlas the entity to save
     * @return the persisted entity
     */
    @Override
    public CourseAtlas save(CourseAtlas courseAtlas) {
        log.debug("Request to save CourseAtlas : {}", courseAtlas);
        CourseAtlas result = courseAtlasRepository.save(courseAtlas);
        return result;
    }

    /**
     *  Get all the courseAtlases.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseAtlas> findAll(Pageable pageable) {
        log.debug("Request to get all CourseAtlases");
        Page<CourseAtlas> result = courseAtlasRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one courseAtlas by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CourseAtlas findOne(Long id) {
        log.debug("Request to get CourseAtlas : {}", id);
        CourseAtlas courseAtlas = courseAtlasRepository.findOne(id);
        return courseAtlas;
    }

    /**
     *  Delete the  courseAtlas by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseAtlas : {}", id);
        courseAtlasRepository.delete(id);
    }
}
