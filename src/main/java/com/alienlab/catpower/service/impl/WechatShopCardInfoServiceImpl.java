package com.alienlab.catpower.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.catpower.domain.*;
import com.alienlab.catpower.service.*;
import com.alienlab.catpower.repository.WechatShopCardInfoRepository;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import com.alienlab.catpower.web.wechat.service.WechatUserService;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.ZonedDateTime;


/**
 * Service Implementation for managing WechatShopCardInfo.
 */
@Service
@Transactional
public class WechatShopCardInfoServiceImpl implements WechatShopCardInfoService{

    private final Logger log = LoggerFactory.getLogger(WechatShopCardInfoServiceImpl.class);

    private final WechatShopCardInfoRepository wechatShopCardInfoRepository;

    @Autowired
    LearnerService learnerService;
    @Autowired
    WechatUserService wechatUserService;
    @Autowired
    BuyCourseService buyCourseService;
    @Autowired
    WechatShopCardService wechatShopCardService;

    @Autowired
    WechatUtil wechatUtil;

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
    public WechatShopCardInfo userGetCard(String openid, String code, String outerStr, String cardId) throws Exception {
        WechatShopCard card=wechatShopCardService.findByCardId(cardId);
        if(card==null){
            log.debug("未找到cardId对应的卡券");
            throw new Exception("未找到cardId对应的卡券");
        }
        return userGetCard(openid,code,outerStr,card);
    }
    /**
     * 用户领取卡片
     * @param openid
     * @param code 用户卡片号
     * @param outerStr 领取来源
     * @param card 关联的卡券
     * @return
     */
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

        //领取卡片，自动创建学员信息
        try {
            Learner learner=learnerService.findByOpenid(openid);
            if(learner==null){
                log.debug("openid未绑定学员，使用微信信息自动绑定学员");
                WechatUser wechatUser=wechatUserService.findUserByOpenid(openid);
                if(wechatUser==null){
                    log.error("openid未关注服务号，获取不到用户信息！");
                    wechatUser=new WechatUser();
                    wechatUser.setOpenId(openid);
                    wechatUser=wechatUserService.save(wechatUser);
                }
                learner=new Learner();
                learner.setFirstBuyclass(ZonedDateTime.now());
                learner.setLearneName(wechatUser.getNickName());
                learner.setRegistTime(ZonedDateTime.now());
                learner.setWechatUser(wechatUser);
                learner=learnerService.save(learner);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return cardInfo;
    }

    @Override
    public WechatShopCardInfo activeCard(String code) throws Exception {
        WechatShopCardInfo cardInfo=wechatShopCardInfoRepository.findByCardCode(code);
        if(cardInfo==null){
            log.error("卡片："+code+"未找到。");
            throw new Exception("卡片："+code+"未找到。");
        }
        //如果卡片关联课程，向领卡用户发放关联的课程
        WechatShopCard shopCard=cardInfo.getWechatShopCard();
        if(shopCard.getCourse()!=null){//如果卡券已设置关联课程
            Course course=shopCard.getCourse();
            Learner learner=learnerService.findByOpenid(cardInfo.getOpenid());
            if(learner!=null){
                BuyCourse buyCourse=new BuyCourse();
                buyCourse.setStatus("正常");
                buyCourse.setRemainClass((long)course.getTotalClassHour());
                buyCourse.setLearner(learner);
                buyCourse.setCourse(course);
                buyCourse.setBuyTime(ZonedDateTime.now());
                buyCourse.setOperateTime(ZonedDateTime.now());
                buyCourse.setPaymentWay("卡券核销");
                buyCourse.setPaymentAccount(0f);
                buyCourse=buyCourseService.save(buyCourse);
            }else{
                throw new Exception("未找到学员信息");
            }
        }
        cardInfo.setRechargeTime(ZonedDateTime.now());
        cardInfo=save(cardInfo);
        return cardInfo;
    }
}
