package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.CatpowerserverApp;

import com.alienlab.catpower.domain.CoachEvaluate;
import com.alienlab.catpower.repository.CoachEvaluateRepository;
import com.alienlab.catpower.service.CoachEvaluateService;
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
 * Test class for the CoachEvaluateResource REST controller.
 *
 * @see CoachEvaluateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatpowerserverApp.class)
public class CoachEvaluateResourceIntTest {

    private static final Long DEFAULT_SERVICE_ATTITUDE = 1L;
    private static final Long UPDATED_SERVICE_ATTITUDE = 2L;

    private static final Long DEFAULT_SPECIALITY = 1L;
    private static final Long UPDATED_SPECIALITY = 2L;

    private static final Long DEFAULT_LIKE = 1L;
    private static final Long UPDATED_LIKE = 2L;

    private static final String DEFAULT_COMPLAIN = "AAAAAAAAAA";
    private static final String UPDATED_COMPLAIN = "BBBBBBBBBB";

    private static final Long DEFAULT_EVALUATION = 1L;
    private static final Long UPDATED_EVALUATION = 2L;

    @Autowired
    private CoachEvaluateRepository coachEvaluateRepository;

    @Autowired
    private CoachEvaluateService coachEvaluateService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCoachEvaluateMockMvc;

    private CoachEvaluate coachEvaluate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoachEvaluateResource coachEvaluateResource = new CoachEvaluateResource(coachEvaluateService);
        this.restCoachEvaluateMockMvc = MockMvcBuilders.standaloneSetup(coachEvaluateResource)
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
    public static CoachEvaluate createEntity(EntityManager em) {
        CoachEvaluate coachEvaluate = new CoachEvaluate()
            .serviceAttitude(DEFAULT_SERVICE_ATTITUDE)
            .speciality(DEFAULT_SPECIALITY)
            .like(DEFAULT_LIKE)
            .complain(DEFAULT_COMPLAIN)
            .evaluation(DEFAULT_EVALUATION);
        return coachEvaluate;
    }

