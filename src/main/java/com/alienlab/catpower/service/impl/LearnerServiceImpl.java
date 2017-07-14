package com.alienlab.catpower.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.catpower.domain.Learner;
import com.alienlab.catpower.repository.BuyCourseRepository;
import com.alienlab.catpower.repository.LearnerAppointmentRepository;
import com.alienlab.catpower.repository.LearnerRepository;
import com.alienlab.catpower.service.BuyCourseService;
import com.alienlab.catpower.service.LearnerAppointmentService;
import com.alienlab.catpower.service.LearnerService;
import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import com.alienlab.catpower.web.wechat.service.QrInfoService;
import com.alienlab.catpower.web.wechat.service.WechatUserService;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service Implementation for managing Learner.
 */
@Service
@Transactional
public class LearnerServiceImpl implements LearnerService{

    private final Logger log = LoggerFactory.getLogger(LearnerServiceImpl.class);

    private final LearnerRepository learnerRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    WechatUserService wechatUserService;

    @Autowired
    WechatUtil wechatUtil;

    @Autowired
    QrInfoService qrInfoService;

    @Autowired
    LearnerAppointmentRepository learnerAppointmentRepository;

    @Autowired
    BuyCourseRepository buyCourseRepository;

    public LearnerServiceImpl(LearnerRepository learnerRepository) {
        this.learnerRepository = learnerRepository;
    }

    /**
     * Save a learner.
     *
     * @param learner the entity to save
     * @return the persisted entity
     */
    @Override
    public Learner save(Learner learner) {
        log.debug("Request to save Learner : {}", learner);
        Learner result = learnerRepository.save(learner);
        return result;
    }

    /**
     *  Get all the learners.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Learner> findAll(Pageable pageable) {
        log.debug("Request to get all Learners");
        Page<Learner> result = learnerRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one learner by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Learner findOne(Long id) {
        log.debug("Request to get Learner : {}", id);
        Learner learner = learnerRepository.findOne(id);
        return learner;
    }

    /**
     *  Delete the  learner by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Learner : {}", id);
        learnerRepository.delete(id);
    }


    @Override
    public Map learnCountStatiscByDate(Date date) throws ParseException {
        SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd=sf.format(date);
        sd=sd.substring(0,8)+"000000";
        Date d1= null,d2=null;
        d1 = sf.parse(sd);
        d2=new Date(d1.getTime()+1000*60*60*24);
        String startDate=sf1.format(d1);
        String endDate=sf1.format(d2);
        String sql="SELECT tb1.sigincount,tb2.regcount FROM ( " +
            "SELECT 1 f, COUNT(learner_id) sigincount FROM `learner_charge` " +
            "WHERE charge_time>='"+startDate+"' AND charge_time<='"+endDate+"' " +
            ") tb1,( " +
            "SELECT 1 f,COUNT(DISTINCT id) regcount FROM `learner`  " +
            "WHERE regist_time>='"+startDate+"' AND regist_time<='"+endDate+"'  " +
            ") tb2 " +
            "WHERE tb1.f=tb2.f";

        return jdbcTemplate.queryForMap(sql);

    }

    @Override
    public Learner findByOpenid(String openid) throws ParseException {
        return learnerRepository.findLearnerByOpenid(openid);
    }

    @Override
    public QrInfo getLearnerBindQr(Long learnerId) throws Exception {
        Learner learner=learnerRepository.findOne(learnerId);
        return getLearnerBindQr(learner);
    }

    @Override
    public QrInfo getLearnerBindQr(Learner learner) throws Exception {
        if(learner==null){
            throw new Exception("未找到学员注册信息。");
        }
        if(learner.getQrInfo()!=null){
            return learner.getQrInfo();
        }
        //如果当前员工
        String sceneId = "2and"+learner.getId()+"";
        JSONObject jo =  wechatUtil.get_qr_code_ticket(sceneId);
        if(jo==null || jo.getString("ticket")==null){
            throw new Exception("生成学员绑定二维码失败！排课编码："+learner.getId());
        }
        QrInfo qr=qrInfoService.createQrinfo(sceneId, 2L,jo.getString("ticket"));
        learner.setQrInfo(qr);
        learnerRepository.save(learner);
        return qr;
    }

    @Override
    public Learner bindWechatUser(String openid, Long learnerId) throws Exception {
        WechatUser user=wechatUserService.findUserByOpenid(openid);
        if(user==null){
            throw new Exception("未找到关联的微信用户，用户openid:"+openid);
        }
        Learner learner=learnerRepository.findOne(learnerId);
        if(learner==null){
            throw new Exception("未找到已注册的学员信息，学员编码:"+learnerId);
        }
        return bindWechatUser(user,learner);
    }

    @Override
    public Learner bindWechatUser(WechatUser wechatUser, Learner learner) throws Exception {
        if(learner.getWechatUser()!=null){
            if(!learner.getWechatUser().getOpenId().equals(wechatUser.getOpenId())){
                throw new Exception("学员账户"+learner.getLearneName()+"已被"+learner.getWechatUser().getNickName()+"绑定");
            }
        }
        learner.setWechatUser(wechatUser);
        return learnerRepository.save(learner);
    }

    @Override
    public Map getLearnerIndexInfo(String openid) throws Exception {
        Learner learner=learnerRepository.findLearnerByOpenid(openid);
        Long learnerId=learner.getId();
        int appointCount=learnerAppointmentRepository.findAppointingByLearner(learnerId).size();
        int courseCount=buyCourseRepository.findCourseByLearner(learnerId).size();
        int coachCount=buyCourseRepository.findCoachByLearner(learnerId).size();
        Map map=new HashMap();
        map.put("learner",learner);
        map.put("appointCount",appointCount);
        map.put("courseCount",courseCount);
        map.put("coachCount",coachCount);
        return map;
    }

}
