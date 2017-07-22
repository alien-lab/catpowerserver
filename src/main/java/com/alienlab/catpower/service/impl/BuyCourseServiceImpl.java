package com.alienlab.catpower.service.impl;

import com.alienlab.catpower.domain.*;
import com.alienlab.catpower.repository.*;
import com.alienlab.catpower.service.BuyCourseService;
import com.alienlab.catpower.service.LearnerService;
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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service Implementation for managing BuyCourse.
 */
@Service
@Transactional
public class BuyCourseServiceImpl implements BuyCourseService{



    private final Logger log = LoggerFactory.getLogger(BuyCourseServiceImpl.class);

    private final BuyCourseRepository buyCourseRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    private LearnerService learnerService;

    @Autowired
    private LearnerAppointmentRepository learnerAppointmentRepository;

    @Autowired
    private LearnerRepository learnerRepository;

    @Autowired
    private CoachRepository coachRepository;

    public BuyCourseServiceImpl(BuyCourseRepository buyCourseRepository) {
        this.buyCourseRepository = buyCourseRepository;
    }

    /**
     * Save a buyCourse.
     *
     * @param buyCourse the entity to save
     * @return the persisted entity
     */
    @Override
    public BuyCourse save(BuyCourse buyCourse) {
        log.debug("Request to save BuyCourse : {}", buyCourse);
        ZonedDateTime dateTime = ZonedDateTime.now();
        buyCourse.setBuyTime(dateTime);
        buyCourse.setOperateTime(dateTime);
        BuyCourse result = buyCourseRepository.save(buyCourse);
        return result;
    }

    /**
     *  Get all the buyCourses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BuyCourse> findAll(Pageable pageable) {
        log.debug("Request to get all BuyCourses");
        Page<BuyCourse> result = buyCourseRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one buyCourse by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BuyCourse findOne(Long id) {
        log.debug("Request to get BuyCourse : {}", id);
        BuyCourse buyCourse = buyCourseRepository.findOne(id);
        return buyCourse;
    }

    /**
     *  Delete the  buyCourse by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BuyCourse : {}", id);
        buyCourseRepository.delete(id);
    }

    @Override
    public Page<BuyCourse> getTodayData(Pageable page) throws Exception{
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today=format.format(new Date());
        String t1=today+"T00:00:00Z";
        String t2=today+"T23:59:59Z";

        return buyCourseRepository.findBuyCourseByBuyTimeBetweenOrderByBuyTimeDesc(
            ZonedDateTime.parse(t1),ZonedDateTime.parse(t2)
            ,page);
    }

    @Override
    public Map getTodayCountByDate(Date date) throws ParseException {
        SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd=sf.format(date);
        sd=sd.substring(0,8)+"000000";
        Date d1= null,d2=null;
        d1 = sf.parse(sd);
        d2=new Date(d1.getTime()+1000*60*60*24);
        String startDate=sf1.format(d1);
        String endDate=sf1.format(d2);

        String sql = "SELECT tb1.buycount,tb2.buymenoy FROM(\n" +
            "SELECT 1 f, COUNT(DISTINCT learner_id) buycount FROM `buy_course` WHERE buy_time>='"+startDate+"' AND buy_time<='"+endDate+"'\n" +
            ") tb1,(\n" +
            "SELECT 1 f, SUM(payment_account) buymenoy FROM `buy_course` WHERE buy_time>='"+startDate+"' AND buy_time<='"+endDate+"'\n" +
            ") tb2\n" +
            "WHERE tb1.f=tb2.f";

        return jdbcTemplate.queryForMap(sql);
    }

    @Override
    public BuyCourse getCourseByLeanerAndCourse(Learner learner, Long courseId) throws Exception {
        Course course=courseRepository.findOne(courseId);
        if(course==null){
            throw new Exception("未找到课程，课程编码："+courseId);
        }
        return buyCourseRepository.findBuyCourseByLearnerAndCourse(learner,course);
    }

    @Override
    public List getAllCoachByLearnerId(Long learnerId) throws Exception {
        if (learnerId==null){
            throw new Exception("请求错误："+learnerId);
        }
        List list=new ArrayList();
        List<BuyCourse> coachList=buyCourseRepository.findCoachByLearner(learnerId);
        for (int i=0;i<coachList.size();i++){
            Map map=new HashMap();
            BuyCourse buyCourse=coachList.get(i);
            Coach coach=buyCourse.getCoach();
            List<BuyCourse> courses=buyCourseRepository.findCourseByCoach(coach,learnerId);
            map.put("coach",coach);
            map.put("courses",courses);
            list.add(map);
        }
        return list;
    }

    //查询学员的全部课程
    @Override
    public List<BuyCourse> findBuyCourseByLearnerId(Long learnerId) throws Exception {
        if (learnerId==null){
            throw new Exception("请求错误："+learnerId);
         }
        Learner learner = learnerRepository.findOne(learnerId);
        if (learner==null){
            throw new Exception("没有找到该学员信息");
        }
        return buyCourseRepository.findBuyCourseByLearner(learner);
    }

    //查询学员的可用课程
    @Override
    public List<BuyCourse> findUseBuyCourseByLearnerId(Long learnerId) throws Exception {
        if (learnerId==null){
            throw new Exception("请求错误："+learnerId);
        }
        Learner learner = learnerRepository.findOne(learnerId);
        if (learner==null){
            throw new Exception("没有找到该学员信息");
        }
        List<BuyCourse> buyCourses = buyCourseRepository.findBuyCourseByLearner(learner);
        List<BuyCourse> buyCoursesList = new ArrayList<BuyCourse>();
        for (BuyCourse buyCourse :buyCourses){
            if (buyCourse.getRemainClass()>0){
                buyCoursesList.add(buyCourse);
            }
        }
        return buyCoursesList;
    }

    //查询学员的不可用课程
    @Override
    public List<BuyCourse> findNotUseBuyCourseByLearnerId(Long learnerId) throws Exception {
        if (learnerId==null ){
            throw new Exception("请求错误："+learnerId);
        }
        Learner learner = learnerRepository.findOne(learnerId);
        if (learner==null){
            throw new Exception("没有找到该学员信息");
        }
        List<BuyCourse> buyCourses = buyCourseRepository.findBuyCourseByLearner(learner);
        List<BuyCourse> buyCoursesList = new ArrayList<BuyCourse>();
        for (BuyCourse buyCourse :buyCourses){
            if (buyCourse.getRemainClass()<=0){
                buyCoursesList.add(buyCourse);
            }
        }
        return buyCoursesList;
    }

    @Override
    public List<BuyCourse> getPaymentWay() throws Exception {
        List<BuyCourse> list = buyCourseRepository.findBuyCourse();
        return list;
    }

    @Override
    public List getCoachCourseByCoachId(Long coachId) throws Exception {
        String sql = "SELECT * FROM `buy_course` a,`course` b WHERE a.coach_id='"+coachId+"' AND a.course_id=b.id GROUP BY a.course_id";
        List list=jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List getLearnerByCoachId(Long coachId) throws Exception {
        if (coachId==null ){
            throw new Exception("请求错误："+coachId);
        }
        List<BuyCourse> buyCourses = buyCourseRepository.getBuyCourseByCoachId(coachId);
        List list = new ArrayList();
        for (BuyCourse buyCourse : buyCourses){
            Map map = new HashMap();
            List<BuyCourse> courses = buyCourseRepository.findCourseByCoach(buyCourse.getCoach(),buyCourse.getLearner().getId());
            map.put("learner",buyCourse.getLearner());
            map.put("courses",courses);
            list.add(map);
        }
        return list;
    }


}
