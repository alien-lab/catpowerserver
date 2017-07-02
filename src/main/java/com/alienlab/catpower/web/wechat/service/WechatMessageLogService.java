package com.alienlab.catpower.web.wechat.service;

import com.alienlab.catpower.web.wechat.bean.entity.WechatMessageLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing WechatMessageLog.
 */
public interface WechatMessageLogService {

    /**
     * Save a wechatMessageLog.
     *
     * @param wechatMessageLog the entity to save
     * @return the persisted entity
     */
    WechatMessageLog save(WechatMessageLog wechatMessageLog);

    /**
     *  Get all the wechatMessageLogs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WechatMessageLog> findAll(Pageable pageable);

    /**
     *  Get the "id" wechatMessageLog.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WechatMessageLog findOne(Long id);

    /**
     *  Delete the "id" wechatMessageLog.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
