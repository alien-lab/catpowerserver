package com.alienlab.catpower.web.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.catpower.domain.BuyCourse;

/**
 * Created by 橘 on 2017/5/14.
 */
public interface WechatMessageService {
    //教练下课时，教练评价的消息推送
    void sendEvalCoachMsg(Long scheId) throws Exception;

    //下课时，教练给学员建议的消息推送
    void sendOverClassMsg(Long scheId) throws Exception;

    //购买成功推送给学员的购买成功消息
    void sendBuyClassSuccess(BuyCourse buyCourse) throws Exception;

}
