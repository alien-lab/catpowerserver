package com.alienlab.catpower.web.wechat.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by 橘 on 2017/5/14.
 */
public interface WechatMessageService {
    //教练下课时，教练评价的消息推送
    void sendEvalCoachMsg(Long scheId) throws Exception;

    //下课时，教练给学员建议的消息推送
    void sendOverClassMsg(Long scheId) throws Exception;

}
