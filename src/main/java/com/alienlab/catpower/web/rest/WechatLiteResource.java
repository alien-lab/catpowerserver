package com.alienlab.catpower.web.rest;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.domain.User;
import com.alienlab.catpower.security.SecurityUtils;
import com.alienlab.catpower.service.CoachService;
import com.alienlab.catpower.service.CourseSchedulingService;
import com.alienlab.catpower.service.LearnerService;
import com.alienlab.catpower.service.UserService;
import com.alienlab.catpower.web.rest.util.ExecResult;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WechatLiteResource {
    private final UserService userService;
    private final LearnerService learnerService;
    private final CoachService coachService;
    private final CourseSchedulingService courseSchedulingService;
    public WechatLiteResource(
        UserService userService,
        LearnerService learnerService,
        CoachService coachService,
        CourseSchedulingService courseSchedulingService
    ){
        this.userService=userService;
        this.learnerService=learnerService;
        this.coachService=coachService;
        this.courseSchedulingService=courseSchedulingService;
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
    @ApiOperation(value = "通过教练查询教练排班，用于预约选择日期")
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







}
