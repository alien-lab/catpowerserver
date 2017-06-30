package com.alienlab.catpower.web.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.alienlab.catpower.web.wechat.service.WechatMessageService;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * Created by 橘 on 2017/5/14.
 */
@Service
public class WechatMessageServiceImpl implements WechatMessageService {
    @Autowired
    WechatUtil wechatUtil;
    @Value("${wechat.host.basepath}")
    private String wechathost;



}
