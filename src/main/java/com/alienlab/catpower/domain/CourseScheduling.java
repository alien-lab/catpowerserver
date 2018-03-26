package com.alienlab.catpower.domain;


import com.alienlab.catpower.web.wechat.bean.entity.QrInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A CourseScheduling.
 */
@Entity
@Table(name = "course_scheduling")
public class CourseScheduling implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name="qr_code")
    private QrInfo qrCode;

    @Column(name = "sign_in_count")
    private Long signInCount;

    @Column(name = "appointdate")
    private String appointDate;
    @Column(name = "appointtime")
    private String appointTime;
    @Column(name = "appointtype")
    private String appointType;



    @ManyToOne
    private Learner learner;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Coach coach;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public CourseScheduling startTime(ZonedDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public CourseScheduling endTime(ZonedDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public CourseScheduling status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public QrInfo getQrCode() {
        return qrCode;
    }

    public CourseScheduling qrCode(QrInfo qrCode) {
        this.qrCode = qrCode;
        return this;
    }

    public void setQrCode(QrInfo qrCode) {
        this.qrCode = qrCode;
    }

    public Long getSignInCount() {
        return signInCount;
    }

    public CourseScheduling signInCount(Long signInCount) {
        this.signInCount = signInCount;
        return this;
    }

    public void setSignInCount(Long signInCount) {
        this.signInCount = signInCount;
    }

    public Course getCourse() {
        return course;
    }

    public CourseScheduling course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Coach getCoach() {
        return coach;
    }

    public CourseScheduling coach(Coach coach) {
        this.coach = coach;
        return this;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public String getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(String appointDate) {
        this.appointDate = appointDate;
    }

    public String getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }

    public String getAppointType() {
        return appointType;
    }

    public void setAppointType(String appointType) {
        this.appointType = appointType;
    }

    public Learner getLearner() {
        return learner;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CourseScheduling courseScheduling = (CourseScheduling) o;
        if (courseScheduling.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courseScheduling.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourseScheduling{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", status='" + getStatus() + "'" +
            ", qrCode='" + getQrCode() + "'" +
            ", signInCount='" + getSignInCount() + "'" +
            "}";
    }
}
