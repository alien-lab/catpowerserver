package com.alienlab.catpower.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Learner.
 */
@Entity
@Table(name = "learner")
public class Learner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "learne_name")
    private String learneName;

    @Column(name = "learner_phone")
    private String learnerPhone;

    @Column(name = "learner_sex")
    private String learnerSex;

    @Column(name = "regist_time")
    private ZonedDateTime registTime;

    @Column(name = "wx_open_id")
    private String wxOpenId;

    @Column(name = "wx_nickname")
    private String wxNickname;

    @Column(name = "wx_header")
    private String wxHeader;

    @Column(name = "first_totime")
    private ZonedDateTime firstTotime;

    @Column(name = "first_buyclass")
    private ZonedDateTime firstBuyclass;

    @Column(name = "recently_signin")
    private ZonedDateTime recentlySignin;

    @Column(name = "experience")
    private Long experience;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLearneName() {
        return learneName;
    }

    public Learner learneName(String learneName) {
        this.learneName = learneName;
        return this;
    }

    public void setLearneName(String learneName) {
        this.learneName = learneName;
    }

    public String getLearnerPhone() {
        return learnerPhone;
    }

    public Learner learnerPhone(String learnerPhone) {
        this.learnerPhone = learnerPhone;
        return this;
    }

    public void setLearnerPhone(String learnerPhone) {
        this.learnerPhone = learnerPhone;
    }

    public String getLearnerSex() {
        return learnerSex;
    }

    public Learner learnerSex(String learnerSex) {
        this.learnerSex = learnerSex;
        return this;
    }

    public void setLearnerSex(String learnerSex) {
        this.learnerSex = learnerSex;
    }

    public ZonedDateTime getRegistTime() {
        return registTime;
    }

    public Learner registTime(ZonedDateTime registTime) {
        this.registTime = registTime;
        return this;
    }

    public void setRegistTime(ZonedDateTime registTime) {
        this.registTime = registTime;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public Learner wxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
        return this;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getWxNickname() {
        return wxNickname;
    }

    public Learner wxNickname(String wxNickname) {
        this.wxNickname = wxNickname;
        return this;
    }

    public void setWxNickname(String wxNickname) {
        this.wxNickname = wxNickname;
    }

    public String getWxHeader() {
        return wxHeader;
    }

    public Learner wxHeader(String wxHeader) {
        this.wxHeader = wxHeader;
        return this;
    }

    public void setWxHeader(String wxHeader) {
        this.wxHeader = wxHeader;
    }

    public ZonedDateTime getFirstTotime() {
        return firstTotime;
    }

    public Learner firstTotime(ZonedDateTime firstTotime) {
        this.firstTotime = firstTotime;
        return this;
    }

    public void setFirstTotime(ZonedDateTime firstTotime) {
        this.firstTotime = firstTotime;
    }

    public ZonedDateTime getFirstBuyclass() {
        return firstBuyclass;
    }

    public Learner firstBuyclass(ZonedDateTime firstBuyclass) {
        this.firstBuyclass = firstBuyclass;
        return this;
    }

    public void setFirstBuyclass(ZonedDateTime firstBuyclass) {
        this.firstBuyclass = firstBuyclass;
    }

    public ZonedDateTime getRecentlySignin() {
        return recentlySignin;
    }

    public Learner recentlySignin(ZonedDateTime recentlySignin) {
        this.recentlySignin = recentlySignin;
        return this;
    }

    public void setRecentlySignin(ZonedDateTime recentlySignin) {
        this.recentlySignin = recentlySignin;
    }

    public Long getExperience() {
        return experience;
    }

    public Learner experience(Long experience) {
        this.experience = experience;
        return this;
    }

    public void setExperience(Long experience) {
        this.experience = experience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Learner learner = (Learner) o;
        if (learner.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), learner.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Learner{" +
            "id=" + getId() +
            ", learneName='" + getLearneName() + "'" +
            ", learnerPhone='" + getLearnerPhone() + "'" +
            ", learnerSex='" + getLearnerSex() + "'" +
            ", registTime='" + getRegistTime() + "'" +
            ", wxOpenId='" + getWxOpenId() + "'" +
            ", wxNickname='" + getWxNickname() + "'" +
            ", wxHeader='" + getWxHeader() + "'" +
            ", firstTotime='" + getFirstTotime() + "'" +
            ", firstBuyclass='" + getFirstBuyclass() + "'" +
            ", recentlySignin='" + getRecentlySignin() + "'" +
            ", experience='" + getExperience() + "'" +
            "}";
    }
}
