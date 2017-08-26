package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.WechatVipcard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing WechatVipcard.
 */
public interface WechatVipcardService {

    /**
     * Save a wechatVipcard.
     *
     * @param wechatVipcard the entity to save
     * @return the persisted entity
     */
    WechatVipcard save(WechatVipcard wechatVipcard);

    /**
     *  Get all the wechatVipcards.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WechatVipcard> findAll(Pageable pageable);

    /**
     *  Get the "id" wechatVipcard.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WechatVipcard findOne(Long id);

    /**
     *  Delete the "id" wechatVipcard.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    WechatVipcard activeVipCard(String code) throws Exception;
}
