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
     * 保存一门课程
     * @param course the entity to save
     * @return the persisted entity
     */
    Course save(Course course);

    /**
     *  Get all the courses.
     *  得到所有的课程
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Course> findAll(Pageable pageable);

    /**
     *  Get the "id" course.
     *  根据id获取指定的课程
     *  @param id the id of the entity
     *  @return the entity
     */
    Course findOne(Long id);

    /**
     *  Delete the "id" course.
     *  删除指定id的课程
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
    /**
     * 根据课程名称获取课程
     */
    List<Course> getCourseInfoByCourseName(String courseName)throws Exception;


    //
    List<Course> likeCourse(String keyword)throws Exception;
}
