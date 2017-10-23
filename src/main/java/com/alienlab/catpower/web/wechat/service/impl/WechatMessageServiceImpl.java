package com.alienlab.catpower.web.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.alienlab.catpower.domain.*;
import com.alienlab.catpower.repository.LearnerAppointmentRepository;
import com.alienlab.catpower.service.BuyCourseService;
import com.alienlab.catpower.service.CourseSchedulingService;
import com.alienlab.catpower.service.LearnerChargeService;
import com.alienlab.catpower.service.LearnerService;
import com.alienlab.catpower.web.wechat.service.WechatMessageService;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    LearnerAppointmentRepository learnerAppointmentRepository;

    @Autowired
    WechatUtil wechatUtil;
    @Autowired
    LearnerService learnerService;

    @Autowired
    BuyCourseService buyCourseService;

    @Value("${wechat.host.server}")
    private String serverPath;

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
                //String url=wechathost+"#!/stuevaluate/"+scheId;
                String url="http://"+serverPath+"/wechat/snsapi?router=stuevaluate/"+scheId;
                String link=wechatUtil.getPageAuthUrl(url,"0");
                wechatUtil.sendTemplateMsg(openid,link,"tsDzYlaIlmC4qTdzCwpa_ddNrjEMFuF0uUMltz-u9rc",param);
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
                String url="http://"+serverPath+"/wechat/snsapi?router=coachwriteinfo/"+scheId+"/"+learnerCharge.getLearner().getId();
                String link=wechatUtil.getPageAuthUrl(url,"0");
                wechatUtil.sendTemplateMsg(openid,link,"tsDzYlaIlmC4qTdzCwpa_ddNrjEMFuF0uUMltz-u9rc",param);
            }catch (Exception e){
                e.getMessage();
            }
        }
    }

    @Override
    public void sendAppointMsg(Long appointmentId) throws Exception {

        LearnerAppointment learnerAppointment=learnerAppointmentRepository.findOne(appointmentId);

       // learnerAppointment.getAppointmentDate()
        //获取时间
        ZonedDateTime zonedDateTime =learnerAppointment.getAppointmentDate();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:ss");
        String time = zonedDateTime.format(dtf);
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

        //会员名称
        JSONObject keyword1 =new JSONObject();
        keyword1.put("value",learnerAppointment.getBuyCourse().getLearner().getLearneName());
        keyword1.put("color","#173177");
        param.put("keyword1",keyword1);

        //会员电话
        JSONObject keyword2 =new JSONObject();
        keyword2.put("value",learnerAppointment.getBuyCourse().getLearner().getLearnerPhone());
        keyword2.put("color","#173177");
        param.put("keyword2",keyword2);

        //上课时间
        JSONObject keyword3 =new JSONObject();
        keyword3.put("value",time);
        keyword3.put("color","#173177");
        param.put("keyword3",keyword3);

        try{
            String openid=learnerAppointment.getBuyCourse().getCoach().getCoachWechatopenid();
            //String url=wechathost+"#!/coachappoint/"+appointmentId;
            String url="http://"+serverPath+"/wechat/snsapi?router=coachappoint/"+appointmentId;
            String link=wechatUtil.getPageAuthUrl(url,"0");
            wechatUtil.sendTemplateMsg(openid,link,"JBvVYvsrz8QDMZDU7EuopSM6J3TsbvyxmSTY_8OpOSM",param);
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
        remark.put("value","如有疑问，请及时联系教练。");
        remark.put("color","#000000");
        param.put("remark",remark);

        //姓名
        JSONObject keyword1 =new JSONObject();
        keyword1.put("value",appointment.getBuyCourse().getLearner().getLearneName());
        keyword1.put("color","#173177");
        param.put("keyword1",keyword1);

        //手机
        JSONObject keyword2 =new JSONObject();
        keyword2.put("value",appointment.getBuyCourse().getLearner().getLearnerPhone());
        keyword2.put("color","#173177");
        param.put("keyword2",keyword2);

        //受理时间
        JSONObject keyword3 =new JSONObject();
        keyword3.put("value",appointment.getAppointmentDate());
        keyword3.put("color","#173177");
        param.put("keyword3",keyword3);

        JSONObject keyword4 =new JSONObject();
        keyword4.put("value",appointment.getBuyCourse().getCourse().getCourseName()+appointment.getAppointmentResult());
        keyword4.put("color","#173177");
        param.put("keyword3",keyword4);

        try{
            String openid=appointment.getBuyCourse().getLearner().getWechatUser().getOpenId();
            //String url=wechathost+"#!/coachappoint?appointId="+appointmentId;
            String url="http://"+serverPath+"/wechat/snsapi?router=coachappoint/"+appointmentId;
            String link=wechatUtil.getPageAuthUrl(url,"0");
            wechatUtil.sendTemplateMsg(openid,link,"bvjXHy-ozredYc3bq-fQwMxbUz7EfiPOuyFZAzvYiU0",param);
        }catch (Exception e){
            e.getMessage();
        }
    }


    @Override
    public void sendBuyClassSuccess(BuyCourse buyCourse) throws Exception {
        //获取时间
        ZonedDateTime zonedDateTime = buyCourse.getBuyTime();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:ss");
        String time = zonedDateTime.format(dtf);

        String describer = "猫力健身房";

        JSONObject param=new JSONObject();
        JSONObject first=new JSONObject();
        first.put("value","尊敬的会员，您已经成功购买《"+buyCourse.getCourse().getCourseName()+"》！");
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
            //String url=wechathost+"#!/stucourse";
            String url="http://"+serverPath+"/wechat/snsapi?router=stucourse";
            String link=wechatUtil.getPageAuthUrl(url,"0");
            wechatUtil.sendTemplateMsg(openid,link,"JysTZpNW93ATzGFO046k94Owpa4IeOz5jYX0dCPwRv4",param);
        }catch (Exception e){
            e.getMessage();
        }

    }
}
