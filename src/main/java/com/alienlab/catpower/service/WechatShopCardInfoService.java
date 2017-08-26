package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.WechatShopCard;
import com.alienlab.catpower.domain.WechatShopCardInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing WechatShopCardInfo.
 */
public interface WechatShopCardInfoService {

    /**
     * Save a wechatShopCardInfo.
     *
     * @param wechatShopCardInfo the entity to save
     * @return the persisted entity
     */
    WechatShopCardInfo save(WechatShopCardInfo wechatShopCardInfo);

    /**
     *  Get all the wechatShopCardInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WechatShopCardInfo> findAll(Pageable pageable);

    /**
     *  Get the "id" wechatShopCardInfo.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WechatShopCardInfo findOne(Long id);

    /**
     *  Delete the "id" wechatShopCardInfo.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    WechatShopCardInfo userGetCard(String openid, String code, String outerStr, String cardId) throws Exception;

    WechatShopCardInfo userGetCard(String openid, String code, String outerStr, WechatShopCard card);

    WechatShopCardInfo activeCard(String code) throws Exception;
}
