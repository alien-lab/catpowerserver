package com.alienlab.catpower.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CoachEvaluate.
 */
@Entity
@Table(name = "coach_evaluate")
public class CoachEvaluate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_attitude")
    private Long serviceAttitude;

    @Column(name = "speciality")
    private Long speciality;

    @Column(name = "jhi_like")
    private Long like;

    @Column(name = "complain")
    private String complain;

    @Column(name = "evaluation")
    private Long evaluation;

    @ManyToOne
    private Learner learner;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Coach coach;

    @ManyToOne
    private LearnerCharge learnerCharge;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceAttitude() {
        return serviceAttitude;
    }

    public CoachEvaluate serviceAttitude(Long serviceAttitude) {
        this.serviceAttitude = serviceAttitude;
        return this;
    }

    public void setServiceAttitude(Long serviceAttitude) {
        this.serviceAttitude = serviceAttitude;
    }

    public Long getSpeciality() {
        return speciality;
    }

    public CoachEvaluate speciality(Long speciality) {
        this.speciality = speciality;
        return this;
    }

    public void setSpeciality(Long speciality) {
        this.speciality = speciality;
    }

    public Long getLike() {
        return like;
    }

    public CoachEvaluate like(Long like) {
        this.like = like;
        return this;
    }

    public void setLike(Long like) {
        this.like = like;
    }

    public String getComplain() {
        return complain;
    }

    public CoachEvaluate complain(String complain) {
        this.complain = complain;
        return this;
    }

    public void setComplain(String complain) {
        this.complain = complain;
    }

    public Long getEvaluation() {
        return evaluation;
    }

    public CoachEvaluate evaluation(Long evaluation) {
        this.evaluation = evaluation;
        return this;
    }

    public void setEvaluation(Long evaluation) {
        this.evaluation = evaluation;
    }

    public Learner getLearner() {
        return learner;
    }

    public CoachEvaluate learner(Learner learner) {
        this.learner = learner;
        return this;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public Course getCourse() {
        return course;
    }

    public CoachEvaluate course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Coach getCoach() {
        return coach;
    }

    public CoachEvaluate coach(Coach coach) {
        this.coach = coach;
        return this;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public LearnerCharge getLearnerCharge() {
        return learnerCharge;
    }

    public CoachEvaluate learnerCharge(LearnerCharge learnerCharge) {
        this.learnerCharge = learnerCharge;
        return this;
    }

    public void setLearnerCharge(LearnerCharge learnerCharge) {
        this.learnerCharge = learnerCharge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CoachEvaluate coachEvaluate = (CoachEvaluate) o;
        if (coachEvaluate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), coachEvaluate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CoachEvaluate{" +
            "id=" + getId() +
            ", serviceAttitude='" + getServiceAttitude() + "'" +
            ", speciality='" + getSpeciality() + "'" +
            ", like='" + getLike() + "'" +
            ", complain='" + getComplain() + "'" +
            ", evaluation='" + getEvaluation() + "'" +
            "}";
    }
}
