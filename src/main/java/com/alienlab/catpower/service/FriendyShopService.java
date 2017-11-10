package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.FriendyShop;
import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing FriendyShop.
 */
public interface FriendyShopService {

    /**
     * Save a friendyShop.
     *
     * @param friendyShop the entity to save
     * @return the persisted entity
     */
    FriendyShop save(FriendyShop friendyShop);

    /**
     *  Get all the friendyShops.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FriendyShop> findAll(Pageable pageable);

    /**
     *  Get the "id" friendyShop.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FriendyShop findOne(Long id);

    /**
     *  Delete the "id" friendyShop.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    QrInfo getShopBindQr(FriendyShop shop) throws Exception;
}
