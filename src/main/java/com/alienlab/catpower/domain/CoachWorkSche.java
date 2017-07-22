package com.alienlab.catpower.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Created by 橘 on 2017/6/26.
 */
@Entity
@Table(name="coach_work_sche")
public class CoachWorkSche implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="work_date")
    private ZonedDateTime workDate;

    @Column(name="work_weekday")
    private int workWeekday;

    @ManyToOne
    private Coach coach;

    public CoachWorkSche() {
    }

    public CoachWorkSche(ZonedDateTime workDate, int workWeekday, Coach coach) {
        this.workDate = workDate;
        this.workWeekday = workWeekday;
        this.coach = coach;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getWorkDate() {
        return workDate;
    }

    public void setWorkDate(ZonedDateTime workDate) {
        this.workDate = workDate;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getWorkWeekday() {
        return workWeekday;
    }

    public void setWorkWeekday(int workWeekday) {
        this.workWeekday = workWeekday;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    @Override
    public String toString() {
        return "CoachWorkSche{" +
            "id=" + id +
            ", workDate=" + workDate +
            ", WorkWeekday=" + workWeekday +
            ", coach=" + coach +
            '}';
    }
}
