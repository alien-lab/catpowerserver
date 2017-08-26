package com.alienlab.catpower.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.catpower.domain.Course;
import com.alienlab.catpower.service.CourseService;
import com.alienlab.catpower.service.WechatShopCardService;
import com.alienlab.catpower.domain.WechatShopCard;
import com.alienlab.catpower.repository.WechatShopCardRepository;
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
 * Service Implementation for managing WechatShopCard.
 */
@Service
@Transactional
public class WechatShopCardServiceImpl implements WechatShopCardService{

    private final Logger log = LoggerFactory.getLogger(WechatShopCardServiceImpl.class);

    private final WechatShopCardRepository wechatShopCardRepository;
    private final WechatUtil wechatUtil;

    @Autowired
    CourseService courseService;

    public WechatShopCardServiceImpl(WechatShopCardRepository wechatShopCardRepository,WechatUtil wechatUtil) {
        this.wechatShopCardRepository = wechatShopCardRepository;
        this.wechatUtil=wechatUtil;
    }

    /**
     * Save a wechatShopCard.
     *
     * @param wechatShopCard the entity to save
     * @return the persisted entity
     */
    @Override
    public WechatShopCard save(WechatShopCard wechatShopCard) {
        log.debug("Request to save WechatShopCard : {}", wechatShopCard);
        return wechatShopCardRepository.save(wechatShopCard);
    }

    /**
     *  Get all the wechatShopCards.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WechatShopCard> findAll(Pageable pageable) {
        log.debug("Request to get all WechatShopCards");
        return wechatShopCardRepository.findAll(pageable);
    }

    /**
     *  Get one wechatShopCard by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WechatShopCard findOne(Long id) {
        log.debug("Request to get WechatShopCard : {}", id);
        return wechatShopCardRepository.findOne(id);
    }

    /**
     *  Delete the  wechatShopCard by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WechatShopCard : {}", id);
        wechatShopCardRepository.delete(id);
    }

    @Override
    public WechatShopCard findByCardId(String cardid) {
        WechatShopCard card=wechatShopCardRepository.findByCardId(cardid);
        JSONObject cardJson=wechatUtil.getShopCard(cardid);
        if(card==null){
            log.debug("Find by cardid null.Cardid is "+cardid);
            log.debug("get card from wechat...");

            cardJson.put("cardid",cardid);

            log.debug("get card from wechat,"+cardJson.toJSONString());

            card=JsonToShopCard(cardJson);
            card=wechatShopCardRepository.save(card);
            return card;
        }else{
            WechatShopCard tempCard=JsonToShopCard(cardJson);
            //更新剩余库存
            card.setCardRemainCount(tempCard.getCardRemainCount());
            card=wechatShopCardRepository.save(card);
            return card;
        }

    }

    @Override
    public WechatShopCard JsonToShopCard(JSONObject cardJson){
        JSONObject cardjo=cardJson.getJSONObject("card");
        String cardid=cardJson.getString("cardid");
        String cardType=cardjo.getString("card_type");
        JSONObject cardbody=cardjo.getJSONObject(cardType.toLowerCase());
        JSONObject baseInfo=cardbody.getJSONObject("base_info");
        String title=baseInfo.getString("title");
        String status=baseInfo.getString("status");

        JSONObject sku=baseInfo.getJSONObject("sku");
        Integer num=sku.getInteger("quantity");

        WechatShopCard card=new WechatShopCard();
        card.setCardJson(cardJson.toJSONString());
        card.setCardId(cardid);
        card.setCardRemainCount(num);
        card.setCardStatus(status);
        card.setCtTime(ZonedDateTime.now());
        card.setTitle(title);
        card.setCardType(cardType);
        return card;
    }

    @Override
    public WechatShopCard setShopCardCourse(String cardId, Long courseId) throws Exception {
        WechatShopCard card=wechatShopCardRepository.findByCardId(cardId);
        Course course=courseService.findOne(courseId);
        if(card!=null&&course!=null){
            return setShopCardCourse(card,course);
        }else{
            throw new Exception("卡券或课程不存在");
        }
    }

    @Override
    public WechatShopCard setShopCardCourse(WechatShopCard card, Course course) {
        card.setCourse(course);
        card=wechatShopCardRepository.save(card);
        return card;
    }

    @Override
    public boolean refreshShopCard() {
        int start=0;
        int size=10;
        int total=0;
        int end=10;
        do{
            JSONObject jo=wechatUtil.getShopCardIdList(start,size);
            String[] ids=jo.getObject("card_id_list",String[].class);
            total=jo.getInteger("total_num");
            start+=size;

            end=start+size;

            for (String id : ids) {
                findByCardId(id);
            }
        }while(end<total);
        return true;
    }


}
