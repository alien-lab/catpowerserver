package com.alienlab.catpower.repository;

import com.alienlab.catpower.domain.WechatGoodsList;
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
    List<WechatOrder> findWechatOrderByWechatUserAAndWechatGoodsListAndOrderStatus(WechatUser wu, WechatGoodsList goods,String status);

}
