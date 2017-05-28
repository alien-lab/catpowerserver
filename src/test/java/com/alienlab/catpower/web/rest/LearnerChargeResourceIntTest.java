package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.CatpowerserverApp;

import com.alienlab.catpower.domain.LearnerCharge;
import com.alienlab.catpower.repository.LearnerChargeRepository;
import com.alienlab.catpower.service.LearnerChargeService;
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
 * Test class for the LearnerChargeResource REST controller.
 *
 * @see LearnerChargeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatpowerserverApp.class)
public class LearnerChargeResourceIntTest {

    private static final ZonedDateTime DEFAULT_CHARGE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CHARGE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_BUY_COURSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_BUY_COURSE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CHARGE_PEOPLE = "AAAAAAAAAA";
    private static final String UPDATED_CHARGE_PEOPLE = "BBBBBBBBBB";

    private static final Long DEFAULT_REMAIN_NUMBER = 1L;
    private static final Long UPDATED_REMAIN_NUMBER = 2L;

    @Autowired
    private LearnerChargeRepository learnerChargeRepository;

    @Autowired
    private LearnerChargeService learnerChargeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLearnerChargeMockMvc;

    private LearnerCharge learnerCharge;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LearnerChargeResource learnerChargeResource = new LearnerChargeResource(learnerChargeService);
        this.restLearnerChargeMockMvc = MockMvcBuilders.standaloneSetup(learnerChargeResource)
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
    public static LearnerCharge createEntity(EntityManager em) {
        LearnerCharge learnerCharge = new LearnerCharge()
            .chargeTime(DEFAULT_CHARGE_TIME)
            .buyCourseId(DEFAULT_BUY_COURSE_ID)
            .chargePeople(DEFAULT_CHARGE_PEOPLE)
            .remainNumber(DEFAULT_REMAIN_NUMBER);
        return learnerCharge;
    }

    @Before
    public void initTest() {
        learnerCharge = createEntity(em);
    }

