package com.alienlab.catpower.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/9/20.
 */
@Entity
@Table(name="online_order")
public class OnlineOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="openid")
    private String openid;

    @Column(name="order_time")
    private Timestamp order_time;

    @Column(name="order_status")
    private String order_status;

    @Column(name="order_paytime")
    private Timestamp order_paytime;

    @Column(name="order_wechatjson")
    private String order_wechatjson;

    @Column(name="course_id")
    private Integer course_id;

    public OnlineOrder() {
    }

    public OnlineOrder(String openid, Timestamp order_time, String order_status, Timestamp order_paytime, String order_wechatjson, Integer course_id) {
        this.openid = openid;
        this.order_time = order_time;
        this.order_status = order_status;
        this.order_paytime = order_paytime;
        this.order_wechatjson = order_wechatjson;
        this.course_id = course_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Timestamp getOrder_time() {
        return order_time;
    }

    public void setOrder_time(Timestamp order_time) {
        this.order_time = order_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public Timestamp getOrder_paytime() {
        return order_paytime;
    }

    public void setOrder_paytime(Timestamp order_paytime) {
        this.order_paytime = order_paytime;
    }

    public String getOrder_wechatjson() {
        return order_wechatjson;
    }

    public void setOrder_wechatjson(String order_wechatjson) {
        this.order_wechatjson = order_wechatjson;
    }

    public Integer getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Integer course_id) {
        this.course_id = course_id;
    }

    @Override
    public String toString() {
        return "OnlineOrder{" +
            "id=" + id +
            ", openid='" + openid + '\'' +
            ", order_time=" + order_time +
            ", order_status='" + order_status + '\'' +
            ", order_paytime=" + order_paytime +
            ", order_wechatjson='" + order_wechatjson + '\'' +
            ", course_id=" + course_id +
            '}';
    }
}
