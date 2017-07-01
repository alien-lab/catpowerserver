package com.alienlab.catpower.web.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;
import com.alienlab.catpower.web.wechat.repository.QrInfoRepository;
import com.alienlab.catpower.web.wechat.repository.QrTypeRepository;
import com.alienlab.catpower.web.wechat.repository.WechatUserRepository;
import com.alienlab.catpower.web.wechat.service.QrInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing QrInfo.
 */
@Service
@Transactional
public class QrInfoServiceImpl implements QrInfoService {

    private final Logger log = LoggerFactory.getLogger(QrInfoServiceImpl.class);
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private QrInfoRepository qrInfoRepository;

    @Autowired
    private QrTypeRepository qrTypeRepository;

    @Autowired
    private WechatUserRepository wechatUserRepository;



    /**
     * Save a qrInfo.
     *
     * @param qrInfo the entity to save
     * @return the persisted entity
     */
    public QrInfo save(QrInfo qrInfo) {
        log.debug("Request to save QrInfo : {}", qrInfo);
        QrInfo result = qrInfoRepository.save(qrInfo);
        return result;
    }

    /**
     *  Get all the qrInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<QrInfo> findAll(Pageable pageable) {
        log.debug("Request to get all QrInfos");
        Page<QrInfo> result = qrInfoRepository.findAll(pageable);
        return result;
    }


    /**
     *  get all the qrInfos where PartnerContract is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<QrInfo> findAllWherePartnerContractIsNull() {
        log.debug("Request to get all qrInfos where PartnerContract is null");
        return StreamSupport
            .stream(qrInfoRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }


    /**
     *  get all the qrInfos where WechatPartner is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<QrInfo> findAllWhereWechatPartnerIsNull() {
        log.debug("Request to get all qrInfos where WechatPartner is null");
        return StreamSupport
            .stream(qrInfoRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     *  Get one qrInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public QrInfo findOne(Long id) {
        log.debug("Request to get QrInfo : {}", id);
        QrInfo qrInfo = qrInfoRepository.findOne(id);
        return qrInfo;
    }

    /**
     *  Delete the  qrInfo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete QrInfo : {}", id);
        qrInfoRepository.delete(id);
    }



    @Override
    public QrInfo createQrinfo(String sceneId , Long id , String ticket){
        QrInfo qrInfo = new QrInfo();
        qrInfo.setQrCttime(ZonedDateTime.now());
        qrInfo.setQrKey(sceneId);
        qrInfo.setQrType(qrTypeRepository.findOne(id));
        qrInfo.setQrTicker(ticket);
        return qrInfoRepository.save(qrInfo);
    }




}
