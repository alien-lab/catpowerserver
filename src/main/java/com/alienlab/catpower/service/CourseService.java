package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Service Interface for managing Course.
 */
public interface CourseService {

    /**
     * Save a course.
     *
     * @param course the entity to save
     * @return the persisted entity
     */
    Course save(Course course);

    /**
     *  Get all the courses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Course> findAll(Pageable pageable);

    /**
     *  Get the "id" course.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Course findOne(Long id);

    /**
     *  Delete the "id" course.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    /**
     * 查询所有课程信息
     */
    List getCourse() throws Exception;
    /**
     * 根据课程类型查询课程信息
     */
    List<Course> getCourseInfoByCourseType(String courseType) throws Exception;
    /**
     * 查询课程的所有类型
     */
    List getCourseType() throws Exception;
    /**
     * 根据课程ID查询课程
     */
    Map getCourseById(Long id) throws Exception;
    /**
     * 根据课程名称获取课程总课时
     */
    Map getTotalClassHourByCourseName(String courseName) throws Exception;
    /**
     * 根据课程名称模糊查询
     */
    List getCourseByCourseName(String courseName) throws Exception;
}
