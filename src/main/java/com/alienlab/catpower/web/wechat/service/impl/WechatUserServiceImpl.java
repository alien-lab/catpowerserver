package com.alienlab.catpower.web.wechat.service.impl;

import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import com.alienlab.catpower.web.wechat.repository.WechatUserRepository;
import com.alienlab.catpower.web.wechat.service.WechatUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing WechatUser.
 */
@Service
@Transactional
public class WechatUserServiceImpl implements WechatUserService {

    private final Logger log = LoggerFactory.getLogger(WechatUserServiceImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private WechatUserRepository wechatUserRepository;

    /**
     * Save a wechatUser.
     *
     * @param wechatUser the entity to save
     * @return the persisted entity
     */
    public WechatUser save(WechatUser wechatUser) {
        log.debug("Request to save WechatUser : {}", wechatUser);
        WechatUser result = wechatUserRepository.save(wechatUser);

        return result;
    }

    /**
     *  Get all the wechatUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WechatUser> findAll(Pageable pageable) {
        log.debug("Request to get all WechatUsers");
        Page<WechatUser> result = wechatUserRepository.findAll(pageable);
        return result;
    }


    /**
     *  Get one wechatUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public WechatUser findOne(Long id) {
        log.debug("Request to get WechatUser : {}", id);
        WechatUser wechatUser = wechatUserRepository.findOne(id);
        return wechatUser;
    }

    /**
     *  Delete the  wechatUser by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WechatUser : {}", id);
        wechatUserRepository.delete(id);
    }

    @Override
    public WechatUser findUserByOpenid(String openid) {
        WechatUser wechatUser = wechatUserRepository.findWechatUserByOpenId(openid);
        return wechatUser;
    }

}
