package com.alienlab.catpower.web.wechat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.catpower.domain.*;
import com.alienlab.catpower.service.*;
import com.alienlab.catpower.web.wechat.bean.MessageResponse;
import com.alienlab.catpower.web.wechat.bean.NewsMessageResponse;
import com.alienlab.catpower.web.wechat.bean.TextMessageResponse;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import com.alienlab.catpower.web.wechat.util.MessageProcessor;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import groovy.lang.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 橘 on 2016/12/23.
 */
@Service
public class ResponseService {
    @Autowired
    MessageProcessor messageProcessor;
    @Autowired
    WechatUtil wechatUtil;
    @Autowired
    WechatService wechatService;
    @Autowired
    QrTypeService qrTypeService;
    @Autowired
    CourseSchedulingService courseSchedulingService;

    @Autowired
    QrScanLogService qrScanLogService;

    @Autowired
    LearnerService learnerService;

    @Autowired
    CoachService coachService;

    @Autowired
    LearnerChargeService learnerChargeService;

    @Autowired
    WechatUserService wechatUserService;

    @Autowired
    WechatShopCardInfoService wechatShopCardInfoService;

    @Autowired
    WechatVipcardService wechatVipcardService;

    @Value("${wechat.response.defaultText}")
    private String defaultText;
    @Value("${wechat.response.subscribe}")
    private String subscribe;
    @Value("${wechat.response.building}")
    private String commingSoon;
    @Value("${wechat.appid}")
    private String appid;
    @Value("${wechat.secret}")
    private String secret;

    @Value("${wechat.host.basepath}")
    private String domain;
    @Value("${wechat.host.server}")
    private String serverPath;



