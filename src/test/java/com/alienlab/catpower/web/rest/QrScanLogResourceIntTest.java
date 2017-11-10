package com.alienlab.catpower.web.rest;

import com.alienlab.catpower.CatpowerserverApp;

import com.alienlab.catpower.domain.QrScanLog;
import com.alienlab.catpower.repository.QrScanLogRepository;
import com.alienlab.catpower.service.QrScanLogService;
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
 * Test class for the QrScanLogResource REST controller.
 *
 * @see QrScanLogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatpowerserverApp.class)
public class QrScanLogResourceIntTest {

    private static final ZonedDateTime DEFAULT_SCAN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SCAN_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_OPENID = "AAAAAAAAAA";
    private static final String UPDATED_OPENID = "BBBBBBBBBB";

    private static final String DEFAULT_QRKEY = "AAAAAAAAAA";
    private static final String UPDATED_QRKEY = "BBBBBBBBBB";

    private static final String DEFAULT_REPLY = "AAAAAAAAAA";
    private static final String UPDATED_REPLY = "BBBBBBBBBB";

    @Autowired
    private QrScanLogRepository qrScanLogRepository;

    @Autowired
    private QrScanLogService qrScanLogService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restQrScanLogMockMvc;

    private QrScanLog qrScanLog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QrScanLogResource qrScanLogResource = new QrScanLogResource(qrScanLogService);
        this.restQrScanLogMockMvc = MockMvcBuilders.standaloneSetup(qrScanLogResource)
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
    public static QrScanLog createEntity(EntityManager em) {
        QrScanLog qrScanLog = new QrScanLog()
            .scanTime(DEFAULT_SCAN_TIME)
            .openid(DEFAULT_OPENID)
            .qrkey(DEFAULT_QRKEY)
            .reply(DEFAULT_REPLY);
        return qrScanLog;
    }

