package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.CatpowerserverApp;

import com.alienlab.catpower.domain.CourseAtlas;
import com.alienlab.catpower.repository.CourseAtlasRepository;
import com.alienlab.catpower.service.CourseAtlasService;
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
 * Test class for the CourseAtlasResource REST controller.
 *
 * @see CourseAtlasResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatpowerserverApp.class)
public class CourseAtlasResourceIntTest {

    private static final String DEFAULT_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE = "BBBBBBBBBB";

    @Autowired
    private CourseAtlasRepository courseAtlasRepository;

    @Autowired
    private CourseAtlasService courseAtlasService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourseAtlasMockMvc;

    private CourseAtlas courseAtlas;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CourseAtlasResource courseAtlasResource = new CourseAtlasResource(courseAtlasService);
        this.restCourseAtlasMockMvc = MockMvcBuilders.standaloneSetup(courseAtlasResource)
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
    public static CourseAtlas createEntity(EntityManager em) {
        CourseAtlas courseAtlas = new CourseAtlas()
            .picture(DEFAULT_PICTURE);
        return courseAtlas;
    }

    @Before
    public void initTest() {
        courseAtlas = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourseAtlas() throws Exception {
        int databaseSizeBeforeCreate = courseAtlasRepository.findAll().size();

        // Create the CourseAtlas
        restCourseAtlasMockMvc.perform(post("/api/course-atlases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseAtlas)))
            .andExpect(status().isCreated());

        // Validate the CourseAtlas in the database
        List<CourseAtlas> courseAtlasList = courseAtlasRepository.findAll();
        assertThat(courseAtlasList).hasSize(databaseSizeBeforeCreate + 1);
        CourseAtlas testCourseAtlas = courseAtlasList.get(courseAtlasList.size() - 1);
        assertThat(testCourseAtlas.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void createCourseAtlasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseAtlasRepository.findAll().size();

        // Create the CourseAtlas with an existing ID
        courseAtlas.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseAtlasMockMvc.perform(post("/api/course-atlases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseAtlas)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CourseAtlas> courseAtlasList = courseAtlasRepository.findAll();
        assertThat(courseAtlasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCourseAtlases() throws Exception {
        // Initialize the database
        courseAtlasRepository.saveAndFlush(courseAtlas);

        // Get all the courseAtlasList
        restCourseAtlasMockMvc.perform(get("/api/course-atlases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseAtlas.getId().intValue())))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE.toString())));
    }

    @Test
    @Transactional
    public void getCourseAtlas() throws Exception {
        // Initialize the database
        courseAtlasRepository.saveAndFlush(courseAtlas);

        // Get the courseAtlas
        restCourseAtlasMockMvc.perform(get("/api/course-atlases/{id}", courseAtlas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courseAtlas.getId().intValue()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCourseAtlas() throws Exception {
        // Get the courseAtlas
        restCourseAtlasMockMvc.perform(get("/api/course-atlases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseAtlas() throws Exception {
        // Initialize the database
        courseAtlasService.save(courseAtlas);

        int databaseSizeBeforeUpdate = courseAtlasRepository.findAll().size();

        // Update the courseAtlas
        CourseAtlas updatedCourseAtlas = courseAtlasRepository.findOne(courseAtlas.getId());
        updatedCourseAtlas
            .picture(UPDATED_PICTURE);

        restCourseAtlasMockMvc.perform(put("/api/course-atlases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourseAtlas)))
            .andExpect(status().isOk());

        // Validate the CourseAtlas in the database
        List<CourseAtlas> courseAtlasList = courseAtlasRepository.findAll();
        assertThat(courseAtlasList).hasSize(databaseSizeBeforeUpdate);
        CourseAtlas testCourseAtlas = courseAtlasList.get(courseAtlasList.size() - 1);
        assertThat(testCourseAtlas.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    public void updateNonExistingCourseAtlas() throws Exception {
        int databaseSizeBeforeUpdate = courseAtlasRepository.findAll().size();

        // Create the CourseAtlas

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCourseAtlasMockMvc.perform(put("/api/course-atlases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseAtlas)))
            .andExpect(status().isCreated());

        // Validate the CourseAtlas in the database
        List<CourseAtlas> courseAtlasList = courseAtlasRepository.findAll();
        assertThat(courseAtlasList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCourseAtlas() throws Exception {
        // Initialize the database
        courseAtlasService.save(courseAtlas);

        int databaseSizeBeforeDelete = courseAtlasRepository.findAll().size();

        // Get the courseAtlas
        restCourseAtlasMockMvc.perform(delete("/api/course-atlases/{id}", courseAtlas.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CourseAtlas> courseAtlasList = courseAtlasRepository.findAll();
        assertThat(courseAtlasList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseAtlas.class);
        CourseAtlas courseAtlas1 = new CourseAtlas();
        courseAtlas1.setId(1L);
        CourseAtlas courseAtlas2 = new CourseAtlas();
        courseAtlas2.setId(courseAtlas1.getId());
        assertThat(courseAtlas1).isEqualTo(courseAtlas2);
        courseAtlas2.setId(2L);
        assertThat(courseAtlas1).isNotEqualTo(courseAtlas2);
        courseAtlas1.setId(null);
        assertThat(courseAtlas1).isNotEqualTo(courseAtlas2);
    }
}
