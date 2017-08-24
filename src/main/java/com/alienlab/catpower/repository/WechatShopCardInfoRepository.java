package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.WechatShopCardInfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WechatShopCardInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WechatShopCardInfoRepository extends JpaRepository<WechatShopCardInfo,Long> {
    
}
