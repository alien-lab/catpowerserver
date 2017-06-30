package com.alienlab.catpower.web.wechat.repository;

import com.alienlab.catpower.web.wechat.bean.entity.QrType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QrType entity.
 */
@Repository
public interface QrTypeRepository extends JpaRepository<QrType,Long> {}
