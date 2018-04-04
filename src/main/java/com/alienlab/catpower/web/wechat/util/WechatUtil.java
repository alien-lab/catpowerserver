package com.alienlab.catpower.web.wechat.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import com.alienlab.catpower.web.wechat.bean.AccessToken;
import com.alienlab.catpower.web.wechat.bean.JSApiTicket;
import com.alienlab.catpower.web.wechat.bean.entity.WechatMessageLog;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import com.alienlab.catpower.web.wechat.service.WechatMessageLogService;
import com.alienlab.catpower.web.wechat.service.WechatUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.print.Pageable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by 橘 on 2016/12/26.
 */
@Component
public class WechatUtil {
    private static final Logger logger = LoggerFactory.getLogger("WechatUtil");
    // 获取access_token的接口地址（GET） 限200（次/天）
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    //JSAPI请求URL
    public final static String jsapi_ticket_url="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    public final static String menu_url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    public final static String user_info="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    public final static String cus_msg="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

    public final static String access_toekn_url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    public final static String user_info_openid="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN ";

    public final static String get_user_info_openid="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN ";

    public final static  String qr_code_ticket_url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";

    public final static String template_msg_url="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    public final static String  get_media_url = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";

    public final static String get_all_media = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";

    public final static String get_btn_info = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=ACCESS_TOKEN";

    public final static String lite_program_openid="https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";
    @Autowired
    SignUtil sign;
    @Value("${wechat.appid}")
    private String wxappid;
    @Value("${wechat.secret}")
    private String wxappsecret;

    @Value("${wechat.liteprogram.appid}")
    private String lpwxappid;
    @Value("${wechat.liteprogram.secret}")
    private String lpwxappsecret;

    @Value("${wechat.host.basepath}")
    private String wechathost;
    public Map<String, String> getJsapiSignature(String url){
        logger.info("获得微信js-SDK加密signature:"+url);
        JSApiTicket jt=getJSApiTicket(wxappid,wxappsecret);
        if(jt==null){
            return null;
        }
        String jsapi_ticket=jt.getTicket();
        logger.info("获得jsapi_ticket："+jsapi_ticket);
        Map map=sign.sign(jsapi_ticket, url);
        map.put("appid", wxappid);
        return map;
    }

    public  JSONObject  get_access_token(String code){
         String url = access_toekn_url.replaceAll("APPID",wxappid).replaceAll("SECRET",wxappsecret).replaceAll("CODE",code);
         JSONObject jsonObject = HttpsInvoker.httpRequest(url,"GET","");
        return jsonObject;
    }



    public  JSONObject  get_media_info(String media_id){
        JSONObject jo = new JSONObject();
        jo.put("media_id",media_id);
        AccessToken at=getAccessToken();
        if(at==null){
            return null;
        }
        String posturl=get_media_url.replace("ACCESS_TOKEN",at.getToken()).replace("MEDIA_ID",media_id);
        JSONObject jsonObject = HttpsInvoker.httpRequest(posturl,"POST",jo.toJSONString());
        return jsonObject;
    }

    public  JSONObject  get_user_info_openid(String openid){
        JSONObject jo = new JSONObject();
        jo.put("openid",openid);
        AccessToken at=getAccessToken();
        if(at==null){
            return null;
        }
        String posturl=get_user_info_openid.replace("ACCESS_TOKEN",at.getToken()).replace("OPENID",openid);
        JSONObject jsonObject = HttpsInvoker.httpRequest(posturl,"POST",jo.toJSONString());
        return jsonObject;
    }

    public  JSONObject  get_btn_info(){
        AccessToken at=getAccessToken();
        if(at==null){
            return null;
        }
        String posturl=get_btn_info.replace("ACCESS_TOKEN",at.getToken());
        JSONObject jsonObject = HttpsInvoker.httpRequest(posturl,"GET","");
        return jsonObject;
    }


    public  JSONObject  get_all_media(String type,String offset,String count){
        JSONObject jo = new JSONObject();
        jo.put("type",type);
        jo.put("offset",offset);
        jo.put("count",count);
        AccessToken at=getAccessToken();
        if(at==null){
            return null;
        }
        String posturl=get_all_media.replace("ACCESS_TOKEN",at.getToken()).replace("TYPE",type).replace("OFFSET",offset).replace("COUNT",count);
        JSONObject jsonObject = HttpsInvoker.httpRequest(posturl,"POST",jo.toJSONString());
        return jsonObject;
    }

