package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.WechatOrder;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the WechatOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WechatOrderRepository extends JpaRepository<WechatOrder,Long> {
    List<WechatOrder> findWechatOrderByWechatUserAndOrderStatus(WechatUser wu,String status);

}
