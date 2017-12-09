package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.domain.WechatGoodsList;
import com.alienlab.catpower.service.WechatGoodsListService;
import com.alienlab.catpower.service.WechatOrderService;
import com.alienlab.catpower.domain.WechatOrder;
import com.alienlab.catpower.repository.WechatOrderRepository;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import com.alienlab.catpower.web.wechat.service.WechatUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import sun.reflect.annotation.ExceptionProxy;

import java.util.List;

/**
 * Service Implementation for managing WechatOrder.
 */
@Service
@Transactional
public class WechatOrderServiceImpl implements WechatOrderService{

    private final Logger log = LoggerFactory.getLogger(WechatOrderServiceImpl.class);

    private final WechatOrderRepository wechatOrderRepository;
    @Autowired
    WechatUserService wechatUserService;

    @Autowired
    WechatGoodsListService wechatGoodsListService;

    public WechatOrderServiceImpl(WechatOrderRepository wechatOrderRepository) {
        this.wechatOrderRepository = wechatOrderRepository;
    }

    /**
     * Save a wechatOrder.
     *
     * @param wechatOrder the entity to save
     * @return the persisted entity
     */
    @Override
    public WechatOrder save(WechatOrder wechatOrder) {
        log.debug("Request to save WechatOrder : {}", wechatOrder);
        WechatOrder result = wechatOrderRepository.save(wechatOrder);
        return result;
    }

    /**
     *  Get all the wechatOrders.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WechatOrder> findAll(Pageable pageable) {
        log.debug("Request to get all WechatOrders");
        Page<WechatOrder> result = wechatOrderRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one wechatOrder by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WechatOrder findOne(Long id) {
        log.debug("Request to get WechatOrder : {}", id);
        WechatOrder wechatOrder = wechatOrderRepository.findOne(id);
        return wechatOrder;
    }

    /**
     *  Delete the  wechatOrder by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WechatOrder : {}", id);
        wechatOrderRepository.delete(id);
    }

    @Override
    public List<WechatOrder> findBuyRecordsByOpenid(Long goodsId,String openid) throws Exception {
        WechatUser wechatUser=wechatUserService.findUserByOpenid(openid);
        if(wechatUser==null){
            throw new Exception("未找到微信用户.");
        }
        WechatGoodsList goods=wechatGoodsListService.findOne(goodsId);
        if(goods==null){
            throw new Exception("未找到售卖商品.");
        }
        return wechatOrderRepository.findWechatOrderByWechatUserAAndWechatGoodsListAndOrderStatus(wechatUser,goods,"已支付");
    }
}
