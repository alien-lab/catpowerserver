package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.CatpowerserverApp;

import com.alienlab.catpower.domain.BuyCourse;
import com.alienlab.catpower.repository.BuyCourseRepository;
import com.alienlab.catpower.service.BuyCourseService;
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
 * Test class for the BuyCourseResource REST controller.
 *
 * @see BuyCourseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatpowerserverApp.class)
public class BuyCourseResourceIntTest {

    private static final String DEFAULT_PAYMENT_WAY = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_WAY = "BBBBBBBBBB";

    private static final Float DEFAULT_PAYMENT_ACCOUNT = 1F;
    private static final Float UPDATED_PAYMENT_ACCOUNT = 2F;

    private static final ZonedDateTime DEFAULT_BUY_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_BUY_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_OPERATOR = "AAAAAAAAAA";
    private static final String UPDATED_OPERATOR = "BBBBBBBBBB";

    private static final String DEFAULT_OPERATE_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_OPERATE_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_OPERATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_OPERATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_REMAIN_CLASS = 1L;
    private static final Long UPDATED_REMAIN_CLASS = 2L;

    @Autowired
    private BuyCourseRepository buyCourseRepository;

    @Autowired
    private BuyCourseService buyCourseService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBuyCourseMockMvc;

    private BuyCourse buyCourse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BuyCourseResource buyCourseResource = new BuyCourseResource(buyCourseService);
        this.restBuyCourseMockMvc = MockMvcBuilders.standaloneSetup(buyCourseResource)
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
    public static BuyCourse createEntity(EntityManager em) {
        BuyCourse buyCourse = new BuyCourse()
            .paymentWay(DEFAULT_PAYMENT_WAY)
            .paymentAccount(DEFAULT_PAYMENT_ACCOUNT)
            .buyTime(DEFAULT_BUY_TIME)
            .status(DEFAULT_STATUS)
            .operator(DEFAULT_OPERATOR)
            .operateContent(DEFAULT_OPERATE_CONTENT)
            .operateTime(DEFAULT_OPERATE_TIME)
            .remainClass(DEFAULT_REMAIN_CLASS);
        return buyCourse;
    }

    @Before
    public void initTest() {
        buyCourse = createEntity(em);
    }

