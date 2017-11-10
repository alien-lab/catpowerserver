package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.service.QrScanLogService;
import com.alienlab.catpower.domain.QrScanLog;
import com.alienlab.catpower.repository.QrScanLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing QrScanLog.
 */
@Service
@Transactional
public class QrScanLogServiceImpl implements QrScanLogService{

    private final Logger log = LoggerFactory.getLogger(QrScanLogServiceImpl.class);

    private final QrScanLogRepository qrScanLogRepository;

    public QrScanLogServiceImpl(QrScanLogRepository qrScanLogRepository) {
        this.qrScanLogRepository = qrScanLogRepository;
    }

    /**
     * Save a qrScanLog.
     *
     * @param qrScanLog the entity to save
     * @return the persisted entity
     */
    @Override
    public QrScanLog save(QrScanLog qrScanLog) {
        log.debug("Request to save QrScanLog : {}", qrScanLog);
        return qrScanLogRepository.save(qrScanLog);
    }

    /**
     *  Get all the qrScanLogs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QrScanLog> findAll(Pageable pageable) {
        log.debug("Request to get all QrScanLogs");
        return qrScanLogRepository.findAll(pageable);
    }

    /**
     *  Get one qrScanLog by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public QrScanLog findOne(Long id) {
        log.debug("Request to get QrScanLog : {}", id);
        return qrScanLogRepository.findOne(id);
    }

    /**
     *  Delete the  qrScanLog by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete QrScanLog : {}", id);
        qrScanLogRepository.delete(id);
    }
}
