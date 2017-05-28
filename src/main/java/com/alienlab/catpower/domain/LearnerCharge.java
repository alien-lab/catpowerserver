package com.alienlab.catpower.domain;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "learner_charge")
public class LearnerCharge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "charge_time")
    private ZonedDateTime chargeTime;

    @Column(name = "buy_course_id")
    private String buyCourseId;

    @Column(name = "charge_people")
    private String chargePeople;

    @Column(name = "remain_number")
    private Long remainNumber;

    @ManyToOne
    private Learner learner;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Coach coach;

    @ManyToOne
    private CourseScheduling courseScheduling;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getChargeTime() {
        return chargeTime;
    }

    public LearnerCharge chargeTime(ZonedDateTime chargeTime) {
        this.chargeTime = chargeTime;
        return this;
    }

    public void setChargeTime(ZonedDateTime chargeTime) {
        this.chargeTime = chargeTime;
    }

    public String getBuyCourseId() {
        return buyCourseId;
    }

    public LearnerCharge buyCourseId(String buyCourseId) {
        this.buyCourseId = buyCourseId;
        return this;
    }

    public void setBuyCourseId(String buyCourseId) {
        this.buyCourseId = buyCourseId;
    }

    public String getChargePeople() {
        return chargePeople;
    }

    public LearnerCharge chargePeople(String chargePeople) {
        this.chargePeople = chargePeople;
        return this;
    }

    public void setChargePeople(String chargePeople) {
        this.chargePeople = chargePeople;
    }

    public Long getRemainNumber() {
        return remainNumber;
    }

    public LearnerCharge remainNumber(Long remainNumber) {
        this.remainNumber = remainNumber;
        return this;
    }

    public void setRemainNumber(Long remainNumber) {
        this.remainNumber = remainNumber;
    }

    public Learner getLearner() {
        return learner;
    }

    public LearnerCharge learner(Learner learner) {
        this.learner = learner;
        return this;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public Course getCourse() {
        return course;
    }

    public LearnerCharge course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Coach getCoach() {
        return coach;
    }

    public LearnerCharge coach(Coach coach) {
        this.coach = coach;
        return this;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public CourseScheduling getCourseScheduling() {
        return courseScheduling;
    }

    public LearnerCharge courseScheduling(CourseScheduling courseScheduling) {
        this.courseScheduling = courseScheduling;
        return this;
    }

    public void setCourseScheduling(CourseScheduling courseScheduling) {
        this.courseScheduling = courseScheduling;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LearnerCharge learnerCharge = (LearnerCharge) o;
        if (learnerCharge.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), learnerCharge.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LearnerCharge{" +
            "id=" + getId() +
            ", chargeTime='" + getChargeTime() + "'" +
            ", buyCourseId='" + getBuyCourseId() + "'" +
            ", chargePeople='" + getChargePeople() + "'" +
            ", remainNumber='" + getRemainNumber() + "'" +
            "}";
    }
}
