package com.alienlab.catpower.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Coach.
 */
@Entity
@Table(name = "coach")
public class Coach implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coach_name")
    private String coachName;

    @Column(name = "coach_phone")
    private String coachPhone;

    @Column(name = "coach_introduce")
    private String coachIntroduce;

    @Column(name = "coach_picture")
    private String coachPicture;

    @Column(name = "coach_wechatopenid")
    private String coachWechatopenid;

    @Column(name = "coach_wechatname")
    private String coachWechatname;

    @Column(name = "coach_wechatpicture")
    private String coachWechatpicture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoachName() {
        return coachName;
    }

    public Coach coachName(String coachName) {
        this.coachName = coachName;
        return this;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getCoachPhone() {
        return coachPhone;
    }

    public Coach coachPhone(String coachPhone) {
        this.coachPhone = coachPhone;
        return this;
    }

    public void setCoachPhone(String coachPhone) {
        this.coachPhone = coachPhone;
    }

    public String getCoachIntroduce() {
        return coachIntroduce;
    }

    public Coach coachIntroduce(String coachIntroduce) {
        this.coachIntroduce = coachIntroduce;
        return this;
    }

    public void setCoachIntroduce(String coachIntroduce) {
        this.coachIntroduce = coachIntroduce;
    }

    public String getCoachPicture() {
        return coachPicture;
    }

    public Coach coachPicture(String coachPicture) {
        this.coachPicture = coachPicture;
        return this;
    }

    public void setCoachPicture(String coachPicture) {
        this.coachPicture = coachPicture;
    }

    public String getCoachWechatopenid() {
        return coachWechatopenid;
    }

    public Coach coachWechatopenid(String coachWechatopenid) {
        this.coachWechatopenid = coachWechatopenid;
        return this;
    }

    public void setCoachWechatopenid(String coachWechatopenid) {
        this.coachWechatopenid = coachWechatopenid;
    }

    public String getCoachWechatname() {
        return coachWechatname;
    }

    public Coach coachWechatname(String coachWechatname) {
        this.coachWechatname = coachWechatname;
        return this;
    }

    public void setCoachWechatname(String coachWechatname) {
        this.coachWechatname = coachWechatname;
    }

    public String getCoachWechatpicture() {
        return coachWechatpicture;
    }

    public Coach coachWechatpicture(String coachWechatpicture) {
        this.coachWechatpicture = coachWechatpicture;
        return this;
    }

    public void setCoachWechatpicture(String coachWechatpicture) {
        this.coachWechatpicture = coachWechatpicture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coach coach = (Coach) o;
        if (coach.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), coach.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Coach{" +
            "id=" + getId() +
            ", coachName='" + getCoachName() + "'" +
            ", coachPhone='" + getCoachPhone() + "'" +
            ", coachIntroduce='" + getCoachIntroduce() + "'" +
            ", coachPicture='" + getCoachPicture() + "'" +
            ", coachWechatopenid='" + getCoachWechatopenid() + "'" +
            ", coachWechatname='" + getCoachWechatname() + "'" +
            ", coachWechatpicture='" + getCoachWechatpicture() + "'" +
            "}";
    }
}
