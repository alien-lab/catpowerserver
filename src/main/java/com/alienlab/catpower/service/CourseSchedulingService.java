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
     * 保存一个教练排课表
     * @param courseScheduling the entity to save
     * @return the persisted entity
     */
    CourseScheduling save(CourseScheduling courseScheduling);

    /**
     *  Get all the courseSchedulings.
     *  得到所有的教练排课表
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CourseScheduling> findAll(Pageable pageable);

    /**
     *  Get the "id" courseScheduling.
     *  根据id获取指定的教练排课表
     *  @param id the id of the entity
     *  @return the entity
     */
    CourseScheduling findOne(Long id);

    /**
     *  Delete the "id" courseScheduling.
     *  删除指定的教练排课表
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

    /**
     * 下课时更新课程结束时间
     * @param scheId
     * @return
     * @throws Exception
     */
    CourseScheduling updateEndTime(Long scheId) throws Exception;

    //根据教练ID和开始时间获取当天的排课记录
    List<CourseScheduling> findCourseSchedulingByCoachIdAndStartTime(Long coachId,ZonedDateTime startTime) throws Exception;

    //根据教练查询查询教练排班
    // List<CourseScheduling> getSchebyCoachName(String coachName) throws Exception;

    //模糊查询
    Page<CourseScheduling> LikeSche(String keyword,Pageable pageable)throws Exception;
    //根据时间查询教练排课情况
    Page<CourseScheduling> getScheByTime(ZonedDateTime startDate, ZonedDateTime endDate, Pageable pageable)throws Exception;
}
