package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.WechatGoodsList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing WechatGoodsList.
 */
public interface WechatGoodsListService {

    /**
     * Save a wechatGoodsList.
     *
     * @param wechatGoodsList the entity to save
     * @return the persisted entity
     */
    WechatGoodsList save(WechatGoodsList wechatGoodsList);

    /**
     *  Get all the wechatGoodsLists.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WechatGoodsList> findAll(Pageable pageable);

    /**
     *  Get the "id" wechatGoodsList.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WechatGoodsList findOne(Long id);

    /**
     *  Delete the "id" wechatGoodsList.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
