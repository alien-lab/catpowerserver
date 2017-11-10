package com.alienlab.catpower.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.catpower.domain.QrScanLog;
import com.alienlab.catpower.service.QrScanLogService;
import com.alienlab.catpower.web.rest.util.HeaderUtil;
import com.alienlab.catpower.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing QrScanLog.
 */
@RestController
@RequestMapping("/api")
public class QrScanLogResource {

    private final Logger log = LoggerFactory.getLogger(QrScanLogResource.class);

    private static final String ENTITY_NAME = "qrScanLog";

    private final QrScanLogService qrScanLogService;

    public QrScanLogResource(QrScanLogService qrScanLogService) {
        this.qrScanLogService = qrScanLogService;
    }

    /**
     * POST  /qr-scan-logs : Create a new qrScanLog.
     *
     * @param qrScanLog the qrScanLog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new qrScanLog, or with status 400 (Bad Request) if the qrScanLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/qr-scan-logs")
    @Timed
    public ResponseEntity<QrScanLog> createQrScanLog(@RequestBody QrScanLog qrScanLog) throws URISyntaxException {
        log.debug("REST request to save QrScanLog : {}", qrScanLog);
        if (qrScanLog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new qrScanLog cannot already have an ID")).body(null);
        }
        QrScanLog result = qrScanLogService.save(qrScanLog);
        return ResponseEntity.created(new URI("/api/qr-scan-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /qr-scan-logs : Updates an existing qrScanLog.
     *
     * @param qrScanLog the qrScanLog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated qrScanLog,
     * or with status 400 (Bad Request) if the qrScanLog is not valid,
     * or with status 500 (Internal Server Error) if the qrScanLog couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/qr-scan-logs")
    @Timed
    public ResponseEntity<QrScanLog> updateQrScanLog(@RequestBody QrScanLog qrScanLog) throws URISyntaxException {
        log.debug("REST request to update QrScanLog : {}", qrScanLog);
        if (qrScanLog.getId() == null) {
            return createQrScanLog(qrScanLog);
        }
        QrScanLog result = qrScanLogService.save(qrScanLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, qrScanLog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /qr-scan-logs : get all the qrScanLogs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of qrScanLogs in body
     */
    @GetMapping("/qr-scan-logs")
    @Timed
    public ResponseEntity<List<QrScanLog>> getAllQrScanLogs(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of QrScanLogs");
        Page<QrScanLog> page = qrScanLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/qr-scan-logs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /qr-scan-logs/:id : get the "id" qrScanLog.
     *
     * @param id the id of the qrScanLog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the qrScanLog, or with status 404 (Not Found)
     */
    @GetMapping("/qr-scan-logs/{id}")
    @Timed
    public ResponseEntity<QrScanLog> getQrScanLog(@PathVariable Long id) {
        log.debug("REST request to get QrScanLog : {}", id);
        QrScanLog qrScanLog = qrScanLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(qrScanLog));
    }

    /**
     * DELETE  /qr-scan-logs/:id : delete the "id" qrScanLog.
     *
     * @param id the id of the qrScanLog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/qr-scan-logs/{id}")
    @Timed
    public ResponseEntity<Void> deleteQrScanLog(@PathVariable Long id) {
        log.debug("REST request to delete QrScanLog : {}", id);
        qrScanLogService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
