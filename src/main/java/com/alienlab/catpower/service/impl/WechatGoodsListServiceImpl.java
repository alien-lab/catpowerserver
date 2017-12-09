package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.service.WechatGoodsListService;
import com.alienlab.catpower.domain.WechatGoodsList;
import com.alienlab.catpower.repository.WechatGoodsListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing WechatGoodsList.
 */
@Service
@Transactional
public class WechatGoodsListServiceImpl implements WechatGoodsListService{

    private final Logger log = LoggerFactory.getLogger(WechatGoodsListServiceImpl.class);

    private final WechatGoodsListRepository wechatGoodsListRepository;

    public WechatGoodsListServiceImpl(WechatGoodsListRepository wechatGoodsListRepository) {
        this.wechatGoodsListRepository = wechatGoodsListRepository;
    }

    /**
     * Save a wechatGoodsList.
     *
     * @param wechatGoodsList the entity to save
     * @return the persisted entity
     */
    @Override
    public WechatGoodsList save(WechatGoodsList wechatGoodsList) {
        log.debug("Request to save WechatGoodsList : {}", wechatGoodsList);
        return wechatGoodsListRepository.save(wechatGoodsList);
    }

    /**
     *  Get all the wechatGoodsLists.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WechatGoodsList> findAll(Pageable pageable) {
        log.debug("Request to get all WechatGoodsLists");
        return wechatGoodsListRepository.findAll(pageable);
    }

    /**
     *  Get one wechatGoodsList by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WechatGoodsList findOne(Long id) {
        log.debug("Request to get WechatGoodsList : {}", id);
        return wechatGoodsListRepository.findOne(id);
    }

    /**
     *  Delete the  wechatGoodsList by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WechatGoodsList : {}", id);
        wechatGoodsListRepository.delete(id);
    }
}
