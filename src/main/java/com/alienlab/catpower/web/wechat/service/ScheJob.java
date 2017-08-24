package com.alienlab.catpower.web.wechat.service;

import com.alienlab.catpower.service.WechatShopCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by juhui on 2017/8/25.
 */
@Component
public class ScheJob {

    @Autowired
    WechatShopCardService wechatShopCardService;

    @Scheduled(cron="00 00 * * * ?")
    public void refreshShopCard(){
        wechatShopCardService.refreshShopCard();
    }
}
