package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.CatpowerserverApp;

import com.alienlab.catpower.domain.LearnerInfo;
import com.alienlab.catpower.repository.LearnerInfoRepository;
import com.alienlab.catpower.service.LearnerInfoService;
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
 * Test class for the LearnerInfoResource REST controller.
 *
 * @see LearnerInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatpowerserverApp.class)
public class LearnerInfoResourceIntTest {

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_EXERCISE_DATA = "AAAAAAAAAA";
    private static final String UPDATED_EXERCISE_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_BODYTEST_DATA = "AAAAAAAAAA";
    private static final String UPDATED_BODYTEST_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_COACH_ADVICE = "AAAAAAAAAA";
    private static final String UPDATED_COACH_ADVICE = "BBBBBBBBBB";

    @Autowired
    private LearnerInfoRepository learnerInfoRepository;

    @Autowired
    private LearnerInfoService learnerInfoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLearnerInfoMockMvc;

    private LearnerInfo learnerInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LearnerInfoResource learnerInfoResource = new LearnerInfoResource(learnerInfoService);
        this.restLearnerInfoMockMvc = MockMvcBuilders.standaloneSetup(learnerInfoResource)
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
    public static LearnerInfo createEntity(EntityManager em) {
        LearnerInfo learnerInfo = new LearnerInfo()
            .time(DEFAULT_TIME)
            .exerciseData(DEFAULT_EXERCISE_DATA)
            .bodytestData(DEFAULT_BODYTEST_DATA)
            .coachAdvice(DEFAULT_COACH_ADVICE);
        return learnerInfo;
    }

    @Before
    public void initTest() {
        learnerInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createLearnerInfo() throws Exception {
        int databaseSizeBeforeCreate = learnerInfoRepository.findAll().size();

        // Create the LearnerInfo
        restLearnerInfoMockMvc.perform(post("/api/learner-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learnerInfo)))
            .andExpect(status().isCreated());

        // Validate the LearnerInfo in the database
        List<LearnerInfo> learnerInfoList = learnerInfoRepository.findAll();
        assertThat(learnerInfoList).hasSize(databaseSizeBeforeCreate + 1);
        LearnerInfo testLearnerInfo = learnerInfoList.get(learnerInfoList.size() - 1);
        assertThat(testLearnerInfo.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testLearnerInfo.getExerciseData()).isEqualTo(DEFAULT_EXERCISE_DATA);
        assertThat(testLearnerInfo.getBodytestData()).isEqualTo(DEFAULT_BODYTEST_DATA);
        assertThat(testLearnerInfo.getCoachAdvice()).isEqualTo(DEFAULT_COACH_ADVICE);
    }

    @Test
    @Transactional
    public void createLearnerInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = learnerInfoRepository.findAll().size();

        // Create the LearnerInfo with an existing ID
        learnerInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLearnerInfoMockMvc.perform(post("/api/learner-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learnerInfo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<LearnerInfo> learnerInfoList = learnerInfoRepository.findAll();
        assertThat(learnerInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLearnerInfos() throws Exception {
        // Initialize the database
        learnerInfoRepository.saveAndFlush(learnerInfo);

        // Get all the learnerInfoList
        restLearnerInfoMockMvc.perform(get("/api/learner-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(learnerInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))))
            .andExpect(jsonPath("$.[*].exerciseData").value(hasItem(DEFAULT_EXERCISE_DATA.toString())))
            .andExpect(jsonPath("$.[*].bodytestData").value(hasItem(DEFAULT_BODYTEST_DATA.toString())))
            .andExpect(jsonPath("$.[*].coachAdvice").value(hasItem(DEFAULT_COACH_ADVICE.toString())));
    }

    @Test
    @Transactional
    public void getLearnerInfo() throws Exception {
        // Initialize the database
        learnerInfoRepository.saveAndFlush(learnerInfo);

        // Get the learnerInfo
        restLearnerInfoMockMvc.perform(get("/api/learner-infos/{id}", learnerInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(learnerInfo.getId().intValue()))
            .andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)))
            .andExpect(jsonPath("$.exerciseData").value(DEFAULT_EXERCISE_DATA.toString()))
            .andExpect(jsonPath("$.bodytestData").value(DEFAULT_BODYTEST_DATA.toString()))
            .andExpect(jsonPath("$.coachAdvice").value(DEFAULT_COACH_ADVICE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLearnerInfo() throws Exception {
        // Get the learnerInfo
        restLearnerInfoMockMvc.perform(get("/api/learner-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLearnerInfo() throws Exception {
        // Initialize the database
        learnerInfoService.save(learnerInfo);

        int databaseSizeBeforeUpdate = learnerInfoRepository.findAll().size();

        // Update the learnerInfo
        LearnerInfo updatedLearnerInfo = learnerInfoRepository.findOne(learnerInfo.getId());
        updatedLearnerInfo
            .time(UPDATED_TIME)
            .exerciseData(UPDATED_EXERCISE_DATA)
            .bodytestData(UPDATED_BODYTEST_DATA)
            .coachAdvice(UPDATED_COACH_ADVICE);

        restLearnerInfoMockMvc.perform(put("/api/learner-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLearnerInfo)))
            .andExpect(status().isOk());

        // Validate the LearnerInfo in the database
        List<LearnerInfo> learnerInfoList = learnerInfoRepository.findAll();
        assertThat(learnerInfoList).hasSize(databaseSizeBeforeUpdate);
        LearnerInfo testLearnerInfo = learnerInfoList.get(learnerInfoList.size() - 1);
        assertThat(testLearnerInfo.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testLearnerInfo.getExerciseData()).isEqualTo(UPDATED_EXERCISE_DATA);
        assertThat(testLearnerInfo.getBodytestData()).isEqualTo(UPDATED_BODYTEST_DATA);
        assertThat(testLearnerInfo.getCoachAdvice()).isEqualTo(UPDATED_COACH_ADVICE);
    }

    @Test
    @Transactional
    public void updateNonExistingLearnerInfo() throws Exception {
        int databaseSizeBeforeUpdate = learnerInfoRepository.findAll().size();

        // Create the LearnerInfo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLearnerInfoMockMvc.perform(put("/api/learner-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learnerInfo)))
            .andExpect(status().isCreated());

        // Validate the LearnerInfo in the database
        List<LearnerInfo> learnerInfoList = learnerInfoRepository.findAll();
        assertThat(learnerInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLearnerInfo() throws Exception {
        // Initialize the database
        learnerInfoService.save(learnerInfo);

        int databaseSizeBeforeDelete = learnerInfoRepository.findAll().size();

        // Get the learnerInfo
        restLearnerInfoMockMvc.perform(delete("/api/learner-infos/{id}", learnerInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LearnerInfo> learnerInfoList = learnerInfoRepository.findAll();
        assertThat(learnerInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LearnerInfo.class);
        LearnerInfo learnerInfo1 = new LearnerInfo();
        learnerInfo1.setId(1L);
        LearnerInfo learnerInfo2 = new LearnerInfo();
        learnerInfo2.setId(learnerInfo1.getId());
        assertThat(learnerInfo1).isEqualTo(learnerInfo2);
        learnerInfo2.setId(2L);
        assertThat(learnerInfo1).isNotEqualTo(learnerInfo2);
        learnerInfo1.setId(null);
        assertThat(learnerInfo1).isNotEqualTo(learnerInfo2);
    }
}
