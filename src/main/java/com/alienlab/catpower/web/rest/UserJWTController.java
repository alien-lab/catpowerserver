package com.alienlab.catpower.web.rest;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.catpower.domain.User;
import com.alienlab.catpower.security.SecurityUtils;
import com.alienlab.catpower.security.jwt.JWTConfigurer;
import com.alienlab.catpower.security.jwt.TokenProvider;
import com.alienlab.catpower.service.LearnerService;
import com.alienlab.catpower.service.UserService;
import com.alienlab.catpower.web.rest.util.ExecResult;
import com.alienlab.catpower.web.rest.vm.LoginVM;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {
    private final Logger log = LoggerFactory.getLogger(UserJWTController.class);

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final WechatUtil wechatUtil;

    private final UserService userService;

    private final LearnerService learnerService;


    public UserJWTController(
        TokenProvider tokenProvider,
        AuthenticationManager authenticationManager,
        WechatUtil wechatUtil,
        UserService userService,
        LearnerService learnerService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.wechatUtil=wechatUtil;
        this.userService=userService;
        this.learnerService=learnerService;
    }

    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        log.info(">>>>>>>>>>>>>>>>>>>认证<<<<<<<<<<<<<<<<<<");
        log.info("【认证】username:{}",loginVM.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        log.info("【认证】authenticationToken:{}",authenticationToken);
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        log.info("【认证】authentication:{}",authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        log.info("【认证】创建token：{}",jwt);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        log.info(">>>>>>>>>>>>>>>>>>>认证<<<<<<<<<<<<<<<<<<");
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @ApiOperation(value="小程序端登录获取token")
    @GetMapping("/authenticate/wechat/{code}")
    @Timed
    public ResponseEntity authorize(@PathVariable String code) {
        log.info(">>>>>>>>>>>>>>>>>>>小程序认证<<<<<<<<<<<<<<<<<<");
        log.info("【wechat小程序认证】code:{}",code);
        //step1 调用微信服务通过code获取openid
        JSONObject liteResult=wechatUtil.getLiteProgramOpenid(code);
        log.info("liteResult is>>>>"+liteResult.toJSONString());
        if(liteResult.containsKey("errcode")&&liteResult.getInteger("errcode")>0){
            log.error("微信验证失败，code无效，错误信息：{}",liteResult.toJSONString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(liteResult);
        }
        String openid=liteResult.getString("openid");

//        String openid="okM0F0cahqvUTVtWVqFgZEum07Ns";

        //step2 通过openid找到对应的系统账户。
        User u=userService.getUserByOpenid(openid);
        if(u==null){
            log.error("Openid {} 未绑定系统用户。",openid);
            ExecResult er=new ExecResult(false,"微信用户身份未绑定。");
            JSONObject jo=new JSONObject();
            jo.put("openid",openid);
            er.setData(jo);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }


        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(u.getLogin()+","+openid, openid);
        log.info("【认证】authenticationToken:{}",authenticationToken);
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        log.info("【认证】authentication:{}",authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = true;
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        log.info("【认证】创建token：{}",jwt);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        log.info(">>>>>>>>>>>>>>>>>>>认证<<<<<<<<<<<<<<<<<<");
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }


    @ApiOperation(value="小程序端绑定用户")
    @ApiImplicitParams({
        @ApiImplicitParam(name="openid",paramType = "query",value="微信身份编码"),
        @ApiImplicitParam(name="phone",paramType = "query",value="需绑定的手机号码"),
        @ApiImplicitParam(name="randomcode",paramType = "query",value="手机短信验证码")
    })
    @PostMapping("/authenticate/wechat/bind")
    @Timed
    public ResponseEntity authorize(@RequestParam String openid, @RequestParam String phone, @RequestParam String randomcode, @RequestParam String account){
        log.info("wechat authorize>>>openid="+openid+",phone="+phone+",randomcode="+randomcode+",account="+account);
        User u=userService.getUserByLogin(account);
        if(u==null){
            log.error("login {} 找不到系统用户。",account);
            ExecResult er=new ExecResult(false,"平台账户不存在。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        if(openid==null||openid.equals("null")){
            log.error("Openid {} 错误。",openid);
            ExecResult er=new ExecResult(false,"获取用户信息出错。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
//        User openidu=userService.getUserByOpenid(openid);
//        if(openidu!=null){
//            log.error("Openid {} 您已绑定系统用户。",openid);
//            ExecResult er=new ExecResult(false,"已经绑定过用户。");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
//        }

//        BsUsers bsUsers=bsUsersService.findByUserName(u.getLogin());
//        if(bsUsers==null){
//            log.error("login {} 找不到业务用户。",account);
//            ExecResult er=new ExecResult(false,"账户名下不存在业务用户。");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
//        }
//        if(!phone.equals(bsUsers.getMobile())){
//            log.error("login {} 平台账户手机号码错误。",account);
//            ExecResult er=new ExecResult(false,"账户关联的手机号码错误。");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
//        }
//
//
//        if(!UtilsResource.validatMobileCode(phone,randomcode)) { //调用验证码验证
//            log.error("手机验证码错误");
//            ExecResult er = new ExecResult(false, "手机验证码错误");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
//        }


        u.setOpenid(openid);
        u=userService.updateUser(u);
        return ResponseEntity.ok(u);
    }

    @ApiOperation(value="小程序端用户解除绑定")
    @PostMapping("/authenticate/wechat/unbind")
    @Timed
    public ResponseEntity unbindUser(){
        String login= SecurityUtils.getCurrentUserLogin().get();
        User u=userService.getUserByLogin(login);
        if(u==null){
            log.error("login {} 找不到系统用户。",login);
            ExecResult er=new ExecResult(false,"平台账户不存在。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        u.setOpenid(null);
        u=userService.updateUser(u);
        return ResponseEntity.ok(u);
    }


    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
