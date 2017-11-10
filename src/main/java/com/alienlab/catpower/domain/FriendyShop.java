package com.alienlab.catpower.domain;


import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A FriendyShop.
 */
@Entity
@Table(name = "friendy_shop")
public class FriendyShop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "shop_desc")
    private String shopDesc;

    @Column(name = "shop_position")
    private String shopPosition;

    @Column(name = "create_time")
    private ZonedDateTime createTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @ManyToOne
    private QrInfo qrInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public FriendyShop shopName(String shopName) {
        this.shopName = shopName;
        return this;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public FriendyShop shopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
        return this;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public String getShopPosition() {
        return shopPosition;
    }

    public FriendyShop shopPosition(String shopPosition) {
        this.shopPosition = shopPosition;
        return this;
    }

    public void setShopPosition(String shopPosition) {
        this.shopPosition = shopPosition;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public FriendyShop createTime(ZonedDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public FriendyShop endTime(ZonedDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public QrInfo getQrInfo() {
        return qrInfo;
    }

    public FriendyShop qrInfo(QrInfo qrInfo) {
        this.qrInfo = qrInfo;
        return this;
    }

    public void setQrInfo(QrInfo qrInfo) {
        this.qrInfo = qrInfo;
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
        FriendyShop friendyShop = (FriendyShop) o;
        if (friendyShop.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), friendyShop.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FriendyShop{" +
            "id=" + getId() +
            ", shopName='" + getShopName() + "'" +
            ", shopDesc='" + getShopDesc() + "'" +
            ", shopPosition='" + getShopPosition() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            "}";
    }
}