    @Before
    public void initTest() {
        coachEvaluate = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoachEvaluate() throws Exception {
        int databaseSizeBeforeCreate = coachEvaluateRepository.findAll().size();

        // Create the CoachEvaluate
        restCoachEvaluateMockMvc.perform(post("/api/coach-evaluates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coachEvaluate)))
            .andExpect(status().isCreated());

        // Validate the CoachEvaluate in the database
        List<CoachEvaluate> coachEvaluateList = coachEvaluateRepository.findAll();
        assertThat(coachEvaluateList).hasSize(databaseSizeBeforeCreate + 1);
        CoachEvaluate testCoachEvaluate = coachEvaluateList.get(coachEvaluateList.size() - 1);
        assertThat(testCoachEvaluate.getServiceAttitude()).isEqualTo(DEFAULT_SERVICE_ATTITUDE);
        assertThat(testCoachEvaluate.getSpeciality()).isEqualTo(DEFAULT_SPECIALITY);
        assertThat(testCoachEvaluate.getLike()).isEqualTo(DEFAULT_LIKE);
        assertThat(testCoachEvaluate.getComplain()).isEqualTo(DEFAULT_COMPLAIN);
        assertThat(testCoachEvaluate.getEvaluation()).isEqualTo(DEFAULT_EVALUATION);
    }

    @Test
    @Transactional
    public void createCoachEvaluateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coachEvaluateRepository.findAll().size();

        // Create the CoachEvaluate with an existing ID
        coachEvaluate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoachEvaluateMockMvc.perform(post("/api/coach-evaluates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coachEvaluate)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CoachEvaluate> coachEvaluateList = coachEvaluateRepository.findAll();
        assertThat(coachEvaluateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCoachEvaluates() throws Exception {
        // Initialize the database
        coachEvaluateRepository.saveAndFlush(coachEvaluate);

        // Get all the coachEvaluateList
        restCoachEvaluateMockMvc.perform(get("/api/coach-evaluates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coachEvaluate.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceAttitude").value(hasItem(DEFAULT_SERVICE_ATTITUDE.intValue())))
            .andExpect(jsonPath("$.[*].speciality").value(hasItem(DEFAULT_SPECIALITY.intValue())))
            .andExpect(jsonPath("$.[*].like").value(hasItem(DEFAULT_LIKE.intValue())))
            .andExpect(jsonPath("$.[*].complain").value(hasItem(DEFAULT_COMPLAIN.toString())))
            .andExpect(jsonPath("$.[*].evaluation").value(hasItem(DEFAULT_EVALUATION.intValue())));
    }

    @Test
    @Transactional
    public void getCoachEvaluate() throws Exception {
        // Initialize the database
        coachEvaluateRepository.saveAndFlush(coachEvaluate);

        // Get the coachEvaluate
        restCoachEvaluateMockMvc.perform(get("/api/coach-evaluates/{id}", coachEvaluate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coachEvaluate.getId().intValue()))
            .andExpect(jsonPath("$.serviceAttitude").value(DEFAULT_SERVICE_ATTITUDE.intValue()))
            .andExpect(jsonPath("$.speciality").value(DEFAULT_SPECIALITY.intValue()))
            .andExpect(jsonPath("$.like").value(DEFAULT_LIKE.intValue()))
            .andExpect(jsonPath("$.complain").value(DEFAULT_COMPLAIN.toString()))
            .andExpect(jsonPath("$.evaluation").value(DEFAULT_EVALUATION.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCoachEvaluate() throws Exception {
        // Get the coachEvaluate
        restCoachEvaluateMockMvc.perform(get("/api/coach-evaluates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoachEvaluate() throws Exception {
        // Initialize the database
        coachEvaluateService.save(coachEvaluate);

        int databaseSizeBeforeUpdate = coachEvaluateRepository.findAll().size();

        // Update the coachEvaluate
        CoachEvaluate updatedCoachEvaluate = coachEvaluateRepository.findOne(coachEvaluate.getId());
        updatedCoachEvaluate
            .serviceAttitude(UPDATED_SERVICE_ATTITUDE)
            .speciality(UPDATED_SPECIALITY)
            .like(UPDATED_LIKE)
            .complain(UPDATED_COMPLAIN)
            .evaluation(UPDATED_EVALUATION);

        restCoachEvaluateMockMvc.perform(put("/api/coach-evaluates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCoachEvaluate)))
            .andExpect(status().isOk());

        // Validate the CoachEvaluate in the database
        List<CoachEvaluate> coachEvaluateList = coachEvaluateRepository.findAll();
        assertThat(coachEvaluateList).hasSize(databaseSizeBeforeUpdate);
        CoachEvaluate testCoachEvaluate = coachEvaluateList.get(coachEvaluateList.size() - 1);
        assertThat(testCoachEvaluate.getServiceAttitude()).isEqualTo(UPDATED_SERVICE_ATTITUDE);
        assertThat(testCoachEvaluate.getSpeciality()).isEqualTo(UPDATED_SPECIALITY);
        assertThat(testCoachEvaluate.getLike()).isEqualTo(UPDATED_LIKE);
        assertThat(testCoachEvaluate.getComplain()).isEqualTo(UPDATED_COMPLAIN);
        assertThat(testCoachEvaluate.getEvaluation()).isEqualTo(UPDATED_EVALUATION);
    }

    @Test
    @Transactional
    public void updateNonExistingCoachEvaluate() throws Exception {
        int databaseSizeBeforeUpdate = coachEvaluateRepository.findAll().size();

        // Create the CoachEvaluate

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCoachEvaluateMockMvc.perform(put("/api/coach-evaluates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coachEvaluate)))
            .andExpect(status().isCreated());

        // Validate the CoachEvaluate in the database
        List<CoachEvaluate> coachEvaluateList = coachEvaluateRepository.findAll();
        assertThat(coachEvaluateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCoachEvaluate() throws Exception {
        // Initialize the database
        coachEvaluateService.save(coachEvaluate);

        int databaseSizeBeforeDelete = coachEvaluateRepository.findAll().size();

        // Get the coachEvaluate
        restCoachEvaluateMockMvc.perform(delete("/api/coach-evaluates/{id}", coachEvaluate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CoachEvaluate> coachEvaluateList = coachEvaluateRepository.findAll();
        assertThat(coachEvaluateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoachEvaluate.class);
        CoachEvaluate coachEvaluate1 = new CoachEvaluate();
        coachEvaluate1.setId(1L);
        CoachEvaluate coachEvaluate2 = new CoachEvaluate();
        coachEvaluate2.setId(coachEvaluate1.getId());
        assertThat(coachEvaluate1).isEqualTo(coachEvaluate2);
        coachEvaluate2.setId(2L);
        assertThat(coachEvaluate1).isNotEqualTo(coachEvaluate2);
        coachEvaluate1.setId(null);
        assertThat(coachEvaluate1).isNotEqualTo(coachEvaluate2);
    }
}
