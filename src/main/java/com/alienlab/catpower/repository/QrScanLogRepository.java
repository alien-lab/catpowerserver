package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.QrScanLog;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the QrScanLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QrScanLogRepository extends JpaRepository<QrScanLog, Long> {

}
