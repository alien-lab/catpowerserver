package com.alienlab.catpower.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.domain.CourseScheduling;
import com.alienlab.catpower.repository.CoachRepository;
import com.alienlab.catpower.repository.CourseSchedulingRepository;
import com.alienlab.catpower.service.CoachService;
import com.alienlab.catpower.service.CourseSchedulingService;
import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;
import com.alienlab.catpower.web.wechat.service.QrInfoService;
import com.alienlab.catpower.web.wechat.service.WechatMessageService;
import com.alienlab.catpower.web.wechat.util.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing CourseScheduling.
 */
@Service
@Transactional
public class CourseSchedulingServiceImpl implements CourseSchedulingService{

    private final Logger log = LoggerFactory.getLogger(CourseSchedulingServiceImpl.class);

    private final CourseSchedulingRepository courseSchedulingRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CoachService coachService;

    @Autowired
    WechatUtil wechatUtil;

    @Autowired
    QrInfoService qrInfoService;

    @Autowired
    CoachRepository coachRepository;

    public CourseSchedulingServiceImpl(CourseSchedulingRepository courseSchedulingRepository) {
        this.courseSchedulingRepository = courseSchedulingRepository;
    }

    /**
     * Save a courseScheduling.
     *
     * @param courseScheduling the entity to save
     * @return the persisted entity
     */
    @Override
    public CourseScheduling save(CourseScheduling courseScheduling) throws Exception {
        log.debug("Request to save CourseScheduling : {}", courseScheduling);
        //保存之前做时间验证

        List<CourseScheduling> existsResult=courseSchedulingRepository.findByAppointDateAndCoachAndAppointTime(
            courseScheduling.getAppointDate(),courseScheduling.getCoach(),courseScheduling.getAppointTime()
        );
        if(existsResult!=null&&existsResult.size()>0){
            throw new Exception("时间段"+courseScheduling.getAppointTime()+"已经被预约。");
        }
        courseScheduling.setSignInCount(0l);

        CourseScheduling result = courseSchedulingRepository.save(courseScheduling);
        return result;
    }

    /**
     *  Get all the courseSchedulings.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseScheduling> findAll(Pageable pageable) {
        log.debug("Request to get all CourseSchedulings");
        Page<CourseScheduling> result = courseSchedulingRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one courseScheduling by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CourseScheduling findOne(Long id) {
        log.debug("Request to get CourseScheduling : {}", id);
        CourseScheduling courseScheduling = courseSchedulingRepository.findOne(id);
        return courseScheduling;
    }

    /**
     *  Delete the  courseScheduling by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseScheduling : {}", id);
        courseSchedulingRepository.delete(id);
    }

    @Override
    public List<CourseScheduling> getScheByDate(ZonedDateTime startDate, ZonedDateTime endDate) throws Exception {
        return courseSchedulingRepository.findCourseSchedulingsByStartTimeBetween(startDate,endDate);
    }

    @Override
    public Page<CourseScheduling> getScheByDate(ZonedDateTime startDate, ZonedDateTime endDate, int index, int size) throws Exception {
        return courseSchedulingRepository.findCourseSchedulingsByStartTimeBetween(startDate,endDate,new PageRequest(index,size));
    }

    /**
     *根据ID更新上课的状态
     *
     */

    @Autowired
    WechatMessageService wechatMessageService;

    @Override
    public int updateCourseScheduling(Long id,String status) throws Exception {
        /*int n = 0;
        String sql = "UPDATE `course_scheduling` SET STATUS=? WHERE id=?";
        Object[] args = {status,id};
        n = jdbcTemplate.update(sql,args);
        if(n>0&&status.equals("已下课")){
            //下课时发送消息推送
            wechatMessageService.sendEvalCoachMsg(id);
            wechatMessageService.sendOverClassMsg(id);
        }
        return n;*/
        return 0;
    }

