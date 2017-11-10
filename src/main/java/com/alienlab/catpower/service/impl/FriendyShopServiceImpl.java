package com.alienlab.catpower.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.service.FriendyShopService;
import com.alienlab.catpower.domain.FriendyShop;
import com.alienlab.catpower.repository.FriendyShopRepository;
import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;
import com.alienlab.catpower.web.wechat.service.QrInfoService;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing FriendyShop.
 */
@Service
@Transactional
public class FriendyShopServiceImpl implements FriendyShopService{

    private final Logger log = LoggerFactory.getLogger(FriendyShopServiceImpl.class);

    private final FriendyShopRepository friendyShopRepository;

    @Autowired
    WechatUtil wechatUtil;

    @Autowired
    QrInfoService qrInfoService;

    public FriendyShopServiceImpl(FriendyShopRepository friendyShopRepository) {
        this.friendyShopRepository = friendyShopRepository;
    }

    /**
     * Save a friendyShop.
     *
     * @param friendyShop the entity to save
     * @return the persisted entity
     */
    @Override
    public FriendyShop save(FriendyShop friendyShop) {
        log.debug("Request to save FriendyShop : {}", friendyShop);
        return friendyShopRepository.save(friendyShop);
    }

    /**
     *  Get all the friendyShops.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FriendyShop> findAll(Pageable pageable) {
        log.debug("Request to get all FriendyShops");
        return friendyShopRepository.findAll(pageable);
    }

    /**
     *  Get one friendyShop by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FriendyShop findOne(Long id) {
        log.debug("Request to get FriendyShop : {}", id);
        return friendyShopRepository.findOne(id);
    }

    /**
     *  Delete the  friendyShop by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FriendyShop : {}", id);
        friendyShopRepository.delete(id);
    }

    @Override
    public QrInfo getShopBindQr(FriendyShop shop) throws Exception {
        if(shop==null){
            throw new Exception("未找到合作店铺信息。");
        }
        if(shop.getQrInfo()!=null){
            return shop.getQrInfo();
        }
        //如果当前员工
        String sceneId = "18and"+shop.getId()+"";
        JSONObject jo =  wechatUtil.get_qr_code_ticket(sceneId);
        if(jo==null || jo.getString("ticket")==null){
            throw new Exception("生成签到二维码失败！合作店铺编码："+shop.getId());
        }
        QrInfo qr=qrInfoService.createQrinfo(sceneId, 18L,jo.getString("ticket"));
        shop.setQrInfo(qr);
        friendyShopRepository.save(shop);
        System.out.println("*****************************************************************");
        System.out.println(qr);
        System.out.println(shop);

        return qr;
    }
}
