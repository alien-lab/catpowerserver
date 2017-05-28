package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.CatpowerserverApp;

import com.alienlab.catpower.domain.Course;
import com.alienlab.catpower.repository.CourseRepository;
import com.alienlab.catpower.service.CourseService;
import com.alienlab.catpower.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CourseResource REST controller.
 *
 * @see CourseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatpowerserverApp.class)
public class CourseResourceIntTest {

    private static final String DEFAULT_COURSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_INTRODUCTIONS = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_INTRODUCTIONS = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_CLASS_HOUR = 1;
    private static final Integer UPDATED_TOTAL_CLASS_HOUR = 2;

    private static final Float DEFAULT_COURSE_PRICES = 1F;
    private static final Float UPDATED_COURSE_PRICES = 2F;

    private static final Float DEFAULT_COURSE_VIPPRICES = 1F;
    private static final Float UPDATED_COURSE_VIPPRICES = 2F;

    private static final Integer DEFAULT_CLASS_NUMBER = 1;
    private static final Integer UPDATED_CLASS_NUMBER = 2;

    private static final String DEFAULT_COURSE_OTHER_INFO = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_OTHER_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_THUMBNAIL = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_THUMBNAIL = "BBBBBBBBBB";

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourseMockMvc;

    private Course course;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CourseResource courseResource = new CourseResource(courseService);
        this.restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .courseName(DEFAULT_COURSE_NAME)
            .courseIntroductions(DEFAULT_COURSE_INTRODUCTIONS)
            .totalClassHour(DEFAULT_TOTAL_CLASS_HOUR)
            .coursePrices(DEFAULT_COURSE_PRICES)
            .courseVipprices(DEFAULT_COURSE_VIPPRICES)
            .classNumber(DEFAULT_CLASS_NUMBER)
            .courseOtherInfo(DEFAULT_COURSE_OTHER_INFO)
            .courseThumbnail(DEFAULT_COURSE_THUMBNAIL);
        return course;
    }

    @Before
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getCourseName()).isEqualTo(DEFAULT_COURSE_NAME);
        assertThat(testCourse.getCourseIntroductions()).isEqualTo(DEFAULT_COURSE_INTRODUCTIONS);
        assertThat(testCourse.getTotalClassHour()).isEqualTo(DEFAULT_TOTAL_CLASS_HOUR);
        assertThat(testCourse.getCoursePrices()).isEqualTo(DEFAULT_COURSE_PRICES);
        assertThat(testCourse.getCourseVipprices()).isEqualTo(DEFAULT_COURSE_VIPPRICES);
        assertThat(testCourse.getClassNumber()).isEqualTo(DEFAULT_CLASS_NUMBER);
        assertThat(testCourse.getCourseOtherInfo()).isEqualTo(DEFAULT_COURSE_OTHER_INFO);
        assertThat(testCourse.getCourseThumbnail()).isEqualTo(DEFAULT_COURSE_THUMBNAIL);
    }

    @Test
    @Transactional
    public void createCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course with an existing ID
        course.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseName").value(hasItem(DEFAULT_COURSE_NAME.toString())))
            .andExpect(jsonPath("$.[*].courseIntroductions").value(hasItem(DEFAULT_COURSE_INTRODUCTIONS.toString())))
            .andExpect(jsonPath("$.[*].totalClassHour").value(hasItem(DEFAULT_TOTAL_CLASS_HOUR)))
            .andExpect(jsonPath("$.[*].coursePrices").value(hasItem(DEFAULT_COURSE_PRICES.doubleValue())))
            .andExpect(jsonPath("$.[*].courseVipprices").value(hasItem(DEFAULT_COURSE_VIPPRICES.doubleValue())))
            .andExpect(jsonPath("$.[*].classNumber").value(hasItem(DEFAULT_CLASS_NUMBER)))
            .andExpect(jsonPath("$.[*].courseOtherInfo").value(hasItem(DEFAULT_COURSE_OTHER_INFO.toString())))
            .andExpect(jsonPath("$.[*].courseThumbnail").value(hasItem(DEFAULT_COURSE_THUMBNAIL.toString())));
    }

    @Test
    @Transactional
    public void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().intValue()))
            .andExpect(jsonPath("$.courseName").value(DEFAULT_COURSE_NAME.toString()))
            .andExpect(jsonPath("$.courseIntroductions").value(DEFAULT_COURSE_INTRODUCTIONS.toString()))
            .andExpect(jsonPath("$.totalClassHour").value(DEFAULT_TOTAL_CLASS_HOUR))
            .andExpect(jsonPath("$.coursePrices").value(DEFAULT_COURSE_PRICES.doubleValue()))
            .andExpect(jsonPath("$.courseVipprices").value(DEFAULT_COURSE_VIPPRICES.doubleValue()))
            .andExpect(jsonPath("$.classNumber").value(DEFAULT_CLASS_NUMBER))
            .andExpect(jsonPath("$.courseOtherInfo").value(DEFAULT_COURSE_OTHER_INFO.toString()))
            .andExpect(jsonPath("$.courseThumbnail").value(DEFAULT_COURSE_THUMBNAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourse() throws Exception {
        // Initialize the database
        courseService.save(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findOne(course.getId());
        updatedCourse
            .courseName(UPDATED_COURSE_NAME)
            .courseIntroductions(UPDATED_COURSE_INTRODUCTIONS)
            .totalClassHour(UPDATED_TOTAL_CLASS_HOUR)
            .coursePrices(UPDATED_COURSE_PRICES)
            .courseVipprices(UPDATED_COURSE_VIPPRICES)
            .classNumber(UPDATED_CLASS_NUMBER)
            .courseOtherInfo(UPDATED_COURSE_OTHER_INFO)
            .courseThumbnail(UPDATED_COURSE_THUMBNAIL);

        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourse)))
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getCourseName()).isEqualTo(UPDATED_COURSE_NAME);
        assertThat(testCourse.getCourseIntroductions()).isEqualTo(UPDATED_COURSE_INTRODUCTIONS);
        assertThat(testCourse.getTotalClassHour()).isEqualTo(UPDATED_TOTAL_CLASS_HOUR);
        assertThat(testCourse.getCoursePrices()).isEqualTo(UPDATED_COURSE_PRICES);
        assertThat(testCourse.getCourseVipprices()).isEqualTo(UPDATED_COURSE_VIPPRICES);
        assertThat(testCourse.getClassNumber()).isEqualTo(UPDATED_CLASS_NUMBER);
        assertThat(testCourse.getCourseOtherInfo()).isEqualTo(UPDATED_COURSE_OTHER_INFO);
        assertThat(testCourse.getCourseThumbnail()).isEqualTo(UPDATED_COURSE_THUMBNAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Create the Course

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCourse() throws Exception {
        // Initialize the database
        courseService.save(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Get the course
        restCourseMockMvc.perform(delete("/api/courses/{id}", course.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = new Course();
        course1.setId(1L);
        Course course2 = new Course();
        course2.setId(course1.getId());
        assertThat(course1).isEqualTo(course2);
        course2.setId(2L);
        assertThat(course1).isNotEqualTo(course2);
        course1.setId(null);
        assertThat(course1).isNotEqualTo(course2);
    }
}
