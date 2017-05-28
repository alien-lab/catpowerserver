package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.CatpowerserverApp;

import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.repository.LearnerRepository;
import com.alienlab.catpower.service.LearnerService;
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
 * Test class for the LearnerResource REST controller.
 *
 * @see LearnerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatpowerserverApp.class)
public class LearnerResourceIntTest {

    private static final String DEFAULT_LEARNE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEARNE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LEARNER_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_LEARNER_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_LEARNER_SEX = "AAAAAAAAAA";
    private static final String UPDATED_LEARNER_SEX = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_REGIST_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REGIST_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_WX_OPEN_ID = "AAAAAAAAAA";
    private static final String UPDATED_WX_OPEN_ID = "BBBBBBBBBB";

    private static final String DEFAULT_WX_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_WX_NICKNAME = "BBBBBBBBBB";

    private static final String DEFAULT_WX_HEADER = "AAAAAAAAAA";
    private static final String UPDATED_WX_HEADER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FIRST_TOTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FIRST_TOTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_FIRST_BUYCLASS = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FIRST_BUYCLASS = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_RECENTLY_SIGNIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RECENTLY_SIGNIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_EXPERIENCE = 1L;
    private static final Long UPDATED_EXPERIENCE = 2L;

    @Autowired
    private LearnerRepository learnerRepository;

    @Autowired
    private LearnerService learnerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLearnerMockMvc;

    private Learner learner;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LearnerResource learnerResource = new LearnerResource(learnerService);
        this.restLearnerMockMvc = MockMvcBuilders.standaloneSetup(learnerResource)
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
    public static Learner createEntity(EntityManager em) {
        Learner learner = new Learner()
            .learneName(DEFAULT_LEARNE_NAME)
            .learnerPhone(DEFAULT_LEARNER_PHONE)
            .learnerSex(DEFAULT_LEARNER_SEX)
            .registTime(DEFAULT_REGIST_TIME)
            .wxOpenId(DEFAULT_WX_OPEN_ID)
            .wxNickname(DEFAULT_WX_NICKNAME)
            .wxHeader(DEFAULT_WX_HEADER)
            .firstTotime(DEFAULT_FIRST_TOTIME)
            .firstBuyclass(DEFAULT_FIRST_BUYCLASS)
            .recentlySignin(DEFAULT_RECENTLY_SIGNIN)
            .experience(DEFAULT_EXPERIENCE);
        return learner;
    }

    @Before
    public void initTest() {
        learner = createEntity(em);
    }

