package com.alienlab.catpower.web.wechat.bean.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * 二维码对象表
 */
@ApiModel(description = "二维码对象")
@Entity
@Table(name = "qr_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QrInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "二维码编码")
    private Long id;

    @Column(name = "qr_key")
    @ApiModelProperty(value = "参数二维码key")
    private String qrKey;

    @Column(name = "qr_ticker")
    @ApiModelProperty(value = "微信系统中参数二维码生成的ticket")
    private String qrTicker;

    @ApiModelProperty(value = "二维码创建时间")
    @Column(name = "qr_cttime")
    private ZonedDateTime qrCttime;

    @ApiModelProperty(value = "二维码有效期，截止时间")
    @Column(name = "qr_edtime")
    private ZonedDateTime qrEdtime;

    @ApiModelProperty(value = "二维码状态")
    @Column(name = "qr_status")
    private String qrStatus;

    @ApiModelProperty(value = "是否临时二维码")
    @Column(name = "qr_istemp")
    private String qrIstemp;

    @ManyToOne
    @ApiModelProperty(value = "二维码类型")
    private QrType qrType;

//    @Basic
//    private String qrUrl;
//
//    public String getQrUrl() {
//        return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+this.getQrTicker();
//    }
//
//    public void setQrUrl(String qrUrl) {
//        this.qrUrl = qrUrl;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQrKey() {
        return qrKey;
    }

    public QrInfo qrKey(String qrKey) {
        this.qrKey = qrKey;
        return this;
    }

    public void setQrKey(String qrKey) {
        this.qrKey = qrKey;
    }

    public String getQrTicker() {
        return qrTicker;
    }

    public QrInfo qrTicker(String qrTicker) {
        this.qrTicker = qrTicker;
        return this;
    }

    public void setQrTicker(String qrTicker) {
        this.qrTicker = qrTicker;
    }

    public ZonedDateTime getQrCttime() {
        return qrCttime;
    }

    public QrInfo qrCttime(ZonedDateTime qrCttime) {
        this.qrCttime = qrCttime;
        return this;
    }

    public void setQrCttime(ZonedDateTime qrCttime) {
        this.qrCttime = qrCttime;
    }

    public ZonedDateTime getQrEdtime() {
        return qrEdtime;
    }

    public QrInfo qrEdtime(ZonedDateTime qrEdtime) {
        this.qrEdtime = qrEdtime;
        return this;
    }

    public void setQrEdtime(ZonedDateTime qrEdtime) {
        this.qrEdtime = qrEdtime;
    }

    public String getQrStatus() {
        return qrStatus;
    }

    public QrInfo qrStatus(String qrStatus) {
        this.qrStatus = qrStatus;
        return this;
    }

    public void setQrStatus(String qrStatus) {
        this.qrStatus = qrStatus;
    }

    public String getQrIstemp() {
        return qrIstemp;
    }

    public QrInfo qrIstemp(String qrIstemp) {
        this.qrIstemp = qrIstemp;
        return this;
    }

    public void setQrIstemp(String qrIstemp) {
        this.qrIstemp = qrIstemp;
    }



    public QrType getQrType() {
        return qrType;
    }

    public QrInfo qrType(QrType qrType) {
        this.qrType = qrType;
        return this;
    }

    public void setQrType(QrType qrType) {
        this.qrType = qrType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrInfo qrInfo = (QrInfo) o;
        if (qrInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, qrInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "QrInfo{" +
            "id=" + id +
            ", qrKey='" + qrKey + "'" +
            ", qrTicker='" + qrTicker + "'" +
            ", qrCttime='" + qrCttime + "'" +
            ", qrEdtime='" + qrEdtime + "'" +
            ", qrStatus='" + qrStatus + "'" +
            ", qrIstemp='" + qrIstemp + "'" +
            '}';
    }
}
