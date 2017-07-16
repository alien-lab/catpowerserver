package com.alienlab.catpower.web.wechat.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.service.CourseSchedulingService;
import com.alienlab.catpower.service.LearnerChargeService;
import com.alienlab.catpower.service.LearnerService;
import com.alienlab.catpower.web.wechat.bean.MessageResponse;
import com.alienlab.catpower.web.wechat.bean.NewsMessageResponse;
import com.alienlab.catpower.web.wechat.bean.TextMessageResponse;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import com.alienlab.catpower.web.wechat.util.MessageProcessor;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    LearnerService learnerService;

    @Autowired
    LearnerChargeService learnerChargeService;

    @Autowired
    WechatUserService wechatUserService;

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


    public MessageResponse doResponse(String msg){
        JSONObject json_msg=messageProcessor.xml2JSON(msg);
        switch (json_msg.getString("MsgType")){
            case "text":{
                try{

                    if(json_msg.getString("Content").equals("")){

                    }else if(json_msg.getString("Content").equals("业务员绑定")){

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
                        }
                        if(qrkey.startsWith("qrscene_")){
                            json_msg.put("EventKey",qrkey.substring(8));
                            return qrscan(json_msg);
                        }else{
                            return messageProcessor.getTextMsg(json_msg.getString("ToUserName"),json_msg.getString("FromUserName"),defaultText);
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
                }
                break;
            }
        }

        return null;

    }

    public MessageResponse qrscan(JSONObject json_msg){
        //课程签到
        if(json_msg.getString("EventKey").startsWith("1and")){
            String title="课程签到成功！";
            String url="http://"+domain+"/#!/stusign";
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
                return result;
            }catch(Exception e){
                TextMessageResponse result=messageProcessor.getTextMsg(from,to,e.getMessage());
                return result;
            }



        }else if(json_msg.getString("EventKey").startsWith("2and")){//人员绑定
            String title="学员账户绑定成功！";
            String url="http://"+domain+"/#!/stuindex";
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
            return messageProcessor.getSingleNews(from,to,
                title,
                url,
                "http://"+domain+"/img/logo.jpg",
                desc
            );
        }else{
            return getMessage(json_msg);
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
//                //如果是业务员推荐企业注册
//                if(typeurl.indexOf("enterpriseRegistration")>0&&namefield.equals("stuff_name")){
//                    link=wechatUtil.getPageAuthUrl(typeurl,"0and"+data.getString(idfield));
//                } else if(qrkey.startsWith("6")){
//                    link=wechatUtil.getPageAuthUrl(typeurl,"6and"+data.getString(idfield));
//                }else{
//                    link=wechatUtil.getPageAuthUrl(typeurl,data.getString(idfield));
//                }

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
