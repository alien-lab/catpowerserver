package com.alienlab.catpower.service;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.catpower.domain.Course;
import com.alienlab.catpower.domain.WechatShopCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing WechatShopCard.
 */
public interface WechatShopCardService {

    /**
     * Save a wechatShopCard.
     *
     * @param wechatShopCard the entity to save
     * @return the persisted entity
     */
    WechatShopCard save(WechatShopCard wechatShopCard);

    /**
     *  Get all the wechatShopCards.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WechatShopCard> findAll(Pageable pageable);

    /**
     *  Get the "id" wechatShopCard.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WechatShopCard findOne(Long id);

    /**
     *  Delete the "id" wechatShopCard.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    WechatShopCard findByCardId(String cardid);

    WechatShopCard JsonToShopCard(JSONObject cardJson);

    WechatShopCard setShopCardCourse(String cardId,Long courseId) throws Exception;

    WechatShopCard setShopCardCourse(WechatShopCard card,Course course);

    boolean refreshShopCard();
}
