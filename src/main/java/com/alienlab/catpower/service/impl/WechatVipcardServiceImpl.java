package com.alienlab.catpower.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.domain.WechatShopCardInfo;
import com.alienlab.catpower.repository.WechatShopCardInfoRepository;
import com.alienlab.catpower.service.LearnerService;
import com.alienlab.catpower.service.WechatShopCardInfoService;
import com.alienlab.catpower.service.WechatVipcardService;
import com.alienlab.catpower.domain.WechatVipcard;
import com.alienlab.catpower.repository.WechatVipcardRepository;
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

import java.time.ZonedDateTime;


/**
 * Service Implementation for managing WechatVipcard.
 */
@Service
@Transactional
public class WechatVipcardServiceImpl implements WechatVipcardService{

    private final Logger log = LoggerFactory.getLogger(WechatVipcardServiceImpl.class);

    private final WechatVipcardRepository wechatVipcardRepository;

    @Autowired
    WechatShopCardInfoRepository wechatShopCardInfoRepository;

    @Autowired
    LearnerService learnerService;

    @Autowired
    WechatUserService wechatUserService;

    @Autowired
    WechatUtil wechatUtil;

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

    @Override
    public WechatVipcard activeVipCard(String code) throws Exception {
        WechatShopCardInfo cardinfo=wechatShopCardInfoRepository.findByCardCode(code);
        if(cardinfo==null){
            throw new Exception("未领取卡片，卡号："+code);
        }
        WechatVipcard vipcard=new WechatVipcard();
        vipcard.setActiveTime(ZonedDateTime.now());
        vipcard.setCardCode(code);
        vipcard.setCardId(cardinfo.getCardId());
        vipcard.setGetTime(cardinfo.getGetTime());
        vipcard.setOpenid(cardinfo.getOpenid());
        vipcard=wechatVipcardRepository.save(vipcard);

        //根据卡片激活信息更新学员信息
        JSONObject vipinfo=wechatUtil.getVipCard(vipcard.getCardId(),vipcard.getCardCode());
        JSONObject cardUserInfo=vipinfo.getJSONObject("user_info");
        JSONArray properties=cardUserInfo.getJSONArray("common_field_list");
        String name="",phone="";
        for(int i=0;i<properties.size();i++){
            JSONObject jo=properties.getJSONObject(i);
            if(jo.containsKey("name")){
                if(jo.getString("name").equals("USER_FORM_INFO_FLAG_MOBILE")){
                    phone=jo.getString("value");
                }else if(jo.getString("name").equals("USER_FORM_INFO_FLAG_NAME")){
                    name=jo.getString("value");
                }
            }
        }
        Learner learner=learnerService.findByOpenid(vipcard.getOpenid());
        WechatUser wechatUser=wechatUserService.findUserByOpenid(vipcard.getOpenid());
        if(learner==null){
            learner=new Learner();

            //如果是新学员，绑定学员与wechatuser
            if(wechatUser!=null){
                learner.setWechatUser(wechatUser);
            }else{
                wechatUser=new WechatUser();
                wechatUser.setOpenId(vipcard.getOpenid());
                wechatUser=wechatUserService.save(wechatUser);
                learner.setWechatUser(wechatUser);
            }
        }
        learner.setLearneName(name);
        learner.setLearnerPhone(phone);

        learnerService.save(learner);


        return vipcard;
    }
}