    @Before
    public void initTest() {
        qrScanLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createQrScanLog() throws Exception {
        int databaseSizeBeforeCreate = qrScanLogRepository.findAll().size();

        // Create the QrScanLog
        restQrScanLogMockMvc.perform(post("/api/qr-scan-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qrScanLog)))
            .andExpect(status().isCreated());

        // Validate the QrScanLog in the database
        List<QrScanLog> qrScanLogList = qrScanLogRepository.findAll();
        assertThat(qrScanLogList).hasSize(databaseSizeBeforeCreate + 1);
        QrScanLog testQrScanLog = qrScanLogList.get(qrScanLogList.size() - 1);
        assertThat(testQrScanLog.getScanTime()).isEqualTo(DEFAULT_SCAN_TIME);
        assertThat(testQrScanLog.getOpenid()).isEqualTo(DEFAULT_OPENID);
        assertThat(testQrScanLog.getQrkey()).isEqualTo(DEFAULT_QRKEY);
        assertThat(testQrScanLog.getReply()).isEqualTo(DEFAULT_REPLY);
    }

    @Test
    @Transactional
    public void createQrScanLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = qrScanLogRepository.findAll().size();

        // Create the QrScanLog with an existing ID
        qrScanLog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQrScanLogMockMvc.perform(post("/api/qr-scan-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qrScanLog)))
            .andExpect(status().isBadRequest());

        // Validate the QrScanLog in the database
        List<QrScanLog> qrScanLogList = qrScanLogRepository.findAll();
        assertThat(qrScanLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllQrScanLogs() throws Exception {
        // Initialize the database
        qrScanLogRepository.saveAndFlush(qrScanLog);

        // Get all the qrScanLogList
        restQrScanLogMockMvc.perform(get("/api/qr-scan-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qrScanLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].scanTime").value(hasItem(sameInstant(DEFAULT_SCAN_TIME))))
            .andExpect(jsonPath("$.[*].openid").value(hasItem(DEFAULT_OPENID.toString())))
            .andExpect(jsonPath("$.[*].qrkey").value(hasItem(DEFAULT_QRKEY.toString())))
            .andExpect(jsonPath("$.[*].reply").value(hasItem(DEFAULT_REPLY.toString())));
    }

    @Test
    @Transactional
    public void getQrScanLog() throws Exception {
        // Initialize the database
        qrScanLogRepository.saveAndFlush(qrScanLog);

        // Get the qrScanLog
        restQrScanLogMockMvc.perform(get("/api/qr-scan-logs/{id}", qrScanLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(qrScanLog.getId().intValue()))
            .andExpect(jsonPath("$.scanTime").value(sameInstant(DEFAULT_SCAN_TIME)))
            .andExpect(jsonPath("$.openid").value(DEFAULT_OPENID.toString()))
            .andExpect(jsonPath("$.qrkey").value(DEFAULT_QRKEY.toString()))
            .andExpect(jsonPath("$.reply").value(DEFAULT_REPLY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQrScanLog() throws Exception {
        // Get the qrScanLog
        restQrScanLogMockMvc.perform(get("/api/qr-scan-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQrScanLog() throws Exception {
        // Initialize the database
        qrScanLogService.save(qrScanLog);

        int databaseSizeBeforeUpdate = qrScanLogRepository.findAll().size();

        // Update the qrScanLog
        QrScanLog updatedQrScanLog = qrScanLogRepository.findOne(qrScanLog.getId());
        updatedQrScanLog
            .scanTime(UPDATED_SCAN_TIME)
            .openid(UPDATED_OPENID)
            .qrkey(UPDATED_QRKEY)
            .reply(UPDATED_REPLY);

        restQrScanLogMockMvc.perform(put("/api/qr-scan-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQrScanLog)))
            .andExpect(status().isOk());

        // Validate the QrScanLog in the database
        List<QrScanLog> qrScanLogList = qrScanLogRepository.findAll();
        assertThat(qrScanLogList).hasSize(databaseSizeBeforeUpdate);
        QrScanLog testQrScanLog = qrScanLogList.get(qrScanLogList.size() - 1);
        assertThat(testQrScanLog.getScanTime()).isEqualTo(UPDATED_SCAN_TIME);
        assertThat(testQrScanLog.getOpenid()).isEqualTo(UPDATED_OPENID);
        assertThat(testQrScanLog.getQrkey()).isEqualTo(UPDATED_QRKEY);
        assertThat(testQrScanLog.getReply()).isEqualTo(UPDATED_REPLY);
    }

    @Test
    @Transactional
    public void updateNonExistingQrScanLog() throws Exception {
        int databaseSizeBeforeUpdate = qrScanLogRepository.findAll().size();

        // Create the QrScanLog

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQrScanLogMockMvc.perform(put("/api/qr-scan-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qrScanLog)))
            .andExpect(status().isCreated());

        // Validate the QrScanLog in the database
        List<QrScanLog> qrScanLogList = qrScanLogRepository.findAll();
        assertThat(qrScanLogList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQrScanLog() throws Exception {
        // Initialize the database
        qrScanLogService.save(qrScanLog);

        int databaseSizeBeforeDelete = qrScanLogRepository.findAll().size();

        // Get the qrScanLog
        restQrScanLogMockMvc.perform(delete("/api/qr-scan-logs/{id}", qrScanLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<QrScanLog> qrScanLogList = qrScanLogRepository.findAll();
        assertThat(qrScanLogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QrScanLog.class);
        QrScanLog qrScanLog1 = new QrScanLog();
        qrScanLog1.setId(1L);
        QrScanLog qrScanLog2 = new QrScanLog();
        qrScanLog2.setId(qrScanLog1.getId());
        assertThat(qrScanLog1).isEqualTo(qrScanLog2);
        qrScanLog2.setId(2L);
        assertThat(qrScanLog1).isNotEqualTo(qrScanLog2);
        qrScanLog1.setId(null);
        assertThat(qrScanLog1).isNotEqualTo(qrScanLog2);
    }
}