    @Test
    @Transactional
    public void createBuyCourse() throws Exception {
        int databaseSizeBeforeCreate = buyCourseRepository.findAll().size();

        // Create the BuyCourse
        restBuyCourseMockMvc.perform(post("/api/buy-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(buyCourse)))
            .andExpect(status().isCreated());

        // Validate the BuyCourse in the database
        List<BuyCourse> buyCourseList = buyCourseRepository.findAll();
        assertThat(buyCourseList).hasSize(databaseSizeBeforeCreate + 1);
        BuyCourse testBuyCourse = buyCourseList.get(buyCourseList.size() - 1);
        assertThat(testBuyCourse.getPaymentWay()).isEqualTo(DEFAULT_PAYMENT_WAY);
        assertThat(testBuyCourse.getPaymentAccount()).isEqualTo(DEFAULT_PAYMENT_ACCOUNT);
        assertThat(testBuyCourse.getBuyTime()).isEqualTo(DEFAULT_BUY_TIME);
        assertThat(testBuyCourse.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBuyCourse.getOperator()).isEqualTo(DEFAULT_OPERATOR);
        assertThat(testBuyCourse.getOperateContent()).isEqualTo(DEFAULT_OPERATE_CONTENT);
        assertThat(testBuyCourse.getOperateTime()).isEqualTo(DEFAULT_OPERATE_TIME);
        assertThat(testBuyCourse.getRemainClass()).isEqualTo(DEFAULT_REMAIN_CLASS);
    }

    @Test
    @Transactional
    public void createBuyCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = buyCourseRepository.findAll().size();

        // Create the BuyCourse with an existing ID
        buyCourse.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBuyCourseMockMvc.perform(post("/api/buy-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(buyCourse)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BuyCourse> buyCourseList = buyCourseRepository.findAll();
        assertThat(buyCourseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBuyCourses() throws Exception {
        // Initialize the database
        buyCourseRepository.saveAndFlush(buyCourse);

        // Get all the buyCourseList
        restBuyCourseMockMvc.perform(get("/api/buy-courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(buyCourse.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentWay").value(hasItem(DEFAULT_PAYMENT_WAY.toString())))
            .andExpect(jsonPath("$.[*].paymentAccount").value(hasItem(DEFAULT_PAYMENT_ACCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].buyTime").value(hasItem(sameInstant(DEFAULT_BUY_TIME))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].operator").value(hasItem(DEFAULT_OPERATOR.toString())))
            .andExpect(jsonPath("$.[*].operateContent").value(hasItem(DEFAULT_OPERATE_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].operateTime").value(hasItem(sameInstant(DEFAULT_OPERATE_TIME))))
            .andExpect(jsonPath("$.[*].remainClass").value(hasItem(DEFAULT_REMAIN_CLASS.intValue())));
    }

    @Test
    @Transactional
    public void getBuyCourse() throws Exception {
        // Initialize the database
        buyCourseRepository.saveAndFlush(buyCourse);

        // Get the buyCourse
        restBuyCourseMockMvc.perform(get("/api/buy-courses/{id}", buyCourse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(buyCourse.getId().intValue()))
            .andExpect(jsonPath("$.paymentWay").value(DEFAULT_PAYMENT_WAY.toString()))
            .andExpect(jsonPath("$.paymentAccount").value(DEFAULT_PAYMENT_ACCOUNT.doubleValue()))
            .andExpect(jsonPath("$.buyTime").value(sameInstant(DEFAULT_BUY_TIME)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.operator").value(DEFAULT_OPERATOR.toString()))
            .andExpect(jsonPath("$.operateContent").value(DEFAULT_OPERATE_CONTENT.toString()))
            .andExpect(jsonPath("$.operateTime").value(sameInstant(DEFAULT_OPERATE_TIME)))
            .andExpect(jsonPath("$.remainClass").value(DEFAULT_REMAIN_CLASS.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBuyCourse() throws Exception {
        // Get the buyCourse
        restBuyCourseMockMvc.perform(get("/api/buy-courses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBuyCourse() throws Exception {
        // Initialize the database
        buyCourseService.save(buyCourse);

        int databaseSizeBeforeUpdate = buyCourseRepository.findAll().size();

        // Update the buyCourse
        BuyCourse updatedBuyCourse = buyCourseRepository.findOne(buyCourse.getId());
        updatedBuyCourse
            .paymentWay(UPDATED_PAYMENT_WAY)
            .paymentAccount(UPDATED_PAYMENT_ACCOUNT)
            .buyTime(UPDATED_BUY_TIME)
            .status(UPDATED_STATUS)
            .operator(UPDATED_OPERATOR)
            .operateContent(UPDATED_OPERATE_CONTENT)
            .operateTime(UPDATED_OPERATE_TIME)
            .remainClass(UPDATED_REMAIN_CLASS);

        restBuyCourseMockMvc.perform(put("/api/buy-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBuyCourse)))
            .andExpect(status().isOk());

        // Validate the BuyCourse in the database
        List<BuyCourse> buyCourseList = buyCourseRepository.findAll();
        assertThat(buyCourseList).hasSize(databaseSizeBeforeUpdate);
        BuyCourse testBuyCourse = buyCourseList.get(buyCourseList.size() - 1);
        assertThat(testBuyCourse.getPaymentWay()).isEqualTo(UPDATED_PAYMENT_WAY);
        assertThat(testBuyCourse.getPaymentAccount()).isEqualTo(UPDATED_PAYMENT_ACCOUNT);
        assertThat(testBuyCourse.getBuyTime()).isEqualTo(UPDATED_BUY_TIME);
        assertThat(testBuyCourse.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBuyCourse.getOperator()).isEqualTo(UPDATED_OPERATOR);
        assertThat(testBuyCourse.getOperateContent()).isEqualTo(UPDATED_OPERATE_CONTENT);
        assertThat(testBuyCourse.getOperateTime()).isEqualTo(UPDATED_OPERATE_TIME);
        assertThat(testBuyCourse.getRemainClass()).isEqualTo(UPDATED_REMAIN_CLASS);
    }

    @Test
    @Transactional
    public void updateNonExistingBuyCourse() throws Exception {
        int databaseSizeBeforeUpdate = buyCourseRepository.findAll().size();

        // Create the BuyCourse

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBuyCourseMockMvc.perform(put("/api/buy-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(buyCourse)))
            .andExpect(status().isCreated());

        // Validate the BuyCourse in the database
        List<BuyCourse> buyCourseList = buyCourseRepository.findAll();
        assertThat(buyCourseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBuyCourse() throws Exception {
        // Initialize the database
        buyCourseService.save(buyCourse);

        int databaseSizeBeforeDelete = buyCourseRepository.findAll().size();

        // Get the buyCourse
        restBuyCourseMockMvc.perform(delete("/api/buy-courses/{id}", buyCourse.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BuyCourse> buyCourseList = buyCourseRepository.findAll();
        assertThat(buyCourseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BuyCourse.class);
        BuyCourse buyCourse1 = new BuyCourse();
        buyCourse1.setId(1L);
        BuyCourse buyCourse2 = new BuyCourse();
        buyCourse2.setId(buyCourse1.getId());
        assertThat(buyCourse1).isEqualTo(buyCourse2);
        buyCourse2.setId(2L);
        assertThat(buyCourse1).isNotEqualTo(buyCourse2);
        buyCourse1.setId(null);
        assertThat(buyCourse1).isNotEqualTo(buyCourse2);
    }
}
