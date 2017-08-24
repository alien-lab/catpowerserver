package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.WechatShopCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;




/**
 * Spring Data JPA repository for the WechatShopCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WechatShopCardRepository extends JpaRepository<WechatShopCard,Long> {
    @Query("select a from WechatShopCard a where a.title like CONCAT('%',?1,'%')")
    Page<WechatShopCard> findByCondition(String filter, Pageable page);

    WechatShopCard findByCardId(String cardid);

}
