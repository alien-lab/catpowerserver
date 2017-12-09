package com.alienlab.catpower.domain;


import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A WechatOrder.
 */
@Entity
@Table(name = "wechat_order")
public class WechatOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "order_time")
    private ZonedDateTime orderTime;

    @Column(name = "pay_time")
    private ZonedDateTime payTime;

    @Column(name = "order_status")
    private String orderStatus;

    @ManyToOne
    private WechatUser wechatUser;

    @ManyToOne
    private WechatGoodsList wechatGoodsList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public WechatOrder orderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public ZonedDateTime getOrderTime() {
        return orderTime;
    }

    public WechatOrder orderTime(ZonedDateTime orderTime) {
        this.orderTime = orderTime;
        return this;
    }

    public void setOrderTime(ZonedDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public ZonedDateTime getPayTime() {
        return payTime;
    }

    public WechatOrder payTime(ZonedDateTime payTime) {
        this.payTime = payTime;
        return this;
    }

    public void setPayTime(ZonedDateTime payTime) {
        this.payTime = payTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public WechatOrder orderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public WechatUser getWechatUser() {
        return wechatUser;
    }

    public WechatOrder wechatUser(WechatUser wechatUser) {
        this.wechatUser = wechatUser;
        return this;
    }

    public void setWechatUser(WechatUser wechatUser) {
        this.wechatUser = wechatUser;
    }

    public WechatGoodsList getWechatGoodsList() {
        return wechatGoodsList;
    }

    public WechatOrder wechatGoodsList(WechatGoodsList wechatGoodsList) {
        this.wechatGoodsList = wechatGoodsList;
        return this;
    }

    public void setWechatGoodsList(WechatGoodsList wechatGoodsList) {
        this.wechatGoodsList = wechatGoodsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WechatOrder wechatOrder = (WechatOrder) o;
        if (wechatOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wechatOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WechatOrder{" +
            "id=" + getId() +
            ", orderNo='" + getOrderNo() + "'" +
            ", orderTime='" + getOrderTime() + "'" +
            ", payTime='" + getPayTime() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            "}";
    }
}
