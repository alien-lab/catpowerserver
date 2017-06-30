package com.alienlab.catpower.web.wechat.bean.entity.dto;


/**
 * Created by admin on 2016-12-26.
 */
public class QrInfoDto {
    private String qr_type_name;
    private String qr_status;
    public QrInfoDto() {
    }

    public QrInfoDto(String qr_type_name) {

        this.qr_type_name = qr_type_name;
/*        this.qr_status = qr_status;*/
    }

    public String getqr_type_name() {
        return qr_type_name;

    }

    public void setqr_type_name(String qr_type_name) {
        this.qr_type_name = qr_type_name;
    }

/*
    public String getqr_status() {
        return qr_status;
    }
*/

/*    public void setqr_status(String qr_status) {
        this.qr_status = qr_status;
    }*/
}
