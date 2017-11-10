package com.alienlab.catpower.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A QrScanLog.
 */
@Entity
@Table(name = "qr_scan_log")
public class QrScanLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scan_time")
    private ZonedDateTime scanTime;

    @Column(name = "openid")
    private String openid;

    @Column(name = "qrkey")
    private String qrkey;

    @Column(name = "reply")
    private String reply;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getScanTime() {
        return scanTime;
    }

    public QrScanLog scanTime(ZonedDateTime scanTime) {
        this.scanTime = scanTime;
        return this;
    }

    public void setScanTime(ZonedDateTime scanTime) {
        this.scanTime = scanTime;
    }

    public String getOpenid() {
        return openid;
    }

    public QrScanLog openid(String openid) {
        this.openid = openid;
        return this;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getQrkey() {
        return qrkey;
    }

    public QrScanLog qrkey(String qrkey) {
        this.qrkey = qrkey;
        return this;
    }

    public void setQrkey(String qrkey) {
        this.qrkey = qrkey;
    }

    public String getReply() {
        return reply;
    }

    public QrScanLog reply(String reply) {
        this.reply = reply;
        return this;
    }

    public void setReply(String reply) {
        this.reply = reply;
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
        QrScanLog qrScanLog = (QrScanLog) o;
        if (qrScanLog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), qrScanLog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QrScanLog{" +
            "id=" + getId() +
            ", scanTime='" + getScanTime() + "'" +
            ", openid='" + getOpenid() + "'" +
            ", qrkey='" + getQrkey() + "'" +
            ", reply='" + getReply() + "'" +
            "}";
    }
}
