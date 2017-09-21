package com.alienlab.catpower.service;

import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Service Interface for managing Coach.
 */
public interface CoachService {

    /**
     * Save a coach.
     * 加入一个教练
     * @param coach the entity to save
     * @return the persisted entity
     */
    Coach save(Coach coach);

    /**
     *  Get all the coaches.
     *  得到所有的教练
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Coach> findAll(Pageable pageable);

    /**
     *  Get the "id" coach.
     *  根据id获取对应的教练
     *  @param id the id of the entity
     *  @return the entity
     */
    Coach findOne(Long id);

    /**
     *  Delete the "id" coach.
     *  根据id删除指定的教练
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *
     */
    Map getCoachByCoachId(Long id) throws Exception;
    //生成教练绑定二维码
    QrInfo getCoachBindQr(Coach coach)throws Exception;
    //判断教练身份是否已被绑定
    Coach bindWechatUser(WechatUser wechatUser, Coach coach)throws Exception;

    Coach bindWechatUser(String openid, Long coachId)throws Exception;

    Coach findCoachByOpenId(String openid) throws Exception;
}
