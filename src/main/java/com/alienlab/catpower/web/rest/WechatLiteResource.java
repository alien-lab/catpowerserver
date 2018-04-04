package com.alienlab.catpower.web.rest;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.alienlab.catpower.domain.*;
import com.alienlab.catpower.security.SecurityUtils;
import com.alienlab.catpower.service.*;
import com.alienlab.catpower.web.rest.util.ExecResult;

import com.alienlab.catpower.web.wechat.util.WechatUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WechatLiteResource {
    private final UserService userService;
    private final LearnerService learnerService;
    private final LearnerChargeService learnerChargeService;
    private final CoachService coachService;
    private final CourseSchedulingService courseSchedulingService;
    private final BuyCourseService buyCourseService;
    private final WechatUtil wechatUtil;
    public WechatLiteResource(
        UserService userService,
        LearnerService learnerService,
        CoachService coachService,
        CourseSchedulingService courseSchedulingService,
        BuyCourseService buyCourseService,
        WechatUtil wechatUtil,
        LearnerChargeService learnerChargeService
    ){
        this.userService=userService;
        this.learnerService=learnerService;
        this.coachService=coachService;
        this.courseSchedulingService=courseSchedulingService;
        this.buyCourseService=buyCourseService;
        this.wechatUtil=wechatUtil;
        this.learnerChargeService=learnerChargeService;
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/wechat/lite/user")
    public ResponseEntity getCurrentUser(){
        String loginname= SecurityUtils.getCurrentUserLogin().get();
        User user=userService.getUserByLogin(loginname);
        if(user==null){
            ExecResult er=new ExecResult(false,"未找到登录用户。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        //根据手机号码获取学员信息
        Learner learner=learnerService.findByPhone(loginname);

        //根据手机号码获取教练信息
        Coach coach=coachService.findCoachByPhone(loginname);
        JSONObject result=new JSONObject();
        result.put("loginUser",user);
        result.put("learner",learner);
        result.put("coach",coach);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "获取当前学员的预约信息")
    @GetMapping("/wechat/appointment/learner")
    public ResponseEntity getLearnerAppointment(){
        String phone=SecurityUtils.getCurrentUserLogin().get();

        //根据手机号码获取学员信息
        Learner learner=learnerService.findByPhone(phone);
        if(learner==null){
            ExecResult er=new ExecResult(false,"未找到学员。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        CourseScheduling result=learnerService.getLearnerAppoint(learner);
        return ResponseEntity.ok(result);

    }
    @ApiOperation(value = "获取学员对应的教练")
    @GetMapping("/wechat/learner/teachers")
    public ResponseEntity getTeachersByLearner(){
        String phone=SecurityUtils.getCurrentUserLogin().get();

        //根据手机号码获取学员信息
        Learner learner=learnerService.findByPhone(phone);
        if(learner==null){
            ExecResult er=new ExecResult(false,"未找到学员。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }

        List<Coach> result=learnerService.findTeachers(learner);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "通过教练查询教练排班，用于预约选择日期")
    @GetMapping("/wechat/teacher/workday/{teacherId}")
    public ResponseEntity getWorkDayByTeacher(@PathVariable Long teacherId){
        Coach coach=coachService.findOne(teacherId);
        if(coach==null){
            ExecResult er=new ExecResult(false,"未找到教练。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        List<String> result=coachService.getWorkDates(coach);
        return ResponseEntity.ok(result);
    }
    @ApiOperation(value = "通过教练查询教练排班，用于预约选择时间")
    @GetMapping("/wechat/teacher/freetime/{teacherId}")
    public ResponseEntity getFreeTime(@PathVariable Long teacherId, @RequestParam String date){
        Coach coach=coachService.findOne(teacherId);
        if(coach==null){
            ExecResult er=new ExecResult(false,"未找到教练。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        List<String> result=coachService.getFreeTimes(coach,date);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "当前用户取消预约")
    @PutMapping("/wechat/learner/cancelappoint")
    public ResponseEntity cancelAppoint(){
        String phone=SecurityUtils.getCurrentUserLogin().get();
        //根据手机号码获取学员信息
        Learner learner=learnerService.findByPhone(phone);
        if(learner==null){
            ExecResult er=new ExecResult(false,"未找到学员。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        CourseScheduling appoint=learnerService.getLearnerAppoint(learner);
        if(appoint==null){
            ExecResult er=new ExecResult(false,"未找到预约信息。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        try {
            appoint=courseSchedulingService.changeStatus(appoint.getId(),"已取消");
            return ResponseEntity.ok(appoint);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,"取消预约发生错误。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
    }

    @ApiOperation(value = "当前用户确认预约")
    @PutMapping("/wechat/learner/confirmappoint")
    public ResponseEntity confirmAppoint(){
        String phone=SecurityUtils.getCurrentUserLogin().get();
        //根据手机号码获取学员信息
        Learner learner=learnerService.findByPhone(phone);
        if(learner==null){
            ExecResult er=new ExecResult(false,"未找到学员。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        CourseScheduling appoint=learnerService.getLearnerAppoint(learner);
        if(appoint==null){
            ExecResult er=new ExecResult(false,"未找到预约信息。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        try {
            appoint=courseSchedulingService.changeStatus(appoint.getId(),"已预约");
            return ResponseEntity.ok(appoint);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,"确认预约发生错误。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
    }

    @ApiOperation(value = "获取教练预约")
    @GetMapping("/wechat/teacher/appoint")
    public ResponseEntity getTeacherAppoint(){
        String phone=SecurityUtils.getCurrentUserLogin().get();
        Coach coach=coachService.findCoachByPhone(phone);
        if(coach==null){
            ExecResult er=new ExecResult(false,"未找到教练。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        List<CourseScheduling> appoints=courseSchedulingService.getAllAppointByCoach(coach);
        List<BuyCourse> buyCourses=buyCourseService.getHasClassLearnerByCoach(coach);
        List<Learner> unappointLearvers=new ArrayList<>();
        List<CourseScheduling> today=new ArrayList<>();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        String date=simpleDateFormat.format(new Date());
        for (CourseScheduling appoint : appoints) {
            if(appoint.getAppointDate().equals(date)){
                today.add(appoint);
            }
        }
        for (BuyCourse buyCours : buyCourses) {
            Learner l=buyCours.getLearner();
            boolean isadd=false;
            for(int i=0;i<unappointLearvers.size();i++){
                if(l.getId()==unappointLearvers.get(i).getId()){
                    break;
                }
                isadd=true;
            }
            if(isadd){
                unappointLearvers.add(l);
            }
        }
        JSONObject result=new JSONObject();
        result.put("allappoint",appoints);
        result.put("today",today);
        result.put("unlearners",unappointLearvers);
        result.put("unbuycourse",buyCourses);
        return ResponseEntity.ok(result);
    }

//    @ApiOperation(value = "获取课程签到二维码")
//    @PostMapping("/wechat/qr")
//    public ResponseEntity getLiteprogramQr(@RequestBody Map param){
//        String page= TypeUtils.castToString(param.get("path"));
//        String scheId=TypeUtils.castToString(param.get("scheId"));
//        String params="scheId="+scheId;
//        JSONObject qr=wechatUtil.getLiteProgramQrcode(page,params);
//        return ResponseEntity.ok(qr);
//    }

    @ApiOperation(value = "学员扫码后开始上课")
    @PostMapping("/wechat/startcourse")
    public ResponseEntity startCourse(@RequestBody Map signInfo){
        String phone=SecurityUtils.getCurrentUserLogin().get();
        //根据手机号码获取学员信息
        Learner learner=learnerService.findByPhone(phone);
        if(learner==null){
            ExecResult er=new ExecResult(false,"未找到学员。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        Long scheId=TypeUtils.castToLong(signInfo.get("scheId"));
        String formId=TypeUtils.castToString(signInfo.get("formId"));
        CourseScheduling sche=courseSchedulingService.findOne(scheId);
        try {
            sche.setWechatLiteFormid(formId);
            LearnerCharge sign=learnerChargeService.learnSign(learner,sche);
            return ResponseEntity.ok(sign);
        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
    }

    @ApiOperation(value = "获取学员正在进行中的课")
    @GetMapping("/wechat/learner/onlinecourse")
    public ResponseEntity getLearnerOnlineCourse(){
        String phone=SecurityUtils.getCurrentUserLogin().get();
        //根据手机号码获取学员信息
        Learner learner=learnerService.findByPhone(phone);
        if(learner==null){
            ExecResult er=new ExecResult(false,"未找到学员。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        CourseScheduling result= courseSchedulingService.getLearnerOnlineCourse(learner);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "获取教练正在进行中的课")
    @GetMapping("/wechat/teacher/onlinecourse")
    public ResponseEntity getTeacherOnlineCourse(){
        String phone=SecurityUtils.getCurrentUserLogin().get();
        Coach coach=coachService.findCoachByPhone(phone);
        if(coach==null){
            ExecResult er=new ExecResult(false,"未找到教练。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        CourseScheduling result=courseSchedulingService.getTeacherOnlineCourse(coach);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "获取预约或上课信息")
    @GetMapping("/wechat/sche/{scheId}")
    public ResponseEntity getSche(@PathVariable Long scheId){
        CourseScheduling sche=courseSchedulingService.findOne(scheId);
        return ResponseEntity.ok(sche);
    }










}
