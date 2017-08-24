package com.alienlab.catpower.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A WechatVipcard.
 */
@Entity
@Table(name = "wechat_vipcard")
public class WechatVipcard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "openid")
    private String openid;

    @Column(name = "card_json")
    private String cardJson;

    @Column(name = "get_time")
    private ZonedDateTime getTime;

    @Column(name = "active_time")
    private ZonedDateTime activeTime;

    @Column(name = "card_id")
    private String cardId;

    @Column(name = "card_code")
    private String cardCode;

    @Column(name = "card_status")
    private String cardStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public WechatVipcard openid(String openid) {
        this.openid = openid;
        return this;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCardJson() {
        return cardJson;
    }

    public WechatVipcard cardJson(String cardJson) {
        this.cardJson = cardJson;
        return this;
    }

    public void setCardJson(String cardJson) {
        this.cardJson = cardJson;
    }

    public ZonedDateTime getGetTime() {
        return getTime;
    }

    public WechatVipcard getTime(ZonedDateTime getTime) {
        this.getTime = getTime;
        return this;
    }

    public void setGetTime(ZonedDateTime getTime) {
        this.getTime = getTime;
    }

    public ZonedDateTime getActiveTime() {
        return activeTime;
    }

    public WechatVipcard activeTime(ZonedDateTime activeTime) {
        this.activeTime = activeTime;
        return this;
    }

    public void setActiveTime(ZonedDateTime activeTime) {
        this.activeTime = activeTime;
    }

    public String getCardId() {
        return cardId;
    }

    public WechatVipcard cardId(String cardId) {
        this.cardId = cardId;
        return this;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardCode() {
        return cardCode;
    }

    public WechatVipcard cardCode(String cardCode) {
        this.cardCode = cardCode;
        return this;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public WechatVipcard cardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
        return this;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WechatVipcard wechatVipcard = (WechatVipcard) o;
        if (wechatVipcard.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wechatVipcard.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WechatVipcard{" +
            "id=" + getId() +
            ", openid='" + getOpenid() + "'" +
            ", cardJson='" + getCardJson() + "'" +
            ", getTime='" + getGetTime() + "'" +
            ", activeTime='" + getActiveTime() + "'" +
            ", cardId='" + getCardId() + "'" +
            ", cardCode='" + getCardCode() + "'" +
            ", cardStatus='" + getCardStatus() + "'" +
            "}";
    }
}
