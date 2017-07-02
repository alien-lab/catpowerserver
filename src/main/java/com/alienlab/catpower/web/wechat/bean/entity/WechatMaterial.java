package com.alienlab.catpower.web.wechat.bean.entity;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Created by zhuliang on 2017/3/28.
 */
@ApiModel(description = "资讯信息表")
@Entity
@Table(name = "wechat_material")
public class WechatMaterial {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "btn_id")
    private String btnId;

    @Column(name = "media_id")
    private String mediaId;



    @Column(name = "create_time")
    private ZonedDateTime craeteTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBtnId() {
        return btnId;
    }

    public void setBtnId(String btnId) {
        this.btnId = btnId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public ZonedDateTime getCraeteTime() {
        return craeteTime;
    }

    public void setCraeteTime(ZonedDateTime craeteTime) {
        this.craeteTime = craeteTime;
    }
}
