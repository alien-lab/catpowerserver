package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.domain.Course;
import com.alienlab.catpower.repository.CourseRepository;
import com.alienlab.catpower.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing Course.
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService{

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Save a course.
     *
     * @param course the entity to save
     * @return the persisted entity
     */
    @Override
    public Course save(Course course) {
        log.debug("Request to save Course : {}", course);
        Course result = courseRepository.save(course);
        return result;
    }

    /**
     *  Get all the courses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Course> findAll(Pageable pageable) {
        log.debug("Request to get all Courses");
        Page<Course> result = courseRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one course by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Course findOne(Long id) {
        log.debug("Request to get Course : {}", id);
        Course course = courseRepository.findOne(id);
        return course;
    }

    /**
     *  Delete the  course by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Course : {}", id);
        courseRepository.delete(id);
    }

    @Override
    public List getCourse() throws Exception {
        String sql = "SELECT a.*,CASE WHEN lj.learnercount IS NULL THEN 0 ELSE lj.learnercount END learnercount ,CASE WHEN lj2.schecount IS NULL THEN 0 ELSE lj2.schecount END schecount FROM course a \n" +
            "LEFT JOIN (SELECT course_id,COUNT(DISTINCT learner_id) learnercount FROM `buy_course` GROUP BY course_id) lj ON lj.course_id=a.id\n" +
            "LEFT JOIN (SELECT course_id,COUNT(id) schecount FROM course_scheduling GROUP BY course_id) lj2 ON lj2.course_id=a.id \n";

        List list = jdbcTemplate.queryForList(sql);
        return list;
    }
    //根据课程类型查询课程信息
    @Override
    public List<Course> getCourseInfoByCourseType(String courseType) throws Exception {
        String sql = "SELECT a.*,CASE WHEN lj.learnercount IS NULL THEN 0 ELSE lj.learnercount END learnercount ,CASE WHEN lj2.schecount IS NULL THEN 0 ELSE lj2.schecount END schecount FROM course a \n" +
            "LEFT JOIN (SELECT course_id,COUNT(DISTINCT learner_id) learnercount FROM `buy_course` GROUP BY course_id) lj ON lj.course_id=a.id\n" +
            "LEFT JOIN (SELECT course_id,COUNT(id) schecount FROM course_scheduling GROUP BY course_id) lj2 ON lj2.course_id=a.id  WHERE a.course_type='"+courseType+"'\n";
        List list = jdbcTemplate.queryForList(sql);
        return list;
    }
    //查询所有课程类型
    @Override
    public List getCourseType() throws Exception {
        String sql = "SELECT course_type FROM `course` GROUP BY course_type";
        List list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public Map getCourseById(Long id) throws Exception {
        String sql = "SELECT a.*,CASE WHEN lj.learnercount IS NULL THEN 0 ELSE lj.learnercount END learnercount ,CASE WHEN lj2.schecount IS NULL THEN 0 ELSE lj2.schecount END schecount FROM course a \n" +
            "LEFT JOIN (SELECT course_id,COUNT(DISTINCT learner_id) learnercount FROM `buy_course` GROUP BY course_id) lj ON lj.course_id=a.id\n" +
            "LEFT JOIN (SELECT course_id,COUNT(id) schecount FROM course_scheduling GROUP BY course_id) lj2 ON lj2.course_id=a.id WHERE id='"+id+"'";
        return jdbcTemplate.queryForMap(sql);
    }
    //根据课程ID查询课程
    @Override
    public Map getTotalClassHourByCourseName(String courseName) throws Exception {
        String sql = "SELECT total_class_hour FROM `course` WHERE course_name='"+courseName+"' GROUP BY course_name";
        return jdbcTemplate.queryForMap(sql);
    }
    //根据课程名称获取课程信息
    @Override
    public List getCourseByCourseName(String courseName) throws Exception {
        if(courseName != null){
            String sql = "SELECT a.*,CASE WHEN lj.learnercount IS NULL THEN 0 ELSE lj.learnercount END learnercount ,CASE WHEN lj2.schecount IS NULL THEN 0 ELSE lj2.schecount END schecount FROM course a\n" +
                "LEFT JOIN (SELECT course_id,COUNT(DISTINCT learner_id) learnercount FROM `buy_course` GROUP BY course_id) lj ON lj.course_id=a.id\n" +
                "LEFT JOIN (SELECT course_id,COUNT(id) schecount FROM course_scheduling GROUP BY course_id) lj2 ON lj2.course_id=a.id WHERE course_name LIKE '%"+courseName+"%'";
            List list = jdbcTemplate.queryForList(sql);
            return list;
        }else{
             throw new Exception("请输入课程名称");
        }

    }
}
