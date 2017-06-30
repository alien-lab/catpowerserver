package com.alienlab.catpower.web.wechat.service.impl;

import com.alienlab.catpower.web.wechat.bean.entity.WechatMessageLog;
import com.alienlab.catpower.web.wechat.repository.WechatMessageLogRepository;
import com.alienlab.catpower.web.wechat.service.WechatMessageLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Service Implementation for managing WechatMessageLog.
 */
@Service
public class WechatMessageLogServiceImpl implements WechatMessageLogService {

    private final Logger log = LoggerFactory.getLogger(WechatMessageLogServiceImpl.class);

    @Autowired
    private WechatMessageLogRepository wechatMessageLogRepository;

    /**
     * Save a wechatMessageLog.
     *
     * @param wechatMessageLog the entity to save
     * @return the persisted entity
     */
    public WechatMessageLog save(WechatMessageLog wechatMessageLog) {
        log.debug("Request to save WechatMessageLog : {}", wechatMessageLog);
        WechatMessageLog result = wechatMessageLogRepository.save(wechatMessageLog);
        return result;
    }

    /**
     *  Get all the wechatMessageLogs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<WechatMessageLog> findAll(Pageable pageable) {
        log.debug("Request to get all WechatMessageLogs");
        Page<WechatMessageLog> result = wechatMessageLogRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one wechatMessageLog by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public WechatMessageLog findOne(Long id) {
        log.debug("Request to get WechatMessageLog : {}", id);
        WechatMessageLog wechatMessageLog = wechatMessageLogRepository.findOne(id);
        return wechatMessageLog;
    }

    /**
     *  Delete the  wechatMessageLog by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WechatMessageLog : {}", id);
        wechatMessageLogRepository.delete(id);
    }
}
