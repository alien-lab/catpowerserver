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
    private int weekDay;

    @ManyToOne
    private Coach coach;

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

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }
}
