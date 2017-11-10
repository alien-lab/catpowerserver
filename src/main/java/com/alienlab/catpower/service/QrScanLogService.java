package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.QrScanLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing QrScanLog.
 */
public interface QrScanLogService {

    /**
     * Save a qrScanLog.
     *
     * @param qrScanLog the entity to save
     * @return the persisted entity
     */
    QrScanLog save(QrScanLog qrScanLog);

    /**
     *  Get all the qrScanLogs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<QrScanLog> findAll(Pageable pageable);

    /**
     *  Get the "id" qrScanLog.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    QrScanLog findOne(Long id);

    /**
     *  Delete the "id" qrScanLog.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
