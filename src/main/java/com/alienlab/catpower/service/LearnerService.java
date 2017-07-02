package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * Service Interface for managing Learner.
 */
public interface LearnerService {

    /**
     * Save a learner.
     *
     * @param learner the entity to save
     * @return the persisted entity
     */
    Learner save(Learner learner);

    /**
     *  Get all the learners.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Learner> findAll(Pageable pageable);

    /**
     *  Get the "id" learner.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Learner findOne(Long id);

    /**
     *  Delete the "id" learner.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    Map learnCountStatiscByDate(Date date) throws ParseException;

    Learner findByOpenid(String openid) throws ParseException;

    QrInfo getLearnerBindQr(String openid) throws Exception;
    QrInfo getLearnerBindQr(Learner learner) throws Exception;

    //绑定学员微信
    Learner bindWechatUser(String openid,Long learnerId) throws Exception;
    Learner bindWechatUser(WechatUser wechatUser,Learner learner) throws Exception;

}
