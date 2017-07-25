package com.alienlab.catpower.web.wechat.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by 橘 on 2017/5/14.
 */
public interface WechatMessageService {
    void sendEvalCoachMsg(Long scheId) throws Exception;

}
