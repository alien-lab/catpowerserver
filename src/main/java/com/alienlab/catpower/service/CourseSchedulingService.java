package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service Interface for managing CourseScheduling.
 */
public interface CourseSchedulingService {

    /**
     * Save a courseScheduling.
     *
     * @param courseScheduling the entity to save
     * @return the persisted entity
     */
    CourseScheduling save(CourseScheduling courseScheduling);

    /**
     *  Get all the courseSchedulings.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CourseScheduling> findAll(Pageable pageable);

    /**
     *  Get the "id" courseScheduling.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CourseScheduling findOne(Long id);

    /**
     *  Delete the "id" courseScheduling.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<CourseScheduling> getScheByDate(ZonedDateTime startDate, ZonedDateTime endDate) throws Exception;
    Page<CourseScheduling> getScheByDate(ZonedDateTime startDate, ZonedDateTime endDate,int index,int size) throws Exception;

    /**
     * 根据id更新上课的状态
     */
    int updateCourseScheduling(Long id,String status) throws  Exception;

    QrInfo getScheQrcode(Long scheId) throws Exception;
    /**
     * 根据教练id获取排课记录
     */
    List<CourseScheduling> getcourseSche(Long coachId) throws  Exception;

    List getCourseScheduling() throws Exception;
}
