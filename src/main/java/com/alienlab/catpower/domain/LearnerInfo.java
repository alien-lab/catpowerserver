package com.alienlab.catpower.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A LearnerInfo.
 */
@Entity
@Table(name = "learner_info")
public class LearnerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_time")
    private ZonedDateTime time;

    @Column(name = "exercise_data")
    private String exerciseData;

    @Column(name = "bodytest_data")
    private String bodytestData;

    @Column(name = "coach_advice")
    private String coachAdvice;

    @ManyToOne
    private Learner learner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public LearnerInfo time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public String getExerciseData() {
        return exerciseData;
    }

    public LearnerInfo exerciseData(String exerciseData) {
        this.exerciseData = exerciseData;
        return this;
    }

    public void setExerciseData(String exerciseData) {
        this.exerciseData = exerciseData;
    }

    public String getBodytestData() {
        return bodytestData;
    }

    public LearnerInfo bodytestData(String bodytestData) {
        this.bodytestData = bodytestData;
        return this;
    }

    public void setBodytestData(String bodytestData) {
        this.bodytestData = bodytestData;
    }

    public String getCoachAdvice() {
        return coachAdvice;
    }

    public LearnerInfo coachAdvice(String coachAdvice) {
        this.coachAdvice = coachAdvice;
        return this;
    }

    public void setCoachAdvice(String coachAdvice) {
        this.coachAdvice = coachAdvice;
    }

    public Learner getLearner() {
        return learner;
    }

    public LearnerInfo learner(Learner learner) {
        this.learner = learner;
        return this;
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
        LearnerInfo learnerInfo = (LearnerInfo) o;
        if (learnerInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), learnerInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LearnerInfo{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", exerciseData='" + getExerciseData() + "'" +
            ", bodytestData='" + getBodytestData() + "'" +
            ", coachAdvice='" + getCoachAdvice() + "'" +
            "}";
    }
}
