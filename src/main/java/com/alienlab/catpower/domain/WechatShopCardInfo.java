package com.alienlab.catpower.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A WechatShopCardInfo.
 */
@Entity
@Table(name = "wechat_shop_card_info")
public class WechatShopCardInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "openid")
    private String openid;

    @Column(name = "card_id")
    private String cardId;

    @Column(name = "card_code")
    private String cardCode;

    @Column(name = "get_time")
    private ZonedDateTime getTime;

    @Column(name = "recharge_time")
    private ZonedDateTime rechargeTime;

    @Column(name = "out_str")
    private String outStr;

    @ManyToOne
    private WechatShopCard wechatShopCard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public WechatShopCardInfo openid(String openid) {
        this.openid = openid;
        return this;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCardId() {
        return cardId;
    }

    public WechatShopCardInfo cardId(String cardId) {
        this.cardId = cardId;
        return this;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardCode() {
        return cardCode;
    }

    public WechatShopCardInfo cardCode(String cardCode) {
        this.cardCode = cardCode;
        return this;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public ZonedDateTime getGetTime() {
        return getTime;
    }

    public WechatShopCardInfo getTime(ZonedDateTime getTime) {
        this.getTime = getTime;
        return this;
    }

    public void setGetTime(ZonedDateTime getTime) {
        this.getTime = getTime;
    }

    public ZonedDateTime getRechargeTime() {
        return rechargeTime;
    }

    public WechatShopCardInfo rechargeTime(ZonedDateTime rechargeTime) {
        this.rechargeTime = rechargeTime;
        return this;
    }

    public void setRechargeTime(ZonedDateTime rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public String getOutStr() {
        return outStr;
    }

    public WechatShopCardInfo outStr(String outStr) {
        this.outStr = outStr;
        return this;
    }

    public void setOutStr(String outStr) {
        this.outStr = outStr;
    }

    public WechatShopCard getWechatShopCard() {
        return wechatShopCard;
    }

    public WechatShopCardInfo wechatShopCard(WechatShopCard wechatShopCard) {
        this.wechatShopCard = wechatShopCard;
        return this;
    }

    public void setWechatShopCard(WechatShopCard wechatShopCard) {
        this.wechatShopCard = wechatShopCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WechatShopCardInfo wechatShopCardInfo = (WechatShopCardInfo) o;
        if (wechatShopCardInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wechatShopCardInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WechatShopCardInfo{" +
            "id=" + getId() +
            ", openid='" + getOpenid() + "'" +
            ", cardId='" + getCardId() + "'" +
            ", cardCode='" + getCardCode() + "'" +
            ", getTime='" + getGetTime() + "'" +
            ", rechargeTime='" + getRechargeTime() + "'" +
            ", outStr='" + getOutStr() + "'" +
            "}";
    }
}
