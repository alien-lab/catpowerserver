package com.alienlab.catpower.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * a learner appointment.
 */
@Entity
@Table(name = "learner_appointment")
public class LearnerAppointment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_date")
    private ZonedDateTime appointmentDate;

    @Column(name = "appointment_result")
    private String appointmentResult;

    @Column(name = "appointment_memo")
    private String appointmentMemo;

    @ManyToOne
    private BuyCourse buyCourse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(ZonedDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentResult() {
        return appointmentResult;
    }

    public void setAppointmentResult(String appointmentResult) {
        this.appointmentResult = appointmentResult;
    }

    public String getAppointmentMemo() {
        return appointmentMemo;
    }

    public void setAppointmentMemo(String appointmentMemo) {
        this.appointmentMemo = appointmentMemo;
    }

    public BuyCourse getBuyCourse() {
        return buyCourse;
    }

    public void setBuyCourse(BuyCourse buyCourse) {
        this.buyCourse = buyCourse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LearnerAppointment buyCourse = (LearnerAppointment) o;
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
        return "LearnerAppointment{" +
            "id=" + id +
            ", appointmentDate=" + appointmentDate +
            ", appointmentResult='" + appointmentResult + '\'' +
            ", appointmentMemo='" + appointmentMemo + '\'' +
            ", buyCourse=" + buyCourse +
            '}';
    }
}
