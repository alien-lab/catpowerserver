package com.alienlab.catpower.web.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.alienlab.catpower.domain.BuyCourse;
import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.domain.LearnerCharge;
import com.alienlab.catpower.service.BuyCourseService;
import com.alienlab.catpower.service.CourseSchedulingService;
import com.alienlab.catpower.service.LearnerChargeService;
import com.alienlab.catpower.service.LearnerService;
import com.alienlab.catpower.web.wechat.service.WechatMessageService;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by 橘 on 2017/5/14.
 */
@Service
public class WechatMessageServiceImpl implements WechatMessageService {
    @Value("${wechat.host.basepath}")
    private String wechathost;
    @Autowired
    CourseSchedulingService courseSchedulingService;

    @Autowired
    LearnerChargeService learnerChargeService;

    @Autowired
    WechatUtil wechatUtil;

    @Autowired
    LearnerService learnerService;

    @Autowired
    BuyCourseService buyCourseService;
    @Override
    public void sendEvalCoachMsg(Long scheId) throws Exception {
        CourseScheduling cs=courseSchedulingService.findOne(scheId);
        if(cs==null){
            throw new Exception("未找到排课信息，排课编号为："+scheId);
        }
        List<LearnerCharge> learners=learnerChargeService.getLeanersBySche(cs);
        for (LearnerCharge learnerCharge : learners) {
            JSONObject param=new JSONObject();
            JSONObject first=new JSONObject();
            first.put("value","您刚刚完成了一节健身课。");
            first.put("color","#000000");
            param.put("first",first);

            JSONObject remark=new JSONObject();
            remark.put("value","猫力健身邀请您对教练进行评价。");
            remark.put("color","#000000");
            param.put("remark",remark);

            JSONObject keyword1 =new JSONObject();
            keyword1.put("value",cs.getCoach().getCoachName());
            keyword1.put("color","#173177");
            param.put("keyword1",keyword1);

            JSONObject keyword2 =new JSONObject();
            keyword2.put("value",cs.getCourse().getCourseName());
            keyword2.put("color","#173177");
            param.put("keyword2",keyword2);

            try{
                String openid=learnerCharge.getLearner().getWechatUser().getOpenId();
                String url=wechathost+"#!/stuevaluate?scheId="+scheId;
                wechatUtil.sendTemplateMsg(openid,url,"_95GX9FsmJS4HmC4MYqHFAeXMrjHPg3iy67yARtXU0U",param);
            }catch (Exception e){
                e.getMessage();
            }
        }
    }

    //下课
    @Override
    public void sendOverClassMsg(Long scheId) throws Exception {
        CourseScheduling cs=courseSchedulingService.findOne(scheId);
        if(cs==null){
            throw new Exception("未找到排课信息，排课编号为："+scheId);
        }
        List<LearnerCharge> learners=learnerChargeService.getLeanersBySche(cs);
        for (LearnerCharge learnerCharge : learners) {
            JSONObject param=new JSONObject();
            JSONObject first=new JSONObject();
            first.put("value","您刚刚完成了一节健身课。");
            first.put("color","#000000");
            param.put("first",first);

            JSONObject remark=new JSONObject();
            remark.put("value","猫力健身邀请您对学员进行填写建议。");
            remark.put("color","#000000");
            param.put("remark",remark);

            JSONObject keyword1 =new JSONObject();
            keyword1.put("value",learnerCharge.getLearner().getLearneName());
            keyword1.put("color","#173177");
            param.put("keyword1",keyword1);

            JSONObject keyword2 =new JSONObject();
            keyword2.put("value",cs.getCourse().getCourseName());
            keyword2.put("color","#173177");
            param.put("keyword2",keyword2);

            try{
                String openid=learnerCharge.getCoach().getCoachWechatopenid();
                String url=wechathost+"#!/coachwriteinfo?scheId="+scheId+"&learnerId="+learnerCharge.getLearner().getId();
                wechatUtil.sendTemplateMsg(openid,url,"_95GX9FsmJS4HmC4MYqHFAeXMrjHPg3iy67yARtXU0U",param);
            }catch (Exception e){
                e.getMessage();
            }

        }
    }

    //买课成功后推送给学员买课成功的消息
    @Override
    public void sendBuyClassSuccess(BuyCourse buyCourse) throws Exception {
        //获取时间
        ZonedDateTime zonedDateTime = buyCourse.getBuyTime();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:ss");
        String time = zonedDateTime.format(dtf);

        String describer = "猫力健身房";

        JSONObject param=new JSONObject();
        JSONObject first=new JSONObject();
        first.put("value","尊敬的会员，您已经成功购买"+buyCourse.getCourse().getCourseName()+"！");
        first.put("color","#000000");
        param.put("first",first);

        JSONObject remark=new JSONObject();
        remark.put("value","猫力健身欢迎你的到来！");
        remark.put("color","#000000");
        param.put("remark",remark);

        //购买日期
        JSONObject keyword1 =new JSONObject();
        keyword1.put("value",time);
        keyword1.put("color","#173177");
        param.put("keyword1",keyword1);

        //项目名称
        JSONObject keyword2 =new JSONObject();
        keyword2.put("value",buyCourse.getCourse().getCourseName());
        keyword2.put("color","#173177");
        param.put("keyword2",keyword2);

        //支付金额
        JSONObject keyword3 = new JSONObject();
        keyword3.put("value",buyCourse.getPaymentAccount()+"元");
        keyword3.put("color","#173177");
        param.put("keyword3",keyword3);

        //支付类型
        JSONObject keyword4 = new JSONObject();
        keyword4.put("value",buyCourse.getPaymentWay());
        keyword4.put("color","#173177");
        param.put("keyword4",keyword4);

        //支付门店
        JSONObject keyword5 = new JSONObject();
        keyword5.put("value",describer);
        keyword5.put("color","#173177");
        param.put("keyword5",keyword5);

        try{
            String openid=buyCourse.getLearner().getWechatUser().getOpenId();
            String url=wechathost+"#!/stucourse";
            wechatUtil.sendTemplateMsg(openid,url,"fsHSuGzowoSVyPyth_2v_39OR6ysm7ItDjv3Wk3_WMg",param);
        }catch (Exception e){
            e.getMessage();
        }

    }
}
