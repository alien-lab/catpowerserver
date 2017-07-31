package com.alienlab.catpower.web.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.domain.LearnerCharge;
import com.alienlab.catpower.service.CourseSchedulingService;
import com.alienlab.catpower.service.LearnerChargeService;
import com.alienlab.catpower.web.wechat.service.WechatMessageService;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
                String url=wechathost+"#/stuevaluate?scheId="+scheId;
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
                String url=wechathost+"#/coachwriteinfo?scheId="+scheId+"&learnerId="+learnerCharge.getLearner().getId();
                wechatUtil.sendTemplateMsg(openid,url,"_95GX9FsmJS4HmC4MYqHFAeXMrjHPg3iy67yARtXU0U",param);
            }catch (Exception e){
                e.getMessage();
            }

        }


    }
}
