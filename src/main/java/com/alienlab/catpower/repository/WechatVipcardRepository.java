package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.WechatVipcard;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WechatVipcard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WechatVipcardRepository extends JpaRepository<WechatVipcard,Long> {
    
}
