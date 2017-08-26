package com.alienlab.catpower.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A WechatShopCard.
 */
@Entity
@Table(name = "wechat_shop_card")
public class WechatShopCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_id")
    private String cardId;

    @Column(name = "title")
    private String title;

    @Column(name = "card_json")
    private String cardJson;

    @Column(name = "ct_time")
    private ZonedDateTime ctTime;

    @Column(name = "card_status")
    private String cardStatus;

    @Column(name = "card_remain_count")
    private Integer cardRemainCount;

    @Column(name = "card_type")
    private String cardType;

    @ManyToOne
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardId() {
        return cardId;
    }

    public WechatShopCard cardId(String cardId) {
        this.cardId = cardId;
        return this;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getTitle() {
        return title;
    }

    public WechatShopCard title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCardJson() {
        return cardJson;
    }

    public WechatShopCard cardJson(String cardJson) {
        this.cardJson = cardJson;
        return this;
    }

    public void setCardJson(String cardJson) {
        this.cardJson = cardJson;
    }

    public ZonedDateTime getCtTime() {
        return ctTime;
    }

    public WechatShopCard ctTime(ZonedDateTime ctTime) {
        this.ctTime = ctTime;
        return this;
    }

    public void setCtTime(ZonedDateTime ctTime) {
        this.ctTime = ctTime;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public WechatShopCard cardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
        return this;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public Integer getCardRemainCount() {
        return cardRemainCount;
    }

    public WechatShopCard cardRemainCount(Integer cardRemainCount) {
        this.cardRemainCount = cardRemainCount;
        return this;
    }

    public void setCardRemainCount(Integer cardRemainCount) {
        this.cardRemainCount = cardRemainCount;
    }

    public String getCardType() {
        return cardType;
    }

    public WechatShopCard cardType(String cardType) {
        this.cardType = cardType;
        return this;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Course getCourse() {
        return course;
    }

    public WechatShopCard course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WechatShopCard wechatShopCard = (WechatShopCard) o;
        if (wechatShopCard.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wechatShopCard.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WechatShopCard{" +
            "id=" + getId() +
            ", cardId='" + getCardId() + "'" +
            ", title='" + getTitle() + "'" +
            ", cardJson='" + getCardJson() + "'" +
            ", ctTime='" + getCtTime() + "'" +
            ", cardStatus='" + getCardStatus() + "'" +
            ", cardRemainCount='" + getCardRemainCount() + "'" +
            ", cardType='" + getCardType() + "'" +
            "}";
    }
}