    @Test
    @Transactional
    public void createLearner() throws Exception {
        int databaseSizeBeforeCreate = learnerRepository.findAll().size();

        // Create the Learner
        restLearnerMockMvc.perform(post("/api/learners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learner)))
            .andExpect(status().isCreated());

        // Validate the Learner in the database
        List<Learner> learnerList = learnerRepository.findAll();
        assertThat(learnerList).hasSize(databaseSizeBeforeCreate + 1);
        Learner testLearner = learnerList.get(learnerList.size() - 1);
        assertThat(testLearner.getLearneName()).isEqualTo(DEFAULT_LEARNE_NAME);
        assertThat(testLearner.getLearnerPhone()).isEqualTo(DEFAULT_LEARNER_PHONE);
        assertThat(testLearner.getLearnerSex()).isEqualTo(DEFAULT_LEARNER_SEX);
        assertThat(testLearner.getRegistTime()).isEqualTo(DEFAULT_REGIST_TIME);
        assertThat(testLearner.getWxOpenId()).isEqualTo(DEFAULT_WX_OPEN_ID);
        assertThat(testLearner.getWxNickname()).isEqualTo(DEFAULT_WX_NICKNAME);
        assertThat(testLearner.getWxHeader()).isEqualTo(DEFAULT_WX_HEADER);
        assertThat(testLearner.getFirstTotime()).isEqualTo(DEFAULT_FIRST_TOTIME);
        assertThat(testLearner.getFirstBuyclass()).isEqualTo(DEFAULT_FIRST_BUYCLASS);
        assertThat(testLearner.getRecentlySignin()).isEqualTo(DEFAULT_RECENTLY_SIGNIN);
        assertThat(testLearner.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
    }

    @Test
    @Transactional
    public void createLearnerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = learnerRepository.findAll().size();

        // Create the Learner with an existing ID
        learner.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLearnerMockMvc.perform(post("/api/learners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learner)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Learner> learnerList = learnerRepository.findAll();
        assertThat(learnerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLearners() throws Exception {
        // Initialize the database
        learnerRepository.saveAndFlush(learner);

        // Get all the learnerList
        restLearnerMockMvc.perform(get("/api/learners?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(learner.getId().intValue())))
            .andExpect(jsonPath("$.[*].learneName").value(hasItem(DEFAULT_LEARNE_NAME.toString())))
            .andExpect(jsonPath("$.[*].learnerPhone").value(hasItem(DEFAULT_LEARNER_PHONE.toString())))
            .andExpect(jsonPath("$.[*].learnerSex").value(hasItem(DEFAULT_LEARNER_SEX.toString())))
            .andExpect(jsonPath("$.[*].registTime").value(hasItem(sameInstant(DEFAULT_REGIST_TIME))))
            .andExpect(jsonPath("$.[*].wxOpenId").value(hasItem(DEFAULT_WX_OPEN_ID.toString())))
            .andExpect(jsonPath("$.[*].wxNickname").value(hasItem(DEFAULT_WX_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].wxHeader").value(hasItem(DEFAULT_WX_HEADER.toString())))
            .andExpect(jsonPath("$.[*].firstTotime").value(hasItem(sameInstant(DEFAULT_FIRST_TOTIME))))
            .andExpect(jsonPath("$.[*].firstBuyclass").value(hasItem(sameInstant(DEFAULT_FIRST_BUYCLASS))))
            .andExpect(jsonPath("$.[*].recentlySignin").value(hasItem(sameInstant(DEFAULT_RECENTLY_SIGNIN))))
            .andExpect(jsonPath("$.[*].experience").value(hasItem(DEFAULT_EXPERIENCE.intValue())));
    }

    @Test
    @Transactional
    public void getLearner() throws Exception {
        // Initialize the database
        learnerRepository.saveAndFlush(learner);

        // Get the learner
        restLearnerMockMvc.perform(get("/api/learners/{id}", learner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(learner.getId().intValue()))
            .andExpect(jsonPath("$.learneName").value(DEFAULT_LEARNE_NAME.toString()))
            .andExpect(jsonPath("$.learnerPhone").value(DEFAULT_LEARNER_PHONE.toString()))
            .andExpect(jsonPath("$.learnerSex").value(DEFAULT_LEARNER_SEX.toString()))
            .andExpect(jsonPath("$.registTime").value(sameInstant(DEFAULT_REGIST_TIME)))
            .andExpect(jsonPath("$.wxOpenId").value(DEFAULT_WX_OPEN_ID.toString()))
            .andExpect(jsonPath("$.wxNickname").value(DEFAULT_WX_NICKNAME.toString()))
            .andExpect(jsonPath("$.wxHeader").value(DEFAULT_WX_HEADER.toString()))
            .andExpect(jsonPath("$.firstTotime").value(sameInstant(DEFAULT_FIRST_TOTIME)))
            .andExpect(jsonPath("$.firstBuyclass").value(sameInstant(DEFAULT_FIRST_BUYCLASS)))
            .andExpect(jsonPath("$.recentlySignin").value(sameInstant(DEFAULT_RECENTLY_SIGNIN)))
            .andExpect(jsonPath("$.experience").value(DEFAULT_EXPERIENCE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLearner() throws Exception {
        // Get the learner
        restLearnerMockMvc.perform(get("/api/learners/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLearner() throws Exception {
        // Initialize the database
        learnerService.save(learner);

        int databaseSizeBeforeUpdate = learnerRepository.findAll().size();

        // Update the learner
        Learner updatedLearner = learnerRepository.findOne(learner.getId());
        updatedLearner
            .learneName(UPDATED_LEARNE_NAME)
            .learnerPhone(UPDATED_LEARNER_PHONE)
            .learnerSex(UPDATED_LEARNER_SEX)
            .registTime(UPDATED_REGIST_TIME)
            .wxOpenId(UPDATED_WX_OPEN_ID)
            .wxNickname(UPDATED_WX_NICKNAME)
            .wxHeader(UPDATED_WX_HEADER)
            .firstTotime(UPDATED_FIRST_TOTIME)
            .firstBuyclass(UPDATED_FIRST_BUYCLASS)
            .recentlySignin(UPDATED_RECENTLY_SIGNIN)
            .experience(UPDATED_EXPERIENCE);

        restLearnerMockMvc.perform(put("/api/learners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLearner)))
            .andExpect(status().isOk());

        // Validate the Learner in the database
        List<Learner> learnerList = learnerRepository.findAll();
        assertThat(learnerList).hasSize(databaseSizeBeforeUpdate);
        Learner testLearner = learnerList.get(learnerList.size() - 1);
        assertThat(testLearner.getLearneName()).isEqualTo(UPDATED_LEARNE_NAME);
        assertThat(testLearner.getLearnerPhone()).isEqualTo(UPDATED_LEARNER_PHONE);
        assertThat(testLearner.getLearnerSex()).isEqualTo(UPDATED_LEARNER_SEX);
        assertThat(testLearner.getRegistTime()).isEqualTo(UPDATED_REGIST_TIME);
        assertThat(testLearner.getWxOpenId()).isEqualTo(UPDATED_WX_OPEN_ID);
        assertThat(testLearner.getWxNickname()).isEqualTo(UPDATED_WX_NICKNAME);
        assertThat(testLearner.getWxHeader()).isEqualTo(UPDATED_WX_HEADER);
        assertThat(testLearner.getFirstTotime()).isEqualTo(UPDATED_FIRST_TOTIME);
        assertThat(testLearner.getFirstBuyclass()).isEqualTo(UPDATED_FIRST_BUYCLASS);
        assertThat(testLearner.getRecentlySignin()).isEqualTo(UPDATED_RECENTLY_SIGNIN);
        assertThat(testLearner.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
    }

    @Test
    @Transactional
    public void updateNonExistingLearner() throws Exception {
        int databaseSizeBeforeUpdate = learnerRepository.findAll().size();

        // Create the Learner

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLearnerMockMvc.perform(put("/api/learners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learner)))
            .andExpect(status().isCreated());

        // Validate the Learner in the database
        List<Learner> learnerList = learnerRepository.findAll();
        assertThat(learnerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLearner() throws Exception {
        // Initialize the database
        learnerService.save(learner);

        int databaseSizeBeforeDelete = learnerRepository.findAll().size();

        // Get the learner
        restLearnerMockMvc.perform(delete("/api/learners/{id}", learner.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Learner> learnerList = learnerRepository.findAll();
        assertThat(learnerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Learner.class);
        Learner learner1 = new Learner();
        learner1.setId(1L);
        Learner learner2 = new Learner();
        learner2.setId(learner1.getId());
        assertThat(learner1).isEqualTo(learner2);
        learner2.setId(2L);
        assertThat(learner1).isNotEqualTo(learner2);
        learner1.setId(null);
        assertThat(learner1).isNotEqualTo(learner2);
    }
}
