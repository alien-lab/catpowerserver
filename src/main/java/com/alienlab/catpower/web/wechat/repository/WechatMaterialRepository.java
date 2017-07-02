package com.alienlab.catpower.web.wechat.repository;

import com.alienlab.catpower.web.wechat.bean.entity.WechatMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhuliang on 2017/3/28.
 */
@SuppressWarnings("unused")
@Repository
public interface WechatMaterialRepository extends JpaRepository<WechatMaterial,Long> {
    List<WechatMaterial> findMaterialByBtnIdOrderByCraeteTimeDesc(String btnId);

    List<WechatMaterial> deleteByBtnId(String btnId);
}
