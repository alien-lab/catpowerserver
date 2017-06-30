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
 * 二维码类别表
 */
@ApiModel(description = "二维码类别")
@Entity
@Table(name = "qr_type")
public class QrType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qr_type_name")
    @ApiModelProperty(value = "二维码类别名称")
    private String qrTypeName;

    @Column(name = "qr_type_table")
    @ApiModelProperty(value = "该类型二维码对象关联数据表")
    private String qrTypeTable;

    @Column(name = "qr_type_idfield")
    @ApiModelProperty(value = "该类型二维码关联数据表中ID字段")
    private String qrTypeIdfield;
    @Column(name = "qr_type_namefield")
    @ApiModelProperty(value = "该类型二维码关联数据表中名称字段")
    private String qrTypeNamefield;

    @Column(name = "qr_type_reptype")
    @ApiModelProperty(value = "该类型二维码微信回复类型，图文、文本、链接")
    private String qrTypeReptype;

    @Column(name = "qr_type_url")
    @ApiModelProperty(value = "该类型二维码回复关联链接")
    private String qrTypeUrl;

    @Column(name = "qr_type_cttime")
    @ApiModelProperty(value = "创建时间")
    private ZonedDateTime qrTypeCttime;

    @Column(name = "qr_type_status")
    @ApiModelProperty(value = "类别状态")
    private String qrTypeStatus;

    @Column(name = "qr_type_autogeneration")
    @ApiModelProperty(value = "是否自动生成")
    private String qrTypeAutogeneration;

    public String getQrTypeAutogeneration() {
        return qrTypeAutogeneration;
    }

    public void setQrTypeAutogeneration(String qrTypeAutogeneration) {
        this.qrTypeAutogeneration = qrTypeAutogeneration;
    }

    public QrType getQrTypeAutogeneration(String qrTypeAutogeneration) {
        this.qrTypeAutogeneration = qrTypeAutogeneration;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQrTypeName() {
        return qrTypeName;
    }

    public QrType qrTypeName(String qrTypeName) {
        this.qrTypeName = qrTypeName;
        return this;
    }

    public void setQrTypeName(String qrTypeName) {
        this.qrTypeName = qrTypeName;
    }

    public String getQrTypeTable() {
        return qrTypeTable;
    }

    public QrType qrTypeTable(String qrTypeTable) {
        this.qrTypeTable = qrTypeTable;
        return this;
    }

    public void setQrTypeTable(String qrTypeTable) {
        this.qrTypeTable = qrTypeTable;
    }

    public String getQrTypeIdfield() {
        return qrTypeIdfield;
    }

    public QrType qrTypeIdfield(String qrTypeIdfield) {
        this.qrTypeIdfield = qrTypeIdfield;
        return this;
    }

    public String getQrTypeNamefield() {
        return qrTypeNamefield;
    }

    public void setQrTypeNamefield(String qrTypeNamefield) {
        this.qrTypeNamefield = qrTypeNamefield;
    }

    public void setQrTypeIdfield(String qrTypeIdfield) {
        this.qrTypeIdfield = qrTypeIdfield;
    }

    public String getQrTypeReptype() {
        return qrTypeReptype;
    }

    public QrType qrTypeReptype(String qrTypeReptype) {
        this.qrTypeReptype = qrTypeReptype;
        return this;
    }

    public void setQrTypeReptype(String qrTypeReptype) {
        this.qrTypeReptype = qrTypeReptype;
    }

    public String getQrTypeUrl() {
        return qrTypeUrl;
    }

    public QrType qrTypeUrl(String qrTypeUrl) {
        this.qrTypeUrl = qrTypeUrl;
        return this;
    }

    public void setQrTypeUrl(String qrTypeUrl) {
        this.qrTypeUrl = qrTypeUrl;
    }

    public ZonedDateTime getQrTypeCttime() {
        return qrTypeCttime;
    }

    public QrType qrTypeCttime(ZonedDateTime qrTypeCttime) {
        this.qrTypeCttime = qrTypeCttime;
        return this;
    }

    public void setQrTypeCttime(ZonedDateTime qrTypeCttime) {
        this.qrTypeCttime = qrTypeCttime;
    }

    public String getQrTypeStatus() {
        return qrTypeStatus;
    }

    public QrType qrTypeStatus(String qrTypeStatus) {
        this.qrTypeStatus = qrTypeStatus;
        return this;
    }

    public void setQrTypeStatus(String qrTypeStatus) {
        this.qrTypeStatus = qrTypeStatus;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrType qrType = (QrType) o;
        if (qrType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, qrType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "QrType{" +
            "id=" + id +
            ", qrTypeName='" + qrTypeName + "'" +
            ", qrTypeTable='" + qrTypeTable + "'" +
            ", qrTypeIdfield='" + qrTypeIdfield + "'" +
            ", qrTypeReptype='" + qrTypeReptype + "'" +
            ", qrTypeUrl='" + qrTypeUrl + "'" +
            ", qrTypeCttime='" + qrTypeCttime + "'" +
            ", qrTypeStatus='" + qrTypeStatus + "'" +
            '}';
    }
}
