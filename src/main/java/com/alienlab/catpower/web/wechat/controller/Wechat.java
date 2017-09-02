package com.alienlab.catpower.web.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.alienlab.catpower.security.jwt.TokenProvider;
import com.alienlab.catpower.web.rest.util.ExecResult;
import com.alienlab.catpower.web.wechat.bean.AccessToken;
import com.alienlab.catpower.web.wechat.bean.MessageResponse;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import com.alienlab.catpower.web.wechat.service.ResponseService;
import com.alienlab.catpower.web.wechat.service.WechatService;
import com.alienlab.catpower.web.wechat.service.WechatUserService;
import com.alienlab.catpower.web.wechat.util.SignUtil;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by 橘 on 2016/12/23.
 */
@Controller
@RequestMapping("/wechat")
public class Wechat {
    private static final Logger logger = LoggerFactory.getLogger("Wechat");

    @Autowired
    SignUtil signUtil;
    @Autowired
    ResponseService responseService;
    @Autowired
    WechatUtil wechatUtil;
    @Autowired
    WechatUserService wechatUserService;
    @Autowired
    WechatService wechatService;
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    AuthenticationManager authenticationManager;


    @Value("${wechat.host.basepath}")
    private String basePath;
    @Value("${wechat.appid}")
    private String appid;


    @RequestMapping(value="",method=RequestMethod.GET)
    @ResponseBody
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

    @GetMapping(value="/snsapi")
    public String dispatch(@RequestParam String code,@RequestParam String router,@RequestParam(required = false) String state){
        logger.info("wechat dispatch>>code="+code+",router="+router);
        if(StringUtils.isEmpty(router))router="/";
        if(code==null){
            return "";
        }
        JSONObject user=wechatService.getUserInfo(code);
        if(user.containsKey("openid")){
            WechatUser wu=wechatUserService.findUserByOpenid(user.getString("openid"));
            if(wu==null){//新用户
                wu=new WechatUser();
            }
            wu.setOpenId(user.getString("openid"));
            wu.setNickName(user.getString("nickname"));
            wu.setIcon(user.getString("headimgurl"));
            wu=wechatUserService.save(wu);

            logger.info("get wechat user from wechatservice>>>"+ user.toJSONString());
            try {
                String u="http://"+basePath+"index.html";
                Map jsapiParams = wechatService.getJsApiTicket(u);
                logger.info("get jsapiparams>>>"+ JSON.toJSONString(jsapiParams));
                String timestamp= TypeUtils.castToString(jsapiParams.get("timestamp"));
                String nonce=TypeUtils.castToString(jsapiParams.get("nonceStr"));
                String signature=TypeUtils.castToString(jsapiParams.get("signature"));
                String openid=user.getString("openid");
                UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken("wechat", "wechat123!");
                Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
                String jwt = tokenProvider.createToken(authentication, true);
                String result="redirect:http://"+basePath+"index.html#!/"+router+"?" +
                    "token="+jwt+"&" +
                    "appid="+appid+"&" +
                    "timestamp="+timestamp+"&" +
                    "nonce="+nonce+"&" +
                    "signature="+signature+"&" +
                    "openid="+openid+"&state="+((state==null)?"":state);
                logger.info(result);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }
}
