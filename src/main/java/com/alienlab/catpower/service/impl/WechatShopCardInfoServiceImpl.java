package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.domain.WechatShopCard;
import com.alienlab.catpower.service.WechatShopCardInfoService;
import com.alienlab.catpower.domain.WechatShopCardInfo;
import com.alienlab.catpower.repository.WechatShopCardInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;


/**
 * Service Implementation for managing WechatShopCardInfo.
 */
@Service
@Transactional
public class WechatShopCardInfoServiceImpl implements WechatShopCardInfoService{

    private final Logger log = LoggerFactory.getLogger(WechatShopCardInfoServiceImpl.class);

    private final WechatShopCardInfoRepository wechatShopCardInfoRepository;

    public WechatShopCardInfoServiceImpl(WechatShopCardInfoRepository wechatShopCardInfoRepository) {
        this.wechatShopCardInfoRepository = wechatShopCardInfoRepository;
    }

    /**
     * Save a wechatShopCardInfo.
     *
     * @param wechatShopCardInfo the entity to save
     * @return the persisted entity
     */
    @Override
    public WechatShopCardInfo save(WechatShopCardInfo wechatShopCardInfo) {
        log.debug("Request to save WechatShopCardInfo : {}", wechatShopCardInfo);
        return wechatShopCardInfoRepository.save(wechatShopCardInfo);
    }

    /**
     *  Get all the wechatShopCardInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WechatShopCardInfo> findAll(Pageable pageable) {
        log.debug("Request to get all WechatShopCardInfos");
        return wechatShopCardInfoRepository.findAll(pageable);
    }

    /**
     *  Get one wechatShopCardInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WechatShopCardInfo findOne(Long id) {
        log.debug("Request to get WechatShopCardInfo : {}", id);
        return wechatShopCardInfoRepository.findOne(id);
    }

    /**
     *  Delete the  wechatShopCardInfo by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WechatShopCardInfo : {}", id);
        wechatShopCardInfoRepository.delete(id);
    }

    @Override
    public WechatShopCardInfo userGetCard(String openid, String code, String outerStr, WechatShopCard card) {
        WechatShopCardInfo cardInfo=new WechatShopCardInfo();
        cardInfo.setCardCode(code);
        cardInfo.setCardId(card.getCardId());
        cardInfo.setGetTime(ZonedDateTime.now());
        cardInfo.setOpenid(openid);
        cardInfo.setOutStr(outerStr);
        cardInfo.setWechatShopCard(card);
        cardInfo=save(cardInfo);
        return cardInfo;
    }
}
