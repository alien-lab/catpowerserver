package com.alienlab.catpower.web.wechat.repository;

import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WechatUser entity.
 */
@Repository
public interface WechatUserRepository extends JpaRepository<WechatUser,Long> {
    WechatUser findWechatUserByOpenId(String openid);

}