    public  JSONObject get_user_info(String access_toekn,String openid){
        String url = user_info_openid.replaceAll("ACCESS_TOKEN",access_toekn).replaceAll("OPENID",openid);
        JSONObject jsonObject = HttpsInvoker.httpRequest(url,"GET","");
        return jsonObject;
    }

    public JSONObject get_qr_code_ticket(String scene_id){
        String access_toen = getAccessToken().getToken();
        String url = qr_code_ticket_url.replaceAll("TOKEN",access_toen);
        String jsonMsg="{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"%s\"}}}";
        JSONObject json = HttpsInvoker.httpRequest(url,"POST",String.format(jsonMsg,scene_id));
        return json;
    }

    private  JSApiTicket jsticket=null;
    public  JSApiTicket getJSApiTicket(String appid,String secret){
        logger.info("获取微信jsticket");
        if(jsticket== null){
            logger.info("系统中jsticket不存在！");
            jsticket=getJsApiTicket(wxappid,wxappsecret);
        }else{
            Calendar c=Calendar.getInstance();
            long now=c.getTimeInMillis();
            if(now-jsticket.getTicketTime()>=7000*1000){
                logger.info("系统中jsticket已超时！gettoken时间："+jsticket.getTicketTime()+",当前时间:"+now);
                jsticket=getJsApiTicket(wxappid,wxappsecret);
            }else{
                logger.info("系统中jsticket未过期可使用");
            }
        }
        return jsticket;
    }

