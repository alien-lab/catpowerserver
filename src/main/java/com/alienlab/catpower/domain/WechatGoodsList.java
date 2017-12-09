package com.alienlab.catpower.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WechatGoodsList.
 */
@Entity
@Table(name = "wechat_goods_list")
public class WechatGoodsList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "goods_name")
    private String goodsName;

    @Column(name = "goods_memo")
    private String goodsMemo;

    @Column(name = "goods_pic")
    private String goodsPic;

    @Column(name = "buy_button_text")
    private String buyButtonText;

    @Column(name = "limit_count")
    private Integer limitCount;

    @Column(name = "goods_price")
    private Float goodsPrice;

    @Column(name = "sell_status")
    private String sellStatus;

    @Column(name = "goods_field_1")
    private String goodsField1;

    @ManyToOne
    private WechatShopCard wechatShopCard;

    @ManyToOne
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public WechatGoodsList goodsName(String goodsName) {
        this.goodsName = goodsName;
        return this;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsMemo() {
        return goodsMemo;
    }

    public WechatGoodsList goodsMemo(String goodsMemo) {
        this.goodsMemo = goodsMemo;
        return this;
    }

    public void setGoodsMemo(String goodsMemo) {
        this.goodsMemo = goodsMemo;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public WechatGoodsList goodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
        return this;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public String getBuyButtonText() {
        return buyButtonText;
    }

    public WechatGoodsList buyButtonText(String buyButtonText) {
        this.buyButtonText = buyButtonText;
        return this;
    }

    public void setBuyButtonText(String buyButtonText) {
        this.buyButtonText = buyButtonText;
    }

    public Integer getLimitCount() {
        return limitCount;
    }

    public WechatGoodsList limitCount(Integer limitCount) {
        this.limitCount = limitCount;
        return this;
    }

    public void setLimitCount(Integer limitCount) {
        this.limitCount = limitCount;
    }

    public Float getGoodsPrice() {
        return goodsPrice;
    }

    public WechatGoodsList goodsPrice(Float goodsPrice) {
        this.goodsPrice = goodsPrice;
        return this;
    }

    public void setGoodsPrice(Float goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getSellStatus() {
        return sellStatus;
    }

    public WechatGoodsList sellStatus(String sellStatus) {
        this.sellStatus = sellStatus;
        return this;
    }

    public void setSellStatus(String sellStatus) {
        this.sellStatus = sellStatus;
    }

    public String getGoodsField1() {
        return goodsField1;
    }

    public WechatGoodsList goodsField1(String goodsField1) {
        this.goodsField1 = goodsField1;
        return this;
    }

    public void setGoodsField1(String goodsField1) {
        this.goodsField1 = goodsField1;
    }

    public WechatShopCard getWechatShopCard() {
        return wechatShopCard;
    }

    public WechatGoodsList wechatShopCard(WechatShopCard wechatShopCard) {
        this.wechatShopCard = wechatShopCard;
        return this;
    }

    public void setWechatShopCard(WechatShopCard wechatShopCard) {
        this.wechatShopCard = wechatShopCard;
    }

    public Course getCourse() {
        return course;
    }

    public WechatGoodsList course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WechatGoodsList wechatGoodsList = (WechatGoodsList) o;
        if (wechatGoodsList.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wechatGoodsList.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WechatGoodsList{" +
            "id=" + getId() +
            ", goodsName='" + getGoodsName() + "'" +
            ", goodsMemo='" + getGoodsMemo() + "'" +
            ", goodsPic='" + getGoodsPic() + "'" +
            ", buyButtonText='" + getBuyButtonText() + "'" +
            ", limitCount='" + getLimitCount() + "'" +
            ", goodsPrice='" + getGoodsPrice() + "'" +
            ", sellStatus='" + getSellStatus() + "'" +
            ", goodsField1='" + getGoodsField1() + "'" +
            "}";
    }
}