    @Test
    @Transactional
    public void createLearnerCharge() throws Exception {
        int databaseSizeBeforeCreate = learnerChargeRepository.findAll().size();

        // Create the LearnerCharge
        restLearnerChargeMockMvc.perform(post("/api/learner-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learnerCharge)))
            .andExpect(status().isCreated());

        // Validate the LearnerCharge in the database
        List<LearnerCharge> learnerChargeList = learnerChargeRepository.findAll();
        assertThat(learnerChargeList).hasSize(databaseSizeBeforeCreate + 1);
        LearnerCharge testLearnerCharge = learnerChargeList.get(learnerChargeList.size() - 1);
        assertThat(testLearnerCharge.getChargeTime()).isEqualTo(DEFAULT_CHARGE_TIME);
        assertThat(testLearnerCharge.getBuyCourseId()).isEqualTo(DEFAULT_BUY_COURSE_ID);
        assertThat(testLearnerCharge.getChargePeople()).isEqualTo(DEFAULT_CHARGE_PEOPLE);
        assertThat(testLearnerCharge.getRemainNumber()).isEqualTo(DEFAULT_REMAIN_NUMBER);
    }

    @Test
    @Transactional
    public void createLearnerChargeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = learnerChargeRepository.findAll().size();

        // Create the LearnerCharge with an existing ID
        learnerCharge.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLearnerChargeMockMvc.perform(post("/api/learner-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learnerCharge)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<LearnerCharge> learnerChargeList = learnerChargeRepository.findAll();
        assertThat(learnerChargeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLearnerCharges() throws Exception {
        // Initialize the database
        learnerChargeRepository.saveAndFlush(learnerCharge);

        // Get all the learnerChargeList
        restLearnerChargeMockMvc.perform(get("/api/learner-charges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(learnerCharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].chargeTime").value(hasItem(sameInstant(DEFAULT_CHARGE_TIME))))
            .andExpect(jsonPath("$.[*].buyCourseId").value(hasItem(DEFAULT_BUY_COURSE_ID.toString())))
            .andExpect(jsonPath("$.[*].chargePeople").value(hasItem(DEFAULT_CHARGE_PEOPLE.toString())))
            .andExpect(jsonPath("$.[*].remainNumber").value(hasItem(DEFAULT_REMAIN_NUMBER.intValue())));
    }

    @Test
    @Transactional
    public void getLearnerCharge() throws Exception {
        // Initialize the database
        learnerChargeRepository.saveAndFlush(learnerCharge);

        // Get the learnerCharge
        restLearnerChargeMockMvc.perform(get("/api/learner-charges/{id}", learnerCharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(learnerCharge.getId().intValue()))
            .andExpect(jsonPath("$.chargeTime").value(sameInstant(DEFAULT_CHARGE_TIME)))
            .andExpect(jsonPath("$.buyCourseId").value(DEFAULT_BUY_COURSE_ID.toString()))
            .andExpect(jsonPath("$.chargePeople").value(DEFAULT_CHARGE_PEOPLE.toString()))
            .andExpect(jsonPath("$.remainNumber").value(DEFAULT_REMAIN_NUMBER.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLearnerCharge() throws Exception {
        // Get the learnerCharge
        restLearnerChargeMockMvc.perform(get("/api/learner-charges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLearnerCharge() throws Exception {
        // Initialize the database
        learnerChargeService.save(learnerCharge);

        int databaseSizeBeforeUpdate = learnerChargeRepository.findAll().size();

        // Update the learnerCharge
        LearnerCharge updatedLearnerCharge = learnerChargeRepository.findOne(learnerCharge.getId());
        updatedLearnerCharge
            .chargeTime(UPDATED_CHARGE_TIME)
            .buyCourseId(UPDATED_BUY_COURSE_ID)
            .chargePeople(UPDATED_CHARGE_PEOPLE)
            .remainNumber(UPDATED_REMAIN_NUMBER);

        restLearnerChargeMockMvc.perform(put("/api/learner-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLearnerCharge)))
            .andExpect(status().isOk());

        // Validate the LearnerCharge in the database
        List<LearnerCharge> learnerChargeList = learnerChargeRepository.findAll();
        assertThat(learnerChargeList).hasSize(databaseSizeBeforeUpdate);
        LearnerCharge testLearnerCharge = learnerChargeList.get(learnerChargeList.size() - 1);
        assertThat(testLearnerCharge.getChargeTime()).isEqualTo(UPDATED_CHARGE_TIME);
        assertThat(testLearnerCharge.getBuyCourseId()).isEqualTo(UPDATED_BUY_COURSE_ID);
        assertThat(testLearnerCharge.getChargePeople()).isEqualTo(UPDATED_CHARGE_PEOPLE);
        assertThat(testLearnerCharge.getRemainNumber()).isEqualTo(UPDATED_REMAIN_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingLearnerCharge() throws Exception {
        int databaseSizeBeforeUpdate = learnerChargeRepository.findAll().size();

        // Create the LearnerCharge

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLearnerChargeMockMvc.perform(put("/api/learner-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learnerCharge)))
            .andExpect(status().isCreated());

        // Validate the LearnerCharge in the database
        List<LearnerCharge> learnerChargeList = learnerChargeRepository.findAll();
        assertThat(learnerChargeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLearnerCharge() throws Exception {
        // Initialize the database
        learnerChargeService.save(learnerCharge);

        int databaseSizeBeforeDelete = learnerChargeRepository.findAll().size();

        // Get the learnerCharge
        restLearnerChargeMockMvc.perform(delete("/api/learner-charges/{id}", learnerCharge.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LearnerCharge> learnerChargeList = learnerChargeRepository.findAll();
        assertThat(learnerChargeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LearnerCharge.class);
        LearnerCharge learnerCharge1 = new LearnerCharge();
        learnerCharge1.setId(1L);
        LearnerCharge learnerCharge2 = new LearnerCharge();
        learnerCharge2.setId(learnerCharge1.getId());
        assertThat(learnerCharge1).isEqualTo(learnerCharge2);
        learnerCharge2.setId(2L);
        assertThat(learnerCharge1).isNotEqualTo(learnerCharge2);
        learnerCharge1.setId(null);
        assertThat(learnerCharge1).isNotEqualTo(learnerCharge2);
    }
}
