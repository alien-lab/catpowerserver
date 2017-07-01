package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.CatpowerserverApp;

import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.repository.CourseSchedulingRepository;
import com.alienlab.catpower.service.CourseSchedulingService;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.alienlab.catpower.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CourseSchedulingResource REST controller.
 *
 * @see CourseSchedulingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatpowerserverApp.class)
public class CourseSchedulingResourceIntTest {

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String UPDATED_QR_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_SIGN_IN_COUNT = 1L;
    private static final Long UPDATED_SIGN_IN_COUNT = 2L;

    @Autowired
    private CourseSchedulingRepository courseSchedulingRepository;

    @Autowired
    private CourseSchedulingService courseSchedulingService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourseSchedulingMockMvc;

    private CourseScheduling courseScheduling;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CourseSchedulingResource courseSchedulingResource = new CourseSchedulingResource(courseSchedulingService);
        this.restCourseSchedulingMockMvc = MockMvcBuilders.standaloneSetup(courseSchedulingResource)
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
    public static CourseScheduling createEntity(EntityManager em) {
        CourseScheduling courseScheduling = new CourseScheduling()
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .status(DEFAULT_STATUS)
            .signInCount(DEFAULT_SIGN_IN_COUNT);
        return courseScheduling;
    }

    @Before
    public void initTest() {
        courseScheduling = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourseScheduling() throws Exception {
        int databaseSizeBeforeCreate = courseSchedulingRepository.findAll().size();

        // Create the CourseScheduling
        restCourseSchedulingMockMvc.perform(post("/api/course-schedulings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseScheduling)))
            .andExpect(status().isCreated());

        // Validate the CourseScheduling in the database
        List<CourseScheduling> courseSchedulingList = courseSchedulingRepository.findAll();
        assertThat(courseSchedulingList).hasSize(databaseSizeBeforeCreate + 1);
        CourseScheduling testCourseScheduling = courseSchedulingList.get(courseSchedulingList.size() - 1);
        assertThat(testCourseScheduling.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testCourseScheduling.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testCourseScheduling.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCourseScheduling.getSignInCount()).isEqualTo(DEFAULT_SIGN_IN_COUNT);
    }

    @Test
    @Transactional
    public void createCourseSchedulingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseSchedulingRepository.findAll().size();

        // Create the CourseScheduling with an existing ID
        courseScheduling.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseSchedulingMockMvc.perform(post("/api/course-schedulings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseScheduling)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CourseScheduling> courseSchedulingList = courseSchedulingRepository.findAll();
        assertThat(courseSchedulingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCourseSchedulings() throws Exception {
        // Initialize the database
        courseSchedulingRepository.saveAndFlush(courseScheduling);

        // Get all the courseSchedulingList
        restCourseSchedulingMockMvc.perform(get("/api/course-schedulings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseScheduling.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].signInCount").value(hasItem(DEFAULT_SIGN_IN_COUNT.intValue())));
    }

    @Test
    @Transactional
    public void getCourseScheduling() throws Exception {
        // Initialize the database
        courseSchedulingRepository.saveAndFlush(courseScheduling);

        // Get the courseScheduling
        restCourseSchedulingMockMvc.perform(get("/api/course-schedulings/{id}", courseScheduling.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courseScheduling.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.signInCount").value(DEFAULT_SIGN_IN_COUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCourseScheduling() throws Exception {
        // Get the courseScheduling
        restCourseSchedulingMockMvc.perform(get("/api/course-schedulings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseScheduling() throws Exception {
        // Initialize the database
        courseSchedulingService.save(courseScheduling);

        int databaseSizeBeforeUpdate = courseSchedulingRepository.findAll().size();

        // Update the courseScheduling
        CourseScheduling updatedCourseScheduling = courseSchedulingRepository.findOne(courseScheduling.getId());
        updatedCourseScheduling
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .status(UPDATED_STATUS)
            .signInCount(UPDATED_SIGN_IN_COUNT);

        restCourseSchedulingMockMvc.perform(put("/api/course-schedulings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourseScheduling)))
            .andExpect(status().isOk());

        // Validate the CourseScheduling in the database
        List<CourseScheduling> courseSchedulingList = courseSchedulingRepository.findAll();
        assertThat(courseSchedulingList).hasSize(databaseSizeBeforeUpdate);
        CourseScheduling testCourseScheduling = courseSchedulingList.get(courseSchedulingList.size() - 1);
        assertThat(testCourseScheduling.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testCourseScheduling.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testCourseScheduling.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCourseScheduling.getQrCode()).isEqualTo(UPDATED_QR_CODE);
        assertThat(testCourseScheduling.getSignInCount()).isEqualTo(UPDATED_SIGN_IN_COUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingCourseScheduling() throws Exception {
        int databaseSizeBeforeUpdate = courseSchedulingRepository.findAll().size();

        // Create the CourseScheduling

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCourseSchedulingMockMvc.perform(put("/api/course-schedulings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseScheduling)))
            .andExpect(status().isCreated());

        // Validate the CourseScheduling in the database
        List<CourseScheduling> courseSchedulingList = courseSchedulingRepository.findAll();
        assertThat(courseSchedulingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCourseScheduling() throws Exception {
        // Initialize the database
        courseSchedulingService.save(courseScheduling);

        int databaseSizeBeforeDelete = courseSchedulingRepository.findAll().size();

        // Get the courseScheduling
        restCourseSchedulingMockMvc.perform(delete("/api/course-schedulings/{id}", courseScheduling.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CourseScheduling> courseSchedulingList = courseSchedulingRepository.findAll();
        assertThat(courseSchedulingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseScheduling.class);
        CourseScheduling courseScheduling1 = new CourseScheduling();
        courseScheduling1.setId(1L);
        CourseScheduling courseScheduling2 = new CourseScheduling();
        courseScheduling2.setId(courseScheduling1.getId());
        assertThat(courseScheduling1).isEqualTo(courseScheduling2);
        courseScheduling2.setId(2L);
        assertThat(courseScheduling1).isNotEqualTo(courseScheduling2);
        courseScheduling1.setId(null);
        assertThat(courseScheduling1).isNotEqualTo(courseScheduling2);
    }
}
