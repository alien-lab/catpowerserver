package com.alienlab.catpower.web.wechat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.alienlab.catpower.web.rest.util.ExecResult;
import com.alienlab.catpower.web.wechat.bean.JSApiTicket;
import com.alienlab.catpower.web.wechat.bean.entity.WechatMaterial;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import com.alienlab.catpower.web.wechat.util.*;
import org.apache.log4j.Logger;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by 橘 on 2017/5/8.
 */
@Service
public class WechatService {
    @Autowired
    SignUtil signUtil;
    @Autowired
    WechatUtil wechatUtil;

    @Autowired
    PayCommonUtil payCommonUtil;

    private static Logger logger = Logger.getLogger(WechatService.class);


    @Value("${wechat.host.basepath}")
    private String wechathost;
    @Value("${wechat.appid}")
    private String wechatappid;

    @Value("${wechat.secret}")
    private String wechatsecret;

    @Value("${wechat.payid}")
    private String wechatpayid;

    @Value("${wechat.paycallback}")
    private String wechatpaycallback;

    public Map<String,String> getJsApiTicket(String url){
        return wechatUtil.getJsapiSignature(url);
    }

    public JSApiTicket getCardTicket(){
        return wechatUtil.getCardTicket(wechatappid,wechatsecret);
    }

    public ResponseEntity responseOptions(String code){
        logger.error("OPTIONS request");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
    }
    @Autowired
    WechatUserService wechatUserService;

    @Autowired
    WechatMaterialService wechatMaterialService;
    public JSONObject getmediainfotest(String media_id){
        JSONObject result = new JSONObject();
        result = wechatUtil.get_media_info(media_id);
        return result;
    }

    public JSONObject getmediainfo(String btn_id){
        JSONObject result = new JSONObject();
        JSONArray jsonArray =  new JSONArray();
        List<WechatMaterial> wechatMaterials = wechatMaterialService.findMaterialByBtnId(btn_id);
        if(wechatMaterials==null || wechatMaterials.size()==0){
            result.put("errorMessage","暂无对应资讯");
            return result;
        }else{
            for (WechatMaterial wechatMaterial : wechatMaterials){
                JSONObject  checkResult = wechatUtil.get_media_info(wechatMaterial.getMediaId());
                System.out.println("微信获取素材信息");
                System.out.println(checkResult.toJSONString());
                if(checkResult.get("errcode")!=null){
                    result.put("errorMessage","素材获取出错！");
                    return result;
                }
                JSONArray array = new JSONArray();
                array = checkResult.getJSONArray("news_item");
                for(int i =0;i<array.size();i++){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject=  array.getJSONObject(i);
                    if(i==0){
                        jsonObject.put("media_id",wechatMaterial.getMediaId());
                    }else{
                        jsonObject.put("media_id",wechatMaterial.getMediaId()+"$"+i+"");
                    }
                    jsonArray.add(jsonObject);
                }

                System.out.println(result);
            }
            result.put("news_item",jsonArray);
            return result;
        }
    }

    public ExecResult getallmedia(String menu){
        ExecResult result = new ExecResult();
        try {
            JSONObject menuJson = (JSONObject) JSONObject.parse(menu);
            boolean flag = wechatUtil.createMenuByUrl(menuJson);
            if(flag){
                result.setResult(true);
                result.setMessage("菜单创建成功");
                return result ;
            }
            result.setResult(false);
            result.setMessage("菜单创建失败");
            return result;
        }catch (Exception e){
            result.setResult(false);
            result.setMessage("菜单创建失败");
            return  result ;
        }

    }

    public JSONObject getallmedia(String type, String offset,String count){
        JSONObject result = new JSONObject();
        result = wechatUtil.get_all_media(type,offset,count);
        return result;
    }

    public JSONObject getbtninfo(){
        JSONObject result = new JSONObject();
        result = wechatUtil.get_btn_info();
        if(result.get("errcode")!=null){
            result.put("errorMessage","获取菜单列表出错！");
            return result;
        }
        return result;
    }


    public JSONObject getUserInfo(String code){
        logger.info("/getuserinfo A code is "+code);

        JSONObject jo = wechatUtil.get_access_token(code);
        if(jo.containsKey("errcode")&&jo.getString("errcode").length()>1){
            ExecResult er=new ExecResult(false,"获取access_token错误");
            return JSONObject.parseObject(er.toString());
        }else{
            logger.info("/getuserinfo B code is "+code);
            JSONObject resultjo = wechatUtil.get_user_info(jo.getString("access_token"),jo.getString("openid"));

            if(!resultjo.containsKey("openid")){
                return null;
            }
            //每调用一次获取身份方法，就更新一次库里的微信用户信息。
            WechatUser user=wechatUserService.findUserByOpenid(resultjo.getString("openid"));
            if(user==null){//如果此用户不存在
                logger.info("new user!!!>>>>"+resultjo.getString("openid"));
                user=new WechatUser();
            }
            String nickName = EmojiUtils.filter(resultjo.getString("nickname"));
            logger.info("/nickName filter  "+nickName);
            user.setArea(resultjo.getString("province")+resultjo.getString("city"));
            user.setIcon(resultjo.getString("headimgurl"));
            user.setNickName(nickName);
            user.setOpenId(resultjo.getString("openid"));
            if(resultjo.containsKey("unionid")){
//                user.setWechatUnionid(resultjo.getString("unionid"));
            }

            wechatUserService.save(user);

            logger.info("/getuserinfo C code is "+code);
            return resultjo;
        }



    }


