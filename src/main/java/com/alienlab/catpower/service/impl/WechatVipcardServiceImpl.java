package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.service.WechatVipcardService;
import com.alienlab.catpower.domain.WechatVipcard;
import com.alienlab.catpower.repository.WechatVipcardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing WechatVipcard.
 */
@Service
@Transactional
public class WechatVipcardServiceImpl implements WechatVipcardService{

    private final Logger log = LoggerFactory.getLogger(WechatVipcardServiceImpl.class);

    private final WechatVipcardRepository wechatVipcardRepository;

    public WechatVipcardServiceImpl(WechatVipcardRepository wechatVipcardRepository) {
        this.wechatVipcardRepository = wechatVipcardRepository;
    }

    /**
     * Save a wechatVipcard.
     *
     * @param wechatVipcard the entity to save
     * @return the persisted entity
     */
    @Override
    public WechatVipcard save(WechatVipcard wechatVipcard) {
        log.debug("Request to save WechatVipcard : {}", wechatVipcard);
        return wechatVipcardRepository.save(wechatVipcard);
    }

    /**
     *  Get all the wechatVipcards.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WechatVipcard> findAll(Pageable pageable) {
        log.debug("Request to get all WechatVipcards");
        return wechatVipcardRepository.findAll(pageable);
    }

    /**
     *  Get one wechatVipcard by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WechatVipcard findOne(Long id) {
        log.debug("Request to get WechatVipcard : {}", id);
        return wechatVipcardRepository.findOne(id);
    }

    /**
     *  Delete the  wechatVipcard by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WechatVipcard : {}", id);
        wechatVipcardRepository.delete(id);
    }
}
