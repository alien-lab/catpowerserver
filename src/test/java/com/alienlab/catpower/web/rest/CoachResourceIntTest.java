package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.CatpowerserverApp;

import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.repository.CoachRepository;
import com.alienlab.catpower.service.CoachService;
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
 * Test class for the CoachResource REST controller.
 *
 * @see CoachResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatpowerserverApp.class)
public class CoachResourceIntTest {

    private static final String DEFAULT_COACH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COACH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COACH_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_COACH_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_COACH_INTRODUCE = "AAAAAAAAAA";
    private static final String UPDATED_COACH_INTRODUCE = "BBBBBBBBBB";

    private static final String DEFAULT_COACH_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_COACH_PICTURE = "BBBBBBBBBB";

    private static final String DEFAULT_COACH_WECHATOPENID = "AAAAAAAAAA";
    private static final String UPDATED_COACH_WECHATOPENID = "BBBBBBBBBB";

    private static final String DEFAULT_COACH_WECHATNAME = "AAAAAAAAAA";
    private static final String UPDATED_COACH_WECHATNAME = "BBBBBBBBBB";

    private static final String DEFAULT_COACH_WECHATPICTURE = "AAAAAAAAAA";
    private static final String UPDATED_COACH_WECHATPICTURE = "BBBBBBBBBB";

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private CoachService coachService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCoachMockMvc;

    private Coach coach;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoachResource coachResource = new CoachResource(coachService);
        this.restCoachMockMvc = MockMvcBuilders.standaloneSetup(coachResource)
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
    public static Coach createEntity(EntityManager em) {
        Coach coach = new Coach()
            .coachName(DEFAULT_COACH_NAME)
            .coachPhone(DEFAULT_COACH_PHONE)
            .coachIntroduce(DEFAULT_COACH_INTRODUCE)
            .coachPicture(DEFAULT_COACH_PICTURE)
            .coachWechatopenid(DEFAULT_COACH_WECHATOPENID)
            .coachWechatname(DEFAULT_COACH_WECHATNAME)
            .coachWechatpicture(DEFAULT_COACH_WECHATPICTURE);
        return coach;
    }

