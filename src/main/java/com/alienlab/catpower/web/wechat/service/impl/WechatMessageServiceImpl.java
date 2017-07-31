package com.alienlab.catpower.web.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.domain.LearnerAppointment;
import com.alienlab.catpower.domain.LearnerCharge;
import com.alienlab.catpower.repository.LearnerAppointmentRepository;
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
    LearnerAppointmentRepository learnerAppointmentRepository;

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

    @Override
    public void sendAppointMsg(Long appointmentId) throws Exception {
        LearnerAppointment learnerAppointment=learnerAppointmentRepository.findOne(appointmentId);
        if(learnerAppointment==null){
            throw new Exception("未找到预约信息，预约编号为："+appointmentId);
        }
        JSONObject param=new JSONObject();
        JSONObject first=new JSONObject();
        first.put("value","您收到一条预约信息");
        first.put("color","#000000");
        param.put("first",first);

        JSONObject remark=new JSONObject();
        remark.put("value","您即将对该预约信息进行回复。");
        remark.put("color","#000000");
        param.put("remark",remark);

        JSONObject keyword1 =new JSONObject();
        keyword1.put("value",learnerAppointment.getBuyCourse().getLearner().getLearneName());
        keyword1.put("color","#173177");
        param.put("keyword1",keyword1);

        JSONObject keyword2 =new JSONObject();
        keyword2.put("value",learnerAppointment.getBuyCourse().getCourse().getCourseName());
        keyword2.put("color","#173177");
        param.put("keyword2",keyword2);

        try{
            String openid=learnerAppointment.getBuyCourse().getCoach().getCoachWechatopenid();
            String url=wechathost+"#/coachappoint?appointId="+appointmentId;
            wechatUtil.sendTemplateMsg(openid,url,"_95GX9FsmJS4HmC4MYqHFAeXMrjHPg3iy67yARtXU0U",param);
        }catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public void sendAppointResultMsg(Long appointmentId) throws Exception {
        LearnerAppointment appointment=learnerAppointmentRepository.findOne(appointmentId);
        if(appointment==null){
            throw new Exception("未找到预约信息，预约编号为："+appointmentId);
        }
        JSONObject param=new JSONObject();
        JSONObject first=new JSONObject();
        first.put("value","您收到一条预约回复");
        first.put("color","#000000");
        param.put("first",first);

        JSONObject remark=new JSONObject();
        remark.put("value","回复结果为："+appointment.getAppointmentResult());
        remark.put("color","#000000");
        param.put("remark",remark);

        JSONObject keyword1 =new JSONObject();
        keyword1.put("value",appointment.getBuyCourse().getCoach().getCoachName());
        keyword1.put("color","#173177");
        param.put("keyword1",keyword1);

        JSONObject keyword2 =new JSONObject();
        keyword2.put("value",appointment.getBuyCourse().getCourse().getCourseName());
        keyword2.put("color","#173177");
        param.put("keyword2",keyword2);

        try{
            String openid=appointment.getBuyCourse().getLearner().getWechatUser().getOpenId();
            String url=wechathost+"#/coachappoint?appointId="+appointmentId;
            wechatUtil.sendTemplateMsg(openid,url,"_95GX9FsmJS4HmC4MYqHFAeXMrjHPg3iy67yARtXU0U",param);
        }catch (Exception e){
            e.getMessage();
        }
    }
}
