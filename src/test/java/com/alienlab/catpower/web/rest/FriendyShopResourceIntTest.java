package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.CatpowerserverApp;

import com.alienlab.catpower.domain.FriendyShop;
import com.alienlab.catpower.repository.FriendyShopRepository;
import com.alienlab.catpower.service.FriendyShopService;
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
 * Test class for the FriendyShopResource REST controller.
 *
 * @see FriendyShopResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatpowerserverApp.class)
public class FriendyShopResourceIntTest {

    private static final String DEFAULT_SHOP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHOP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHOP_DESC = "AAAAAAAAAA";
    private static final String UPDATED_SHOP_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_SHOP_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_SHOP_POSITION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private FriendyShopRepository friendyShopRepository;

    @Autowired
    private FriendyShopService friendyShopService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFriendyShopMockMvc;

    private FriendyShop friendyShop;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FriendyShopResource friendyShopResource = new FriendyShopResource(friendyShopService);
        this.restFriendyShopMockMvc = MockMvcBuilders.standaloneSetup(friendyShopResource)
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
    public static FriendyShop createEntity(EntityManager em) {
        FriendyShop friendyShop = new FriendyShop()
            .shopName(DEFAULT_SHOP_NAME)
            .shopDesc(DEFAULT_SHOP_DESC)
            .shopPosition(DEFAULT_SHOP_POSITION)
            .createTime(DEFAULT_CREATE_TIME)
            .endTime(DEFAULT_END_TIME);
        return friendyShop;
    }

    @Before
    public void initTest() {
        friendyShop = createEntity(em);
    }

    @Test
    @Transactional
    public void createFriendyShop() throws Exception {
        int databaseSizeBeforeCreate = friendyShopRepository.findAll().size();

        // Create the FriendyShop
        restFriendyShopMockMvc.perform(post("/api/friendy-shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(friendyShop)))
            .andExpect(status().isCreated());

        // Validate the FriendyShop in the database
        List<FriendyShop> friendyShopList = friendyShopRepository.findAll();
        assertThat(friendyShopList).hasSize(databaseSizeBeforeCreate + 1);
        FriendyShop testFriendyShop = friendyShopList.get(friendyShopList.size() - 1);
        assertThat(testFriendyShop.getShopName()).isEqualTo(DEFAULT_SHOP_NAME);
        assertThat(testFriendyShop.getShopDesc()).isEqualTo(DEFAULT_SHOP_DESC);
        assertThat(testFriendyShop.getShopPosition()).isEqualTo(DEFAULT_SHOP_POSITION);
        assertThat(testFriendyShop.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testFriendyShop.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    public void createFriendyShopWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = friendyShopRepository.findAll().size();

        // Create the FriendyShop with an existing ID
        friendyShop.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFriendyShopMockMvc.perform(post("/api/friendy-shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(friendyShop)))
            .andExpect(status().isBadRequest());

        // Validate the FriendyShop in the database
        List<FriendyShop> friendyShopList = friendyShopRepository.findAll();
        assertThat(friendyShopList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFriendyShops() throws Exception {
        // Initialize the database
        friendyShopRepository.saveAndFlush(friendyShop);

        // Get all the friendyShopList
        restFriendyShopMockMvc.perform(get("/api/friendy-shops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(friendyShop.getId().intValue())))
            .andExpect(jsonPath("$.[*].shopName").value(hasItem(DEFAULT_SHOP_NAME.toString())))
            .andExpect(jsonPath("$.[*].shopDesc").value(hasItem(DEFAULT_SHOP_DESC.toString())))
            .andExpect(jsonPath("$.[*].shopPosition").value(hasItem(DEFAULT_SHOP_POSITION.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(sameInstant(DEFAULT_CREATE_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))));
    }

    @Test
    @Transactional
    public void getFriendyShop() throws Exception {
        // Initialize the database
        friendyShopRepository.saveAndFlush(friendyShop);

        // Get the friendyShop
        restFriendyShopMockMvc.perform(get("/api/friendy-shops/{id}", friendyShop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(friendyShop.getId().intValue()))
            .andExpect(jsonPath("$.shopName").value(DEFAULT_SHOP_NAME.toString()))
            .andExpect(jsonPath("$.shopDesc").value(DEFAULT_SHOP_DESC.toString()))
            .andExpect(jsonPath("$.shopPosition").value(DEFAULT_SHOP_POSITION.toString()))
            .andExpect(jsonPath("$.createTime").value(sameInstant(DEFAULT_CREATE_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingFriendyShop() throws Exception {
        // Get the friendyShop
        restFriendyShopMockMvc.perform(get("/api/friendy-shops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFriendyShop() throws Exception {
        // Initialize the database
        friendyShopService.save(friendyShop);

        int databaseSizeBeforeUpdate = friendyShopRepository.findAll().size();

        // Update the friendyShop
        FriendyShop updatedFriendyShop = friendyShopRepository.findOne(friendyShop.getId());
        updatedFriendyShop
            .shopName(UPDATED_SHOP_NAME)
            .shopDesc(UPDATED_SHOP_DESC)
            .shopPosition(UPDATED_SHOP_POSITION)
            .createTime(UPDATED_CREATE_TIME)
            .endTime(UPDATED_END_TIME);

        restFriendyShopMockMvc.perform(put("/api/friendy-shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFriendyShop)))
            .andExpect(status().isOk());

        // Validate the FriendyShop in the database
        List<FriendyShop> friendyShopList = friendyShopRepository.findAll();
        assertThat(friendyShopList).hasSize(databaseSizeBeforeUpdate);
        FriendyShop testFriendyShop = friendyShopList.get(friendyShopList.size() - 1);
        assertThat(testFriendyShop.getShopName()).isEqualTo(UPDATED_SHOP_NAME);
        assertThat(testFriendyShop.getShopDesc()).isEqualTo(UPDATED_SHOP_DESC);
        assertThat(testFriendyShop.getShopPosition()).isEqualTo(UPDATED_SHOP_POSITION);
        assertThat(testFriendyShop.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testFriendyShop.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingFriendyShop() throws Exception {
        int databaseSizeBeforeUpdate = friendyShopRepository.findAll().size();

        // Create the FriendyShop

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFriendyShopMockMvc.perform(put("/api/friendy-shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(friendyShop)))
            .andExpect(status().isCreated());

        // Validate the FriendyShop in the database
        List<FriendyShop> friendyShopList = friendyShopRepository.findAll();
        assertThat(friendyShopList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFriendyShop() throws Exception {
        // Initialize the database
        friendyShopService.save(friendyShop);

        int databaseSizeBeforeDelete = friendyShopRepository.findAll().size();

        // Get the friendyShop
        restFriendyShopMockMvc.perform(delete("/api/friendy-shops/{id}", friendyShop.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FriendyShop> friendyShopList = friendyShopRepository.findAll();
        assertThat(friendyShopList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FriendyShop.class);
        FriendyShop friendyShop1 = new FriendyShop();
        friendyShop1.setId(1L);
        FriendyShop friendyShop2 = new FriendyShop();
        friendyShop2.setId(friendyShop1.getId());
        assertThat(friendyShop1).isEqualTo(friendyShop2);
        friendyShop2.setId(2L);
        assertThat(friendyShop1).isNotEqualTo(friendyShop2);
        friendyShop1.setId(null);
        assertThat(friendyShop1).isNotEqualTo(friendyShop2);
    }
}
