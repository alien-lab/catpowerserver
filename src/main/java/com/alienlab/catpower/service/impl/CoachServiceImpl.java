package com.alienlab.catpower.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.repository.CoachRepository;
import com.alienlab.catpower.service.CoachService;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Service Implementation for managing Coach.
 */
@Service
@Transactional
public class CoachServiceImpl implements CoachService{

    private final Logger log = LoggerFactory.getLogger(CoachServiceImpl.class);

    private final CoachRepository coachRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    CoachService coachService;
    @Autowired
    WechatUtil wechatUtil;
    @Autowired
    QrInfoService qrInfoService;
    @Autowired
    WechatUserService wechatUserService;

    public CoachServiceImpl(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    /**
     * Save a coach.
     *
     * @param coach the entity to save
     * @return the persisted entity
     */
    @Override
    public Coach save(Coach coach) {
        log.debug("Request to save Coach : {}", coach);
        Coach result = coachRepository.save(coach);
        return result;
    }

    /**
     *  Get all the coaches.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Coach> findAll(Pageable pageable) {
        log.debug("Request to get all Coaches");
        Page<Coach> result = coachRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one coach by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Coach findOne(Long id) {
        log.debug("Request to get Coach : {}", id);
        Coach coach = coachRepository.findOne(id);
        return coach;
    }

    /**
     *  Delete the  coach by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Coach : {}", id);
        coachRepository.delete(id);
    }

    @Override
    public Map getCoachByCoachId(Long id) throws Exception {

       /* String sql = "SELECT a.complain,a.evaluation,b.coach_name,b.coach_phone,b.coach_introduce,b.coach_picture,c.course_id FROM `coach_evaluate` a,`coach` b, `buy_course` c\n" +
            "WHERE b.id='"+id+"' AND c.coach_id='"+id+"' AND a.coach_id=b.id AND b.id=c.coach_id ";
        */

        String sql1 = "SELECT * FROM `coach` a WHERE a.id='"+id+"'";
        String sql2 = "SELECT * FROM `coach_evaluate` b WHERE b.`coach_id`='"+id+"'";
        String sql3 = "SELECT DISTINCT c.course_name FROM course c,buy_course b WHERE b.coach_id = '"+id+"' AND b.`course_id` = c.id";

        Map<String ,Object> map = new HashMap<String ,Object>();
        map.put("coachBase",jdbcTemplate.queryForMap(sql1));
        map.put("courseCharge",jdbcTemplate.queryForMap(sql2));
        map.put("courseInfo",jdbcTemplate.queryForMap(sql3));
        return map;
    }

    @Override
    public QrInfo getCoachBindQr(Coach coach) throws Exception {
        if(coach==null){
            throw new Exception("未找到教练信息。");
        }
        if(coach.getQrInfo() != null){
            return coach.getQrInfo();
        }
        String sceneId = "3and" + coach.getId() + "";
        JSONObject jo = wechatUtil.get_qr_code_ticket(sceneId);
        if( jo == null || jo.getString("ticket") == null){
            throw new Exception("生成绑定教练二维码失败！教练绑定编码："+ coach.getId());
        }
        QrInfo qr = qrInfoService.createQrinfo(sceneId,3L,jo.getString("ticket"));
        coach.setQrInfo(qr);
        coachRepository.save(coach);
        return qr;
    }

    @Override
    public Coach bindWechatUser(WechatUser wechatUser, Coach coach) throws Exception {
        Coach coach1 = coachRepository.findCoachBycoachWechatopenid(coach.getCoachWechatopenid());
        System.out.println(coach1);
        if (coach1 != null){
            throw new Exception("教练账户" + coach.getCoachName() + "已被" + coach.getCoachWechatname() + "绑定");
        }
       /*if(coach.getWechatUser() != null){
            throw new Exception("教练账户" + coach.getCoachName() + "已被" + coach.getWechatUser().getNickName() + "绑定");
        }*/
        coach.setCoachWechatopenid(wechatUser.getOpenId());
        coach.setCoachWechatname(wechatUser.getNickName());
        coach.setCoachWechatpicture(wechatUser.getIcon());
        return coachRepository.save(coach);
    }

    @Override
    public Coach bindWechatUser(String openid, Long coachId) throws Exception {
        WechatUser user=wechatUserService.findUserByOpenid(openid);
        if(user==null){
            throw new Exception("未找到关联的微信用户，用户openid:"+openid);
        }
        Coach coach = coachRepository.findOne(coachId);
        if (coach == null){
            throw new Exception("未找到已注册的教练信息，教练编码：" + coachId);
        }
        return bindWechatUser(user,coach);
    }
}
