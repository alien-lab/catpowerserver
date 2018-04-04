package com.alienlab.catpower.web.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.catpower.web.wechat.bean.AccessToken;

import com.alienlab.catpower.web.wechat.util.MyX509TrustManager;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.*;

import java.net.URL;

@Controller
@RequestMapping("/api")
public class WechatLiteProgramQrController {
    @Autowired
    WechatUtil wechatUtil;

    @GetMapping("/wechat/open/liteqr")
    public void getQrcode(@RequestParam String path, HttpServletResponse response){
        AccessToken at=wechatUtil.getLpAccessToken();
        String url="https://api.weixin.qq.com/wxa/getwxacode?access_token=ACCESS_TOKEN";
        url=url.replace("ACCESS_TOKEN",at.getToken());
        JSONObject param=new JSONObject();
        param.put("path",path);

        StringBuffer buffer = new StringBuffer();
        try {
            System.setProperty("https.protocols", "TLSv1");
            // 创建SSLContext对象，并使用我们指定的信任管理器初始�?
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            System.out.println(sslContext.getProtocol());
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL address = new URL(url);

            HttpsURLConnection httpUrlConn = (HttpsURLConnection) address.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST�?
            httpUrlConn.setRequestMethod("POST");

            // 当有数据�?要提交时
            if (null != param) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(param.toJSONString().getBytes("UTF-8"));
                outputStream.close();
            }
            response.setContentType("image/jpg");
            InputStream inputStream = httpUrlConn.getInputStream();
            BufferedImage image=ImageIO.read(inputStream);
            ImageIO.write(image,"jpg",response.getOutputStream());
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
