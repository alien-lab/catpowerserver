package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.WechatGoodsList;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WechatGoodsList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WechatGoodsListRepository extends JpaRepository<WechatGoodsList, Long> {

}
