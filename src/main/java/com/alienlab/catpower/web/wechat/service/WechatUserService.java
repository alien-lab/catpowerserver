package com.alienlab.catpower.web.wechat.service;

import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing WechatUser.
 */
public interface WechatUserService {

    /**
     * Save a wechatUser.
     *
     * @param wechatUser the entity to save
     * @return the persisted entity
     */
    WechatUser save(WechatUser wechatUser);

    /**
     *  Get all the wechatUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WechatUser> findAll(Pageable pageable);
    /**
     *  Get the "id" wechatUser.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WechatUser findOne(Long id);

    /**
     *  Delete the "id" wechatUser.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);


    WechatUser findUserByOpenid(String openid);
}
