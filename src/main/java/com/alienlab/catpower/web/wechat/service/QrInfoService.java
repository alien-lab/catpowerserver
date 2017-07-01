package com.alienlab.catpower.web.wechat.service;

import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing QrInfo.
 */
public interface QrInfoService {

    /**
     * Save a qrInfo.
     *
     * @param qrInfo the entity to save
     * @return the persisted entity
     */
    QrInfo save(QrInfo qrInfo);

    /**
     *  Get all the qrInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<QrInfo> findAll(Pageable pageable);
    /**
     *  Get all the QrInfoDTO where PartnerContract is null.
     *
     *  @return the list of entities
     */
    List<QrInfo> findAllWherePartnerContractIsNull();
    /**
     *  Get all the QrInfoDTO where WechatPartner is null.
     *
     *  @return the list of entities
     */
    List<QrInfo> findAllWhereWechatPartnerIsNull();

    /**
     *  Get the "id" qrInfo.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    QrInfo findOne(Long id);

    /**
     *  Delete the "id" qrInfo.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    QrInfo createQrinfo(String sceneId , Long id , String ticket);

}