    public JSONObject getQRCode(String scene_id ){
        JSONObject jo = wechatUtil.get_qr_code_ticket(scene_id);
        return jo;
    }


    public JSONObject responseQrEvent(String qrid){
        return null;
    }


    //统一下单
    public Map<String,String> makeOrder(String orderName,String orderNo,int money,String cusIp,String openid){
        logger.info("正在调用微信下单系统");
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("appid", wechatappid);
        logger.info("正在调用微信下单系统："+wechatappid);
        parameters.put("mch_id", wechatpayid);
        logger.info("正在调用微信下单系统："+wechatpayid);
        parameters.put("nonce_str", payCommonUtil.CreateNoncestr());
        logger.info("正在调用微信下单系统："+payCommonUtil.CreateNoncestr());
        parameters.put("body", orderName);
        logger.info("正在调用微信下单系统："+orderName);
        parameters.put("out_trade_no", orderNo);
        logger.info("正在调用微信下单系统："+orderNo);
        parameters.put("total_fee", String.valueOf(money));
        logger.info("正在调用微信下单系统："+String.valueOf(money));
        parameters.put("spbill_create_ip",cusIp);
        logger.info("正在调用微信下单系统："+cusIp);
        parameters.put("notify_url", wechatpaycallback);
        logger.info("正在调用微信下单系统："+wechatpaycallback);
        parameters.put("trade_type", "JSAPI");
        parameters.put("openid", openid);
        String sign = payCommonUtil.createSign("UTF-8", parameters);
        logger.info("正在调用微信下单系统："+sign);
        parameters.put("sign", sign);
        String requestXML = payCommonUtil.getRequestXml(parameters);
        logger.info("微信支付系统，下单返回内容>>>"+requestXML);
        String result = HttpsInvoker.httpRequestStr("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", requestXML);
        logger.info("微信支付系统，下单返回内容>>>"+result);

        try {
            return XMLUtil.doXMLParse(result);
        } catch (JDOMException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getPayParam(Map<String,String>order){
        SortedMap<Object,Object> params = new TreeMap<Object,Object>();
        params.put("appId",wechatappid);
        params.put("timeStamp", Long.toString(new Date().getTime()/1000));
        params.put("nonceStr", payCommonUtil.CreateNoncestr());
        params.put("package", "prepay_id="+order.get("prepay_id"));
        params.put("signType", "MD5");
        String paySign =  payCommonUtil.createSign("UTF-8", params);
        params.put("packageValue", "prepay_id="+order.get("prepay_id"));    //这里用packageValue是预防package是关键字在js获取值出错
        params.put("paySign", paySign);                                                          //paySign的生成规则和Sign的生成规则一致

        JSONObject json =JSONObject.parseObject(JSONObject.toJSONString(params));
        return json;
    }

    public Map<String,String> getOrder(String orderNo){
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("appid", wechatappid);
        parameters.put("mch_id", wechatpayid);
        parameters.put("out_trade_no", orderNo);
        parameters.put("nonce_str", payCommonUtil.CreateNoncestr());
        String sign = payCommonUtil.createSign("UTF-8", parameters);
        parameters.put("sign", sign);
        String requestXML = payCommonUtil.getRequestXml(parameters);

        logger.info("get order info>>>"+requestXML);
        String result = HttpsInvoker.httpRequestStr("https://api.mch.weixin.qq.com/pay/orderquery", "POST", requestXML);

        try {
            return XMLUtil.doXMLParse(result);
        } catch (JDOMException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String,String> getCardExt(String cardid){
        Map<String,String> result=new HashMap<String,String>();
        String timestamp=Long.toString(System.currentTimeMillis() / 1000);
        JSApiTicket apiTicket=wechatUtil.getCardTicket(wechatappid,wechatsecret);
        result.put("timestamp",timestamp);
        System.out.println("get Api_ticket>>"+apiTicket.getTicket());
        result.put("signature",signUtil.genSign(apiTicket.getTicket(),timestamp,cardid));
        System.out.println("get Api_ticket map>>"+ JSON.toJSONString(result));

        return result;
    }


}
