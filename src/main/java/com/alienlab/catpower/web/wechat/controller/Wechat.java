package com.alienlab.catpower.web.wechat.controller;

import com.alienlab.catpower.web.rest.util.ExecResult;
import com.alienlab.catpower.web.wechat.bean.AccessToken;
import com.alienlab.catpower.web.wechat.bean.MessageResponse;
import com.alienlab.catpower.web.wechat.service.ResponseService;
import com.alienlab.catpower.web.wechat.util.SignUtil;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 橘 on 2016/12/23.
 */
@RestController
@RequestMapping("/wechat")
public class Wechat {
    private static final Logger logger = LoggerFactory.getLogger("Wechat");

    @Autowired
    SignUtil signUtil;
    @Autowired
    ResponseService responseService;
    @Autowired
    WechatUtil wechatUtil;
    @RequestMapping(value="",method=RequestMethod.GET)
    public String validateRequest(@RequestParam String signature,@RequestParam String timestamp,@RequestParam String nonce,@RequestParam String echostr){
        if (signUtil.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }else{
            return "error";
        }
    }

    //有事情发生用post
    @RequestMapping(value="",method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    MessageResponse doMessageResponse(@RequestBody String body){
        logger.debug("get message from wechat:"+body);
        return responseService.doResponse(body);
    }

    @Value("${wechat.host.allowed}")
    private String allowed;

    @RequestMapping(value="/token",method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getWechatToken(HttpServletRequest request){
        String host=request.getRemoteHost();
        System.out.println(host);
        if(allowed.indexOf(host)>=0){
            AccessToken at=wechatUtil.getAccessToken();
            return ResponseEntity.ok().body(at);
        }else{
            ExecResult er=new ExecResult(false,"主机"+host+" 不被允许.");
            return ResponseEntity.status(500).body(er);
        }

    }
}
