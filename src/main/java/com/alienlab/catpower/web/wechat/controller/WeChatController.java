package com.alienlab.catpower.web.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.alienlab.catpower.web.rest.util.ExecResult;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import com.alienlab.catpower.web.wechat.service.WechatService;
import com.alienlab.catpower.web.wechat.service.WechatUserService;
import com.alienlab.catpower.web.wechat.util.SignUtil;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by admin on 2017-01-18.
 */
@RestController
@RequestMapping("/api")
public class WeChatController {
    @Autowired
    SignUtil signUtil;
    @Autowired
    WechatUtil wechatUtil;


    @Autowired
    WechatService wechatService;
    @Autowired
    WechatUserService wechatUserService;

    private static Logger logger = Logger.getLogger(WeChatController.class);



    @RequestMapping(value="/jsapi",method = RequestMethod.GET)
    public Map<String,String> getJsApiTicket(@RequestParam("url") String url){
        return wechatService.getJsApiTicket(url);
    }

    @RequestMapping(value="/getmediainfotest",method = RequestMethod.GET)
    public JSONObject getmediainfotest(@RequestParam("media_id") String media_id){
        return wechatService.getmediainfotest(media_id);
    }

    @RequestMapping(value="/getmediainfo",method = RequestMethod.GET)
    public JSONObject getmediainfo(@RequestParam("btn_id") String btn_id){
        return wechatService.getmediainfo(btn_id);
    }


    @RequestMapping(value="/createMenu",method = RequestMethod.POST)
    public ResponseEntity getallmedia(@RequestBody Map param){
        ExecResult result = wechatService.getallmedia(TypeUtils.castToString(param.get("menu")));
        if(result.getResult()>0){
            return ResponseEntity.ok().body( result) ;
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( result) ;
        }
    }

    @RequestMapping(value="/getallmedia",method = RequestMethod.GET)
    public JSONObject getallmedia(@RequestParam("type") String type, @RequestParam("offset") String offset, @RequestParam("count") String count){
        return wechatService.getallmedia(type,offset,count);
    }


    @RequestMapping(value = "/getbtninfo" , method = RequestMethod.GET)
    public JSONObject getbtninfo(){
        return wechatService.getbtninfo();
    }

    @RequestMapping(value="/getuserinfo",method = RequestMethod.GET)
    public JSONObject getUserInfo(@RequestParam("code") String code,@RequestParam(value="state",required = false) String state){
        JSONObject wechatUser= wechatService.getUserInfo(code);
        if(wechatUser.containsKey("openid")){
            WechatUser user=wechatUserService.findUserByOpenid(wechatUser.getString("openid"));
            if(user==null){//新用户
                user=new WechatUser();
            }
            user.setOpenId(wechatUser.getString("openid"));
            user.setNickName(wechatUser.getString("nickname"));
            user.setIcon(wechatUser.getString("headimgurl"));
            user=wechatUserService.save(user);
            return JSONObject.parseObject(JSONObject.toJSONString(user));
        }else{
            return null;
        }
    }


    @RequestMapping(value = "/getqrcode",method = RequestMethod.GET)
    public JSONObject getQRCode(@RequestParam("scene_id") String scene_id ){
        return wechatService.getQRCode(scene_id);
    }

    @RequestMapping(value="/qrresponse",method = RequestMethod.GET)
    public JSONObject responseQrEvent(@RequestParam("qrid") String qrid){
        return wechatService.responseQrEvent(qrid);
    }



}