    private  JSApiTicket getJsApiTicket(String appid,String secret){
        logger.info("微信服务号获取新JSApiTicket");
        HttpsInvoker invoker=new HttpsInvoker();
        JSApiTicket jt=null;
        AccessToken at=getAccessToken();
        if(at==null){
            return jt;
        }
        String requestUrl = jsapi_ticket_url.replace("ACCESS_TOKEN", at.getToken());
        JSONObject jsonObject = HttpsInvoker.httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                jt = new JSApiTicket();
                jt.setErrcode(jsonObject.getString("errcode"));
                jt.setErrmsg(jsonObject.getString("errmsg"));
                jt.setExpires_in(jsonObject.getString("expires_in"));
                jt.setTicket(jsonObject.getString("ticket"));
                Calendar c=Calendar.getInstance();
                jt.setTicketTime(c.getTimeInMillis());
            } catch (JSONException e) {
                jt = null;
                // 获取token失败
                logger.error("获取JSApiTicket失败 errcode:"+jsonObject.getString("errcode")+"errmsg:"+jsonObject.getString("errmsg"));
            }
        }
        return jt;
    }

    public JSApiTicket cardTicket=null;
    public  JSApiTicket getCardTicket(String appid,String secret){
        logger.info("获取微信cardticket");
        if(cardTicket== null){
            logger.info("系统中cardticket不存在！");
            cardTicket=genCardTicket(wxappid,wxappsecret);
        }else{
            Calendar c=Calendar.getInstance();
            long now=c.getTimeInMillis();
            if(now-cardTicket.getTicketTime()>=7000*1000){
                logger.info("系统中cardticket已超时！gettoken时间："+jsticket.getTicketTime()+",当前时间:"+now);
                cardTicket=genCardTicket(wxappid,wxappsecret);
            }else{
                logger.info("系统中cardticket未过期可使用");
            }
        }
        return cardTicket;
    }

    private  JSApiTicket genCardTicket(String appid,String secret){
        logger.info("微信服务号获取新CardTicket");
        HttpsInvoker invoker=new HttpsInvoker();
        JSApiTicket jt=null;
        AccessToken at=getAccessToken();
        if(at==null){
            return jt;
        }
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=wx_card"
            .replace("ACCESS_TOKEN", at.getToken());
        JSONObject jsonObject = HttpsInvoker.httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                jt = new JSApiTicket();
                jt.setErrcode(jsonObject.getString("errcode"));
                jt.setErrmsg(jsonObject.getString("errmsg"));
                jt.setExpires_in(jsonObject.getString("expires_in"));
                jt.setTicket(jsonObject.getString("ticket"));
                Calendar c=Calendar.getInstance();
                jt.setTicketTime(c.getTimeInMillis());
            } catch (JSONException e) {
                jt = null;
                // 获取token失败
                logger.error("获取CardTicket失败 errcode:"+jsonObject.getString("errcode")+"errmsg:"+jsonObject.getString("errmsg"));
            }
        }
        return jt;
    }


    private  AccessToken accessToken = null;
    public  AccessToken getAccessToken() {
        logger.info("获取微信AccessToken");
        if(accessToken== null){
            logger.info("系统中token不存在！");
            accessToken=getToken();
        }else{
            Calendar c=Calendar.getInstance();
            long now=c.getTimeInMillis();
            if(now-accessToken.getTokenTime()>=7000*1000){
                logger.info("系统中token已超时！gettoken时间："+accessToken.getTokenTime()+",当前时间:"+now);
                accessToken=getToken();
            }else{
                logger.info("系统中token未过期可使用");
            }
        }
        return accessToken;
    }

    public AccessToken getToken(){
        logger.info("微信服务号获取新token");
        AccessToken at=null;
        String requestUrl = access_token_url.replace("APPID", wxappid).replace("APPSECRET", wxappsecret);
        HttpsInvoker invoker=new HttpsInvoker();
        JSONObject jsonObject = HttpsInvoker.httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                at = new AccessToken();
                at.setToken(jsonObject.getString("access_token"));
                at.setExpiresIn(jsonObject.getIntValue("expires_in"));
                Calendar c=Calendar.getInstance();
                at.setTokenTime(c.getTimeInMillis());
            } catch (JSONException e) {
                at = null;
                // 获取token失败
                logger.error("获取token失败 errcode:"+jsonObject.getString("errcode")+"errmsg:"+jsonObject.getString("errmsg"));
            }
        }
        return at;
    }


    private  AccessToken lpaccessToken = null;
    public  AccessToken getLpAccessToken() {
        logger.info("获取微信小程序AccessToken");
        if(lpaccessToken== null){
            logger.info("系统中token不存在！");
            lpaccessToken=getLiteProgramAT();
        }else{
            Calendar c=Calendar.getInstance();
            long now=c.getTimeInMillis();
            if(now-lpaccessToken.getTokenTime()>=7000*1000){
                logger.info("系统中token已超时！gettoken时间："+lpaccessToken.getTokenTime()+",当前时间:"+now);
                lpaccessToken=getLiteProgramAT();
            }else{
                logger.info("系统中token未过期可使用");
            }
        }
        return lpaccessToken;
    }
    public AccessToken getLiteProgramAT(){
        logger.info("微信小程序获取新accesstoken");
        AccessToken at=null;
        String requestUrl = access_token_url.replace("APPID", lpwxappid).replace("APPSECRET", lpwxappsecret);
        HttpsInvoker invoker=new HttpsInvoker();
        JSONObject jsonObject = HttpsInvoker.httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                at = new AccessToken();
                at.setToken(jsonObject.getString("access_token"));
                at.setExpiresIn(jsonObject.getIntValue("expires_in"));
                Calendar c=Calendar.getInstance();
                at.setTokenTime(c.getTimeInMillis());
            } catch (JSONException e) {
                at = null;
                // 获取token失败
                logger.error("获取token失败 errcode:"+jsonObject.getString("errcode")+"errmsg:"+jsonObject.getString("errmsg"));
            }
        }
        return at;
    }



    public JSONObject getMenu(){
        JSONObject menu=new JSONObject();
        JSONArray buttons=new JSONArray();//定义一个按钮组
        //第一组按钮
        JSONObject button1=new JSONObject();
        button1.put("name","SUMEC");//第一个一级菜单
        JSONArray btn1_sub=new JSONArray();//第一个一级菜单的子菜单
        JSONObject btn1_sub_btn4=new JSONObject();
        btn1_sub_btn4.put("type","click");
        btn1_sub_btn4.put("name","关于我们");
        btn1_sub_btn4.put("key","M11");
        btn1_sub.add(btn1_sub_btn4);
        JSONObject btn1_sub_btn2=new JSONObject();
        btn1_sub_btn2.put("type","click");
        btn1_sub_btn2.put("name","行业动态");
        btn1_sub_btn2.put("key","M12");
        btn1_sub.add(btn1_sub_btn2);
        JSONObject btn1_sub_btn3=new JSONObject();
        btn1_sub_btn3.put("type","click");
        btn1_sub_btn3.put("name","金融资讯");
        btn1_sub_btn3.put("key","M13");
        btn1_sub.add(btn1_sub_btn3);
        JSONObject btn1_sub_btn1=new JSONObject();
        btn1_sub_btn1.put("type","click");
        btn1_sub_btn1.put("name","打折转售");
        btn1_sub_btn1.put("key","M14");
        btn1_sub.add(btn1_sub_btn1);

        button1.put("sub_button",btn1_sub);
        buttons.add(button1);

        menu.put("button",buttons);
        return menu;
    }

    public boolean createMenuByUrl(JSONObject menu){
        AccessToken at=getAccessToken();
        String token=at.getToken();
        String url=menu_url.replaceAll("ACCESS_TOKEN",token);
        JSONObject jsonObject = HttpsInvoker.httpRequest(url, "POST", menu.toJSONString());
        logger.debug("menu create:"+jsonObject.toJSONString());
        if (null != jsonObject) {
            if (0 != jsonObject.getIntValue("errcode")) {
                int result = jsonObject.getIntValue("errcode");
                logger.error("创建菜单失败 errcode:"+jsonObject.getIntValue("errcode")+",errmsg:"+jsonObject.getString("errmsg"));
            }else{
                logger.debug("菜单创建成功！");
            }
        }
        return true;
    }

    public boolean createMenu(){
        JSONObject menu=getMenu();//获得菜单
        AccessToken at=getAccessToken();
        String token=at.getToken();
        String url=menu_url.replaceAll("ACCESS_TOKEN",token);
        JSONObject jsonObject = HttpsInvoker.httpRequest(url, "POST", menu.toJSONString());
        logger.debug("menu create:"+jsonObject.toJSONString());
        if (null != jsonObject) {
            if (0 != jsonObject.getIntValue("errcode")) {
                int result = jsonObject.getIntValue("errcode");
                logger.error("创建菜单失败 errcode:"+jsonObject.getIntValue("errcode")+",errmsg:"+jsonObject.getString("errmsg"));
            }else{
                logger.debug("菜单创建成功！");
            }
        }
        return true;
    }

    public JSONObject getUserInfo(String openid){
        AccessToken at=getAccessToken();
        String url=user_info.replaceAll("ACCESS_TOKEN",at.getToken()).replaceAll("OPENID",openid);
        JSONObject jsonObject = HttpsInvoker.httpRequest(url, "GET", null);
        if (null != jsonObject) {
            if (0 != jsonObject.getIntValue("errcode")) {
                int result = jsonObject.getIntValue("errcode");
                logger.error("获取用户信息失败 errcode:"+jsonObject.getIntValue("errcode")+",errmsg:"+jsonObject.getString("errmsg"));
            }else{
                logger.debug("获取用户信息成功："+jsonObject.toJSONString());
            }
        }
        return jsonObject;
    }


    public JSONObject sendTextMsg(String openid,String text){
        AccessToken at=getAccessToken();
        //AccessToken at=getAccessToken("wxb85df50f1f75e679","c0e8667b1b3265d53d5708b0cdbab42b");
        String url=cus_msg.replaceAll("ACCESS_TOKEN",at.getToken());
        JSONObject msg=new JSONObject();
        msg.put("touser",openid);
        msg.put("msgtype","text");
        JSONObject content=new JSONObject();
        content.put("content",text);
        msg.put("text",content);
        JSONObject jsonObject = HttpsInvoker.httpRequest(url, "POST", msg.toJSONString());
        if (null != jsonObject) {
            if (0 != jsonObject.getIntValue("errcode")) {
                int result = jsonObject.getIntValue("errcode");
                logger.error("发送用户信息失败 errcode:"+jsonObject.getIntValue("errcode")+",errmsg:"+jsonObject.getString("errmsg"));
            }else{
                logger.debug("发送用户信息成功："+jsonObject.toJSONString());
            }
        }

        return jsonObject;
    }


    @Autowired
    WechatMessageLogService wechatMessageLogService;
    @Autowired
    WechatUserService wechatUserService;
    /**
     * 发送模板消息
     * @param openid
     * @param url
     * @param tid
     * @param post
     * @return
     */
    public JSONObject sendTemplateMsg(String openid,String url,String tid,JSONObject post ){
        AccessToken at=getAccessToken();
        if(at==null){
            return null;
        }
        String posturl=template_msg_url.replace("ACCESS_TOKEN",at.getToken());
        JSONObject param=new JSONObject();
        param.put("touser",openid);
        param.put("template_id",tid);
        param.put("url",url);
        param.put("data",post);
        JSONObject jsonObject = HttpsInvoker.httpRequest(posturl, "POST", param.toJSONString());
        if (null != jsonObject) {
            if (0 != jsonObject.getIntValue("errcode")) {
                int result = jsonObject.getIntValue("errcode");
                logger.error("发送用户信息失败 errcode:"+jsonObject.getIntValue("errcode")+",errmsg:"+jsonObject.getString("errmsg"));
            }else{
                WechatMessageLog log=new WechatMessageLog();
                log.setMessageBody(post.toJSONString());
                WechatUser user=wechatUserService.findUserByOpenid(openid);
                log.setWechatUser(user);
                log.setMessageTime(ZonedDateTime.now());
                //log.setId(jsonObject.("msgid"));
                wechatMessageLogService.save(log);
                logger.debug("发送用户信息成功："+jsonObject.toJSONString());
            }
        }
        return jsonObject;
    }

    /**
     * 获取微信页面认证URL
     * @param url
     * @return
     */
    public String getPageAuthUrl(String url,String state){
        String baseurl="https://open.weixin.qq.com/connect/oauth2/authorize?appid=$(APPID)&redirect_uri=$(URL)&response_type=code&scope=snsapi_userinfo&state=$(STATE)";
        //if(wxappid==null)wxappid="wxf617bb39539b5f2e";
        baseurl=baseurl.replace("$(APPID)", wxappid);
        if(state==null)state="null";
        baseurl=baseurl.replace("$(STATE)", state);
        try {
            url= URLEncoder.encode(url,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        baseurl=baseurl.replace("$(URL)",url);
        return baseurl;
    }

    public JSONObject getVipCard(String cardid,String usercode){
        String baseurl="https://api.weixin.qq.com/card/membercard/userinfo/get?access_token=ACCESS_TOKEN";
        AccessToken at=getAccessToken();
        baseurl=baseurl.replace("ACCESS_TOKEN",at.getToken());
        JSONObject param=new JSONObject();
        param.put("card_id",cardid);
        param.put("code",usercode);
        JSONObject jsonObject = HttpsInvoker.httpRequest(baseurl, "POST", param.toJSONString());
        return jsonObject;
    }

    public JSONObject getShopCard(String cardid){
        String baseurl="https://api.weixin.qq.com/card/get?access_token=ACCESS_TOKEN";
        AccessToken at=getAccessToken();
        baseurl=baseurl.replace("ACCESS_TOKEN",at.getToken());
        JSONObject param=new JSONObject();
        param.put("card_id",cardid);
        JSONObject jsonObject = HttpsInvoker.httpRequest(baseurl, "POST", param.toJSONString());
        return jsonObject;
    }

    public JSONObject getShopCardIdList(int offset,int size){
        String baseurl="https://api.weixin.qq.com/card/batchget?access_token=ACCESS_TOKEN";
        AccessToken at=getAccessToken();
        baseurl=baseurl.replace("ACCESS_TOKEN",at.getToken());
        JSONObject param=new JSONObject();
        param.put("offset",offset);
        param.put("count",size);
        JSONObject jsonObject = HttpsInvoker.httpRequest(baseurl, "POST", param.toJSONString());
        return jsonObject;
    }

    public JSONObject cardRecharge(String cardid,String code){
        String baseurl="https://api.weixin.qq.com/card/code/get?access_token=ACCESS_TOKEN";
        AccessToken at=getAccessToken();
        baseurl=baseurl.replace("ACCESS_TOKEN",at.getToken());
        JSONObject param=new JSONObject();
        param.put("card_id",cardid);
        param.put("code",code);
        param.put("check_consume",true);
        JSONObject jsonObject = HttpsInvoker.httpRequest(baseurl, "POST", param.toJSONString());
        return jsonObject;
    }

    public JSONObject getLiteProgramOpenid(String code){
        String url = lite_program_openid.replaceAll("APPID",lpwxappid).replaceAll("SECRET",lpwxappsecret).replaceAll("CODE",code);
        JSONObject jsonObject = HttpsInvoker.httpRequest(url,"GET","");
        return jsonObject;
    }

    public JSONObject getLiteProgramQrcode(String page,String params){
        String url="https://api.weixin.qq.com/wxa/getwxacode?access_token=ACCESS_TOKEN";
        AccessToken accessToken=getLpAccessToken();
        url=url.replace("ACCESS_TOKEN",accessToken.getToken());
        JSONObject param=new JSONObject();
        param.put("path",page+"?"+params);
        JSONObject jsonObject = HttpsInvoker.httpRequest(url, "POST", param.toJSONString());
        logger.info("get liteprogram qrcode>>>"+jsonObject.toJSONString());
        return jsonObject;
    }

/*  public static void main(String [] args){
        WechatUtil w=new WechatUtil();
        w.createMenu();
        System.out.println("创建成功");

   }*/
}