    public MessageResponse doResponse(String msg){
        JSONObject json_msg=messageProcessor.xml2JSON(msg);
        switch (json_msg.getString("MsgType")){
            case "text":{
                try{

                    if(json_msg.getString("Content").equals("")){

                    }else if(json_msg.getString("Content").equals("冠军")){
                        return messageProcessor.getImageMsg(json_msg.getString("ToUserName"),json_msg.getString("FromUserName"),"wuWdT4MMdsIYxrc6LYQB0vyOvqABsSv44DrZjb2PROY");
                    }else{
                        return messageProcessor.transToCustomMsg(json_msg.getString("ToUserName"),json_msg.getString("FromUserName"));
                    }
                }catch(Exception e){
                    return messageProcessor.getTextMsg(json_msg.getString("ToUserName"),json_msg.getString("FromUserName"),defaultText);
                }
            }
            case "image":{
                break;
            }
            case "voice":{
                break;
            }
            case "video":{
                break;
            }
            case "shortvideo":{
                break;
            }
            case "location":{
                break;
            }
            case "link":{
                break;
            }
            case "event":{
                String event=json_msg.getString("Event");
                String fromuser=json_msg.getString("FromUserName");
                Long eventTime=json_msg.getLong("CreateTime");
                String eventKey=fromuser+"$"+event;
                System.out.println("eventKey>>>"+eventKey);

                if(MessageKey.msgMap.containsKey(eventKey)){

                    Long lasttime=MessageKey.msgMap.get(eventKey);

                    System.out.println("lasttime>>>"+lasttime);
                    if(eventTime-lasttime<50){
                        System.out.println("fast request!!");
                        return null;
                    }
                }else{
                    MessageKey.msgMap.put(eventKey,eventTime);
                }
                switch (event){
                    case "subscribe":{ //用户关注或者未扫码关注事件
                        String from=json_msg.getString("ToUserName");
                        String to=json_msg.getString("FromUserName");
                        String qrkey = json_msg.getString("EventKey");
                        //用户关注，更新用户信息
                        JSONObject wechatUser=wechatUtil.get_user_info_openid(to);
                        if(wechatUser.containsKey("openid")){
                            WechatUser user=wechatUserService.findUserByOpenid(wechatUser.getString("openid"));
                            if(user==null){//新用户
                                user=new WechatUser();
                            }
                            user.setOpenId(wechatUser.getString("openid"));
                            user.setNickName(wechatUser.getString("nickname"));
                            user.setIcon(wechatUser.getString("headimgurl"));
                            user=wechatUserService.save(user);

                            //如果根据openid没有找到Learner，直接产生一条learner记录，直接绑定
                            try {
                                Learner learner=learnerService.findByOpenid(user.getOpenId());
                                if(learner==null){//新用户关注如果没有绑定学员信息，自动创建
                                    learner=new Learner();
                                    learner.setWechatUser(user);
                                    learner.setLearneName(user.getNickName());
                                    learner.setFirstTotime(ZonedDateTime.now());
                                    learnerService.save(learner);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        if(qrkey.startsWith("qrscene_")){
                            json_msg.put("EventKey",qrkey.substring(8));
                            return qrscan(json_msg);
                        }else{
                            String url="http://"+serverPath+"/wechat/snsapi?router=stureg";
                            String link=wechatUtil.getPageAuthUrl(url,"0");
                            NewsMessageResponse result= messageProcessor.getSingleNews(from,to,
                                "欢迎注册猫力健身",
                                link,
                                "http://"+domain+"/img/logo.jpg",
                                "注册成为猫力健身会员，免费领取课程体验卡。"
                            );
                            return result;
                        }
                    }
                    case "unsubscribe":{ //用户取消关注
                        String openid=json_msg.getString("FromUserName");
                        break;
                    }
                    case "CLICK":{//菜单被点击事件
                        String menukey=json_msg.getString("EventKey");
                        String from=json_msg.getString("ToUserName");
                        String to=json_msg.getString("FromUserName");
                        Map param=new HashMap<String,String>();
                        param.put("btn_id",menukey);
                        String qresult=wechatService.getmediainfo(menukey).toJSONString();
                        JSONObject qrjson=JSONObject.parseObject(qresult);
                        if(qrjson.getString("errorMessage")!=null && !qrjson.getString("errorMessage").equals("") ){
                            return messageProcessor.getTextMsg(from,to,qrjson.getString("errorMessage"));
                        }
                        JSONArray ja = qrjson.getJSONArray("news_item");
                        System.out.println("微信media_id获取结果");
                        System.out.println(ja);
                        return messageProcessor.getSinglesNews(from,to,ja);
                    }
                    case "SCAN":{ //用户扫描二维码
                       return qrscan(json_msg);
                    }
                    case "LOCATION":{ //用户提交位置
                        break;
                    }
                    case "submit_membercard_user_info":{//会员卡激活
                        String cardid=json_msg.getString("CardId");
                        String openid=json_msg.getString("FromUserName");
                        String userCardCode=json_msg.getString("UserCardCode");
                        try {
                            wechatVipcardService.activeVipCard(userCardCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }

                    case "user_get_card":{//领取卡券
                        String cardid=json_msg.getString("CardId");
                        String openid=json_msg.getString("FromUserName");
                        String userCardCode=json_msg.getString("UserCardCode");
                        String outerStr=json_msg.getString("OuterStr");
                        try {
                            wechatShopCardInfoService.userGetCard(openid,userCardCode,outerStr,cardid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }

                    case "user_consume_card":{
                        String cardid=json_msg.getString("CardId");
                        String openid=json_msg.getString("FromUserName");
                        String userCardCode=json_msg.getString("UserCardCode");
                        try {
                            wechatShopCardInfoService.activeCard(userCardCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }

                MessageKey.msgMap.remove(eventKey);
                break;

            }

        }

        return null;

    }



    public MessageResponse qrscan(JSONObject json_msg){
        String openid=json_msg.getString("FromUserName");
        QrScanLog log=new QrScanLog();
        log.setOpenid(openid);
        log.setQrkey(json_msg.getString("EventKey"));
        log.setScanTime(ZonedDateTime.now());
        //课程签到
        if(json_msg.getString("EventKey").startsWith("1and")){
            String title="课程签到成功！";
            String url="http://"+serverPath+"/wechat/snsapi?router=stusign";
            String from=json_msg.getString("ToUserName");
            String to=json_msg.getString("FromUserName");
            String state=json_msg.getString("EventKey").substring(4);
            String link=wechatUtil.getPageAuthUrl(url,state);
            CourseScheduling sche=courseSchedulingService.findOne(Long.parseLong(state));
            try{
                learnerChargeService.chargeCourse(to,sche.getId());
                String desc="您已签到：["+sche.getCourse().getCourseName()+"]课程，教练："+sche.getCoach().getCoachName();

                NewsMessageResponse result= messageProcessor.getSingleNews(from,to,
                    title,
                    link,
                    "http://"+domain+"/img/logo.jpg",
                    desc
                );
                log.reply(JSON.toJSONString(result));
                qrScanLogService.save(log);
                return result;
            }catch(Exception e){
                TextMessageResponse result=messageProcessor.getTextMsg(from,to,e.getMessage());
                log.reply(JSON.toJSONString(result));
                qrScanLogService.save(log);
                return result;
            }

        }else if(json_msg.getString("EventKey").startsWith("2and")){//人员绑定
            String title="学员账户绑定成功！";
            String url="http://"+serverPath+"/wechat/snsapi?router=stuindex";
            String from=json_msg.getString("ToUserName");
            String to=json_msg.getString("FromUserName");
            String state=json_msg.getString("EventKey").substring(4);
            String link=wechatUtil.getPageAuthUrl(url,state);
            Learner learner=learnerService.findOne(Long.parseLong(state));
            String desc="您已绑定学员账户："+learner.getLearneName();
            try{
                learnerService.bindWechatUser(to,Long.parseLong(state));
            }catch(Exception e){
                e.printStackTrace();
                title="学员账户绑定出错";
                desc="您绑定学员账户："+learner.getLearneName()+" 时出错了。错误原因："+e.getMessage();
            }

            NewsMessageResponse result=messageProcessor.getSingleNews(from,to,
                title,
                link,
                "http://"+domain+"/img/logo.jpg",
                desc
            );
            log.reply(JSON.toJSONString(result));
            qrScanLogService.save(log);
            return result;
        }else if (json_msg.getString("EventKey").startsWith("3and")){
            String title="教练账户绑定成功！";
            String url="http://"+serverPath+"/wechat/snsapi?router=coachindex";
            String from=json_msg.getString("ToUserName");
            String to=json_msg.getString("FromUserName");
            String state=json_msg.getString("EventKey").substring(4);
            String link=wechatUtil.getPageAuthUrl(url,state);
            Coach coach=coachService.findOne(Long.parseLong(state));
            String desc="您已绑定教练账户："+coach.getCoachName();
            try{
                coachService.bindWechatUser(to,Long.parseLong(state));
            }catch(Exception e){
                e.printStackTrace();
                title="教练账户绑定出错";
                desc="您绑定教练账户："+coach.getCoachName()+" 时出错了。错误原因："+e.getMessage();
            }
            NewsMessageResponse result= messageProcessor.getSingleNews(from,to,
                title,
                link,
                "http://"+domain+"/img/logo.jpg",
                desc
            );
            log.reply(JSON.toJSONString(result));
            qrScanLogService.save(log);
            return result;
        }else if(json_msg.getString("EventKey").startsWith("18and")){//店铺推广二维码扫描
            String from=json_msg.getString("ToUserName");
            String to=json_msg.getString("FromUserName");
            String url="http://"+serverPath+"/wechat/snsapi?router=stureg";
            String link=wechatUtil.getPageAuthUrl(url,"0");
            String state=json_msg.getString("EventKey").substring(5);
            NewsMessageResponse result= messageProcessor.getSingleNews(from,to,
                "欢迎注册猫力健身",
                link,
                "http://"+domain+"/img/logo.jpg",
                "注册成为猫力健身会员，免费领取课程体验卡。"
            );
            log.reply(JSON.toJSONString(result));
            qrScanLogService.save(log);
            return result;
        }else{
            MessageResponse result=getMessage(json_msg);
            log.reply(JSON.toJSONString(result));
            qrScanLogService.save(log);
            return result;
        }
    }

    public MessageResponse getMessage(JSONObject json_msg){
        String qrkey=json_msg.getString("EventKey");
        String from=json_msg.getString("ToUserName");
        String to=json_msg.getString("FromUserName");
        System.out.println(qrkey);
        Map param=new HashMap<String,String>();
        param.put("key",qrkey);
        try{
            String qresult=qrTypeService.getqrcodepar(qrkey).toJSONString();
            if(qresult.equals("")){

            }else{
                JSONObject qrjson=JSONObject.parseObject(qresult);
                System.out.println(qrjson);
                JSONObject qrtype=qrjson.getJSONObject("qrType");
                String title=qrtype.getString("qrTypeName");
                String namefield=qrtype.getString("qrTypeNamefield");
                String idfield=qrtype.getString("qrTypeIdfield");
                JSONObject data=qrjson.getJSONObject("data");

                String typeurl=qrtype.getString("qrTypeUrl");
                if(!typeurl.startsWith("http://")){
                    typeurl="http://"+domain+"/"+typeurl;//自动加上当前域
                }

                String link="";
                String state=data.getString(namefield);
                link=wechatUtil.getPageAuthUrl(typeurl,data.getString(idfield));

                System.out.println(qrjson);
                return messageProcessor.getSingleNews(from,to,
                        title,
                        link,
                        "http://"+domain+"/img/logo.jpg",
                        state
                );
            }
        }catch (Exception e){
            e.printStackTrace();
            return messageProcessor.getTextMsg(from,to,"很抱歉，系统正忙，请稍后再试");
        }

        return null;
    }
}
