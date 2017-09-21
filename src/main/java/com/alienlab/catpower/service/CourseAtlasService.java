package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.CourseAtlas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CourseAtlas.
 */
public interface CourseAtlasService {

    /**
     * Save a courseAtlas.
     * 保存一个课程图册表
     * @param courseAtlas the entity to save
     * @return the persisted entity
     */
    CourseAtlas save(CourseAtlas courseAtlas);

    /**
     *  Get all the courseAtlases.
     *  得到所有的课程图册表
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CourseAtlas> findAll(Pageable pageable);

    /**
     *  Get the "id" courseAtlas.
     *  得到指定id的课程图册表
     *  @param id the id of the entity
     *  @return the entity
     */
    CourseAtlas findOne(Long id);

    /**
     *  Delete the "id" courseAtlas.
     *  删除指定id的课程图册表
     *  @param id the id of the entity
     */
    void delete(Long id);
}
