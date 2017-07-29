package com.alienlab.catpower.service.impl;

import com.alibaba.fastjson.util.TypeUtils;
import com.alienlab.catpower.domain.Coach;
import com.alienlab.catpower.domain.CoachWorkSche;
import com.alienlab.catpower.repository.CoachRepository;
import com.alienlab.catpower.repository.CoachWorkScheRespository;
import com.alienlab.catpower.service.CoachService;
import com.alienlab.catpower.service.CoachWorkScheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * user :小乐爷
 * time :2017/7/22
 */
@Service
@Transactional
public class CoachWorkScheServiceImpl implements CoachWorkScheService {
    private final Logger log = LoggerFactory.getLogger(CoachWorkScheService.class);

    private final CoachWorkScheRespository coachWorkScheRespository;

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    CoachWorkScheService coachWorkScheService;
    @Autowired
    CoachService coachService;
    @Autowired
    CoachRepository coachRepository;

    public CoachWorkScheServiceImpl(CoachWorkScheRespository coachWorkScheRespository) {
        this.coachWorkScheRespository = coachWorkScheRespository;
    }

    /**
     * Save a coach.
     *
     * @param coachWorkSche the entity to save
     * @return the persisted entity
     */
    @Override
    public CoachWorkSche save(CoachWorkSche coachWorkSche) {
        log.debug("Request to save Coach : {}", coachWorkSche);
        ZonedDateTime dateTime = ZonedDateTime.now();
        coachWorkSche.setWorkDate(dateTime);
        CoachWorkSche result = coachWorkScheRespository.save(coachWorkSche);
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
    public Page<CoachWorkSche> findAll(Pageable pageable) {
        log.debug("Request to get all Coaches");
        Page<CoachWorkSche> result = coachWorkScheRespository.findAll(pageable);
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
    public CoachWorkSche findOne(Long id) {
        log.debug("Request to get Coach : {}", id);
        CoachWorkSche coach = coachWorkScheRespository.findOne(id);
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
        coachWorkScheRespository.delete(id);
    }

    //根据教练的ID查询对应的排班时间
    @Override
    public List<CoachWorkSche> findCoachWorkScheByCoachId(Long coachId) throws Exception{
        if(coachId == null){
            throw new Exception("参数异常");
        }
        Coach coach = coachService.findOne(coachId);
        if (coach == null){
            throw  new Exception("没有找到对应的教练信息");
        }
        List<CoachWorkSche> lists = coachWorkScheRespository.findCoachWorkScheByCoach(coach);
        Date date  = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 =df.parse(TypeUtils.castToString(date.toInstant()));
        List<CoachWorkSche> coachScheLists =  new ArrayList<CoachWorkSche>();
        for (CoachWorkSche coachChe:lists
            ) {
            Date d2 =df.parse(TypeUtils.castToString(coachChe.getWorkDate().toInstant()));
            Long time = d2.getTime()-d1.getTime();
            if (0 < time && time <= 604800000){
                coachScheLists.add(coachChe);
            }
        }
        return coachScheLists;
    }

    //根据教练排班日期获取教练
    @Override
    public List<CoachWorkSche> getCoachesByWorkDate(ZonedDateTime workDate) throws Exception {
        /*SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        String sd=sf.format(workDate);
        Date d1= null;
        d1 = sf.parse(sd);
        String workTime=sf.format(d1);*/
        List<CoachWorkSche> result = coachWorkScheRespository.findCoachWorkScheByworkDate(workDate);
        return result;
    }

    @Override
    public List<CoachWorkSche> findAll() {
        return coachWorkScheRespository.findAll();
    }

    @Override
    public CoachWorkSche createCoachWorkSche(ZonedDateTime time, int wordWeekday, Long coachId) throws Exception {
        if (time == null || coachId == null){
            throw new Exception("参数解析异常！");
        }
        Coach coach = coachRepository.findOne(coachId);
        if (coach == null){
            throw new Exception("没有找到对应的教练！");
        }
        CoachWorkSche coachWorkSche=new CoachWorkSche();
        coachWorkSche.setWorkDate(time);
        coachWorkSche.setWorkWeekday(wordWeekday);
        coachWorkSche.setCoach(coach);
        return coachWorkScheRespository.save(coachWorkSche);
    }

    @Override
    public List getCoachByTime(Date firstDay, Date finalDay) throws Exception {
        List list = new ArrayList();
        /*List<CoachWorkSche> workScheList = coachWorkScheRespository.findCoachByTime(firstDay,finalDay);
        for (CoachWorkSche coachWorkSche:workScheList){
            ZonedDateTime workTime=coachWorkSche.getWorkDate();
            List<CoachWorkSche> coachWorkScheList=coachWorkScheRespository.findCoachWorkScheByworkDate(workTime);
            Map map = new HashMap();
            map.put("workDate",workTime);
            map.put("coachWorkScheList",coachWorkScheList);
            list.add(map);
        }*/

        while (firstDay.getTime() <= finalDay.getTime()){
            ZonedDateTime workTime = ZonedDateTime.ofInstant(firstDay.toInstant(), ZoneId.systemDefault());
            List<CoachWorkSche> coachWorkScheList=coachWorkScheRespository.findCoachWorkScheByworkDate(workTime);
            Map map = new HashMap();
            map.put("workDate",workTime);
            map.put("coachWorkScheList",coachWorkScheList);
            list.add(map);
            firstDay = new Date(firstDay.getTime()+1000*24*60*60);
        }

        return list;
    }
}
