package com.alienlab.catpower.web.wechat.bean.entity;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * 微信消息日志
 */
@ApiModel(description = "微信消息日志")
@Entity
@Table(name = "wechat_message_log")
public class WechatMessageLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_time")
    private ZonedDateTime messageTime;

    @Column(name = "message_body")
    private String messageBody;

    @Column(name = "message_status")
    private String messageStatus;
    @ManyToOne
    private WechatUser wechatUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getMessageTime() {
        return messageTime;
    }

    public WechatMessageLog messageTime(ZonedDateTime messageTime) {
        this.messageTime = messageTime;
        return this;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public void setMessageTime(ZonedDateTime messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public WechatMessageLog messageBody(String messageBody) {
        this.messageBody = messageBody;
        return this;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public WechatUser getWechatUser() {
        return wechatUser;
    }

    public WechatMessageLog wechatUser(WechatUser wechatUser) {
        this.wechatUser = wechatUser;
        return this;
    }

    public void setWechatUser(WechatUser wechatUser) {
        this.wechatUser = wechatUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WechatMessageLog wechatMessageLog = (WechatMessageLog) o;
        if (wechatMessageLog.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, wechatMessageLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WechatMessageLog{" +
            "id=" + id +
            ", messageTime='" + messageTime + "'" +
            ", messageBody='" + messageBody + "'" +
            '}';
    }
}
