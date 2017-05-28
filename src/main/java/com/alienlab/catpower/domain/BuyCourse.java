package com.alienlab.catpower.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BuyCourse.
 */
@Entity
@Table(name = "buy_course")
public class BuyCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_way")
    private String paymentWay;

    @Column(name = "payment_account")
    private Float paymentAccount;

    @Column(name = "buy_time")
    private ZonedDateTime buyTime;

    @Column(name = "status")
    private String status;

    @Column(name = "operator")
    private String operator;

    @Column(name = "operate_content")
    private String operateContent;

    @Column(name = "operate_time")
    private ZonedDateTime operateTime;

    @Column(name = "remain_class")
    private Long remainClass;

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

    public String getPaymentWay() {
        return paymentWay;
    }

    public BuyCourse paymentWay(String paymentWay) {
        this.paymentWay = paymentWay;
        return this;
    }

    public void setPaymentWay(String paymentWay) {
        this.paymentWay = paymentWay;
    }

    public Float getPaymentAccount() {
        return paymentAccount;
    }

    public BuyCourse paymentAccount(Float paymentAccount) {
        this.paymentAccount = paymentAccount;
        return this;
    }

    public void setPaymentAccount(Float paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public ZonedDateTime getBuyTime() {
        return buyTime;
    }

    public BuyCourse buyTime(ZonedDateTime buyTime) {
        this.buyTime = buyTime;
        return this;
    }

    public void setBuyTime(ZonedDateTime buyTime) {
        this.buyTime = buyTime;
    }

    public String getStatus() {
        return status;
    }

    public BuyCourse status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public BuyCourse operator(String operator) {
        this.operator = operator;
        return this;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperateContent() {
        return operateContent;
    }

    public BuyCourse operateContent(String operateContent) {
        this.operateContent = operateContent;
        return this;
    }

    public void setOperateContent(String operateContent) {
        this.operateContent = operateContent;
    }

    public ZonedDateTime getOperateTime() {
        return operateTime;
    }

    public BuyCourse operateTime(ZonedDateTime operateTime) {
        this.operateTime = operateTime;
        return this;
    }

    public void setOperateTime(ZonedDateTime operateTime) {
        this.operateTime = operateTime;
    }

    public Long getRemainClass() {
        return remainClass;
    }

    public BuyCourse remainClass(Long remainClass) {
        this.remainClass = remainClass;
        return this;
    }

    public void setRemainClass(Long remainClass) {
        this.remainClass = remainClass;
    }

    public Learner getLearner() {
        return learner;
    }

    public BuyCourse learner(Learner learner) {
        this.learner = learner;
        return this;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public Course getCourse() {
        return course;
    }

    public BuyCourse course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Coach getCoach() {
        return coach;
    }

    public BuyCourse coach(Coach coach) {
        this.coach = coach;
        return this;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BuyCourse buyCourse = (BuyCourse) o;
        if (buyCourse.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), buyCourse.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BuyCourse{" +
            "id=" + getId() +
            ", paymentWay='" + getPaymentWay() + "'" +
            ", paymentAccount='" + getPaymentAccount() + "'" +
            ", buyTime='" + getBuyTime() + "'" +
            ", status='" + getStatus() + "'" +
            ", operator='" + getOperator() + "'" +
            ", operateContent='" + getOperateContent() + "'" +
            ", operateTime='" + getOperateTime() + "'" +
            ", remainClass='" + getRemainClass() + "'" +
            "}";
    }
}
