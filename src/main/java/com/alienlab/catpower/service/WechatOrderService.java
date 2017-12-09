package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.WechatOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing WechatOrder.
 */
public interface WechatOrderService {

    /**
     * Save a wechatOrder.
     *
     * @param wechatOrder the entity to save
     * @return the persisted entity
     */
    WechatOrder save(WechatOrder wechatOrder);

    /**
     *  Get all the wechatOrders.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WechatOrder> findAll(Pageable pageable);

    /**
     *  Get the "id" wechatOrder.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WechatOrder findOne(Long id);

    /**
     *  Delete the "id" wechatOrder.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<WechatOrder> findBuyRecordsByOpenid(Long goodsId,String openid) throws Exception;
}