    @Override
    public QrInfo getScheQrcode(Long scheId) throws Exception {
        CourseScheduling sche=courseSchedulingRepository.findOne(scheId);
        if(sche==null){
            throw new Exception("没有找到排课信息，排课编码："+scheId);
        }
        if(sche.getStatus().equals("已下课")){
            throw new Exception("课程已下课，排课编码："+scheId);
        }
        //如果已经生成，即直接返回
        if(sche.getQrCode()!=null){
            return sche.getQrCode();
        }
        //如果当前员工
        String sceneId = "1and"+sche.getId()+"";
        JSONObject jo =  wechatUtil.get_qr_code_ticket(sceneId);
        if(jo==null || jo.getString("ticket")==null){
            throw new Exception("生成签到二维码失败！排课编码："+sche.getId());
        }
        QrInfo qr=qrInfoService.createQrinfo(sceneId, 1L,jo.getString("ticket"));
        sche.setQrCode(qr);
        courseSchedulingRepository.save(sche);
        return qr;
    }
    /**
     * 根据教练id获取排课记录
     */
    @Override
    public List<CourseScheduling> getcourseSche(Long coachId) throws Exception {
        Coach coach = coachService.findOne(coachId);

        List<CourseScheduling> result = courseSchedulingRepository.findCourseSchedulingsByCoach(coach);
        return result;
    }

    @Override
    public List getCourseScheduling() throws Exception {
        List<Coach> coachList = coachRepository.findAll();
        List list = new ArrayList();
        for (int i=0;i<coachList.size();i++){
            Coach coach = coachList.get(i);
            List<CourseScheduling> courseSchedulingList=courseSchedulingRepository.findCourseSchedulingsByCoach(coach);
            Map map = new HashMap();
            map.put("coach",coach);
            map.put("courseSchedulingList",courseSchedulingList);
            list.add(map);
        }
        return list;
    }

    @Override
    public CourseScheduling updateEndTime(Long scheId) throws Exception {
        CourseScheduling courseScheduling=courseSchedulingRepository.findOne(scheId);
        if (courseScheduling == null){
            throw new Exception();
        }
        ZonedDateTime endTime = ZonedDateTime.now();
        courseScheduling.setEndTime(endTime);
        courseScheduling.setStatus("已下课");
        CourseScheduling result = courseSchedulingRepository.save(courseScheduling);
        if(scheId != null){
            //下课时发送消息推送
            wechatMessageService.sendEvalCoachMsg(scheId);
            wechatMessageService.sendOverClassMsg(scheId);
        }
        return result;
    }

    //根据教练ID和开始时间获取当天的排课记录
    @Override
    public List<CourseScheduling> findCourseSchedulingByCoachIdAndStartTime(Long coachId, ZonedDateTime startTime) throws Exception {
        if (coachId == null){
            throw new Exception("参数解析异常！");
        }
        Coach coach = coachRepository.findOne(coachId);
        if (coach == null){
            throw new Exception("没有找到该教练的信息！");
        }
        List<CourseScheduling> list = courseSchedulingRepository.findCourseSchedulingsByCoachAndStartTime(coach,startTime);
        return list;
    }

    @Override
    public Page<CourseScheduling> LikeSche(String keyword, Pageable pageable) throws Exception {
        if(keyword == null){
            throw new Exception("未输入关键字");
        }
        return courseSchedulingRepository.findCourseSchedulingByCourseNameLike("%"+keyword+"%",pageable);
    }

    //根据时间查询教练排班情况
    @Override
    public Page<CourseScheduling> getScheByTime(ZonedDateTime startDate, ZonedDateTime endDate, Pageable pageable) throws Exception {
        if(startDate == null){
            throw new Exception("未选择开始时间");
        }else if (endDate == null){
            throw new Exception("未选择结束时间时间");
        }
        return courseSchedulingRepository.findCourseSchedulingsByStartTimeBetween(startDate,endDate,pageable);
    }

    @Override
    public CourseScheduling changeStatus(Long scheId, String status) throws Exception {
        CourseScheduling sche=courseSchedulingRepository.findOne(scheId);
        if(sche==null){
            throw new Exception("没有找到课程预约");
        }

        sche.setStatus(status);
        sche=courseSchedulingRepository.save(sche);
        return sche;
    }


}