    @Before
    public void initTest() {
        coach = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoach() throws Exception {
        int databaseSizeBeforeCreate = coachRepository.findAll().size();

        // Create the Coach
        restCoachMockMvc.perform(post("/api/coaches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coach)))
            .andExpect(status().isCreated());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeCreate + 1);
        Coach testCoach = coachList.get(coachList.size() - 1);
        assertThat(testCoach.getCoachName()).isEqualTo(DEFAULT_COACH_NAME);
        assertThat(testCoach.getCoachPhone()).isEqualTo(DEFAULT_COACH_PHONE);
        assertThat(testCoach.getCoachIntroduce()).isEqualTo(DEFAULT_COACH_INTRODUCE);
        assertThat(testCoach.getCoachPicture()).isEqualTo(DEFAULT_COACH_PICTURE);
        assertThat(testCoach.getCoachWechatopenid()).isEqualTo(DEFAULT_COACH_WECHATOPENID);
        assertThat(testCoach.getCoachWechatname()).isEqualTo(DEFAULT_COACH_WECHATNAME);
        assertThat(testCoach.getCoachWechatpicture()).isEqualTo(DEFAULT_COACH_WECHATPICTURE);
    }

    @Test
    @Transactional
    public void createCoachWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coachRepository.findAll().size();

        // Create the Coach with an existing ID
        coach.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoachMockMvc.perform(post("/api/coaches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coach)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCoaches() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

        // Get all the coachList
        restCoachMockMvc.perform(get("/api/coaches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coach.getId().intValue())))
            .andExpect(jsonPath("$.[*].coachName").value(hasItem(DEFAULT_COACH_NAME.toString())))
            .andExpect(jsonPath("$.[*].coachPhone").value(hasItem(DEFAULT_COACH_PHONE.toString())))
            .andExpect(jsonPath("$.[*].coachIntroduce").value(hasItem(DEFAULT_COACH_INTRODUCE.toString())))
            .andExpect(jsonPath("$.[*].coachPicture").value(hasItem(DEFAULT_COACH_PICTURE.toString())))
            .andExpect(jsonPath("$.[*].coachWechatopenid").value(hasItem(DEFAULT_COACH_WECHATOPENID.toString())))
            .andExpect(jsonPath("$.[*].coachWechatname").value(hasItem(DEFAULT_COACH_WECHATNAME.toString())))
            .andExpect(jsonPath("$.[*].coachWechatpicture").value(hasItem(DEFAULT_COACH_WECHATPICTURE.toString())));
    }

    @Test
    @Transactional
    public void getCoach() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

        // Get the coach
        restCoachMockMvc.perform(get("/api/coaches/{id}", coach.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coach.getId().intValue()))
            .andExpect(jsonPath("$.coachName").value(DEFAULT_COACH_NAME.toString()))
            .andExpect(jsonPath("$.coachPhone").value(DEFAULT_COACH_PHONE.toString()))
            .andExpect(jsonPath("$.coachIntroduce").value(DEFAULT_COACH_INTRODUCE.toString()))
            .andExpect(jsonPath("$.coachPicture").value(DEFAULT_COACH_PICTURE.toString()))
            .andExpect(jsonPath("$.coachWechatopenid").value(DEFAULT_COACH_WECHATOPENID.toString()))
            .andExpect(jsonPath("$.coachWechatname").value(DEFAULT_COACH_WECHATNAME.toString()))
            .andExpect(jsonPath("$.coachWechatpicture").value(DEFAULT_COACH_WECHATPICTURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoach() throws Exception {
        // Get the coach
        restCoachMockMvc.perform(get("/api/coaches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoach() throws Exception {
        // Initialize the database
        coachService.save(coach);

        int databaseSizeBeforeUpdate = coachRepository.findAll().size();

        // Update the coach
        Coach updatedCoach = coachRepository.findOne(coach.getId());
        updatedCoach
            .coachName(UPDATED_COACH_NAME)
            .coachPhone(UPDATED_COACH_PHONE)
            .coachIntroduce(UPDATED_COACH_INTRODUCE)
            .coachPicture(UPDATED_COACH_PICTURE)
            .coachWechatopenid(UPDATED_COACH_WECHATOPENID)
            .coachWechatname(UPDATED_COACH_WECHATNAME)
            .coachWechatpicture(UPDATED_COACH_WECHATPICTURE);

        restCoachMockMvc.perform(put("/api/coaches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCoach)))
            .andExpect(status().isOk());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeUpdate);
        Coach testCoach = coachList.get(coachList.size() - 1);
        assertThat(testCoach.getCoachName()).isEqualTo(UPDATED_COACH_NAME);
        assertThat(testCoach.getCoachPhone()).isEqualTo(UPDATED_COACH_PHONE);
        assertThat(testCoach.getCoachIntroduce()).isEqualTo(UPDATED_COACH_INTRODUCE);
        assertThat(testCoach.getCoachPicture()).isEqualTo(UPDATED_COACH_PICTURE);
        assertThat(testCoach.getCoachWechatopenid()).isEqualTo(UPDATED_COACH_WECHATOPENID);
        assertThat(testCoach.getCoachWechatname()).isEqualTo(UPDATED_COACH_WECHATNAME);
        assertThat(testCoach.getCoachWechatpicture()).isEqualTo(UPDATED_COACH_WECHATPICTURE);
    }

    @Test
    @Transactional
    public void updateNonExistingCoach() throws Exception {
        int databaseSizeBeforeUpdate = coachRepository.findAll().size();

        // Create the Coach

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCoachMockMvc.perform(put("/api/coaches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coach)))
            .andExpect(status().isCreated());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCoach() throws Exception {
        // Initialize the database
        coachService.save(coach);

        int databaseSizeBeforeDelete = coachRepository.findAll().size();

        // Get the coach
        restCoachMockMvc.perform(delete("/api/coaches/{id}", coach.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coach.class);
        Coach coach1 = new Coach();
        coach1.setId(1L);
        Coach coach2 = new Coach();
        coach2.setId(coach1.getId());
        assertThat(coach1).isEqualTo(coach2);
        coach2.setId(2L);
        assertThat(coach1).isNotEqualTo(coach2);
        coach1.setId(null);
        assertThat(coach1).isNotEqualTo(coach2);
    }
}
