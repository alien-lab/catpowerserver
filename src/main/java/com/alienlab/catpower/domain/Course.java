package com.alienlab.catpower.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "course_introductions")
    private String courseIntroductions;

    @Column(name = "total_class_hour")
    private Integer totalClassHour;

    @Column(name = "course_prices")
    private Float coursePrices;

    @Column(name = "course_vipprices")
    private Float courseVipprices;

    @Column(name = "class_number")
    private Integer classNumber;

    @Column(name = "course_other_info")
    private String courseOtherInfo;

    @Column(name = "course_thumbnail")
    private String courseThumbnail;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private Set<CourseAtlas> pictures = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public Course courseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseIntroductions() {
        return courseIntroductions;
    }

    public Course courseIntroductions(String courseIntroductions) {
        this.courseIntroductions = courseIntroductions;
        return this;
    }

    public void setCourseIntroductions(String courseIntroductions) {
        this.courseIntroductions = courseIntroductions;
    }

    public Integer getTotalClassHour() {
        return totalClassHour;
    }

    public Course totalClassHour(Integer totalClassHour) {
        this.totalClassHour = totalClassHour;
        return this;
    }

    public void setTotalClassHour(Integer totalClassHour) {
        this.totalClassHour = totalClassHour;
    }

    public Float getCoursePrices() {
        return coursePrices;
    }

    public Course coursePrices(Float coursePrices) {
        this.coursePrices = coursePrices;
        return this;
    }

    public void setCoursePrices(Float coursePrices) {
        this.coursePrices = coursePrices;
    }

    public Float getCourseVipprices() {
        return courseVipprices;
    }

    public Course courseVipprices(Float courseVipprices) {
        this.courseVipprices = courseVipprices;
        return this;
    }

    public void setCourseVipprices(Float courseVipprices) {
        this.courseVipprices = courseVipprices;
    }

    public Integer getClassNumber() {
        return classNumber;
    }

    public Course classNumber(Integer classNumber) {
        this.classNumber = classNumber;
        return this;
    }

    public void setClassNumber(Integer classNumber) {
        this.classNumber = classNumber;
    }

    public String getCourseOtherInfo() {
        return courseOtherInfo;
    }

    public Course courseOtherInfo(String courseOtherInfo) {
        this.courseOtherInfo = courseOtherInfo;
        return this;
    }

    public void setCourseOtherInfo(String courseOtherInfo) {
        this.courseOtherInfo = courseOtherInfo;
    }

    public String getCourseThumbnail() {
        return courseThumbnail;
    }

    public Course courseThumbnail(String courseThumbnail) {
        this.courseThumbnail = courseThumbnail;
        return this;
    }

    public void setCourseThumbnail(String courseThumbnail) {
        this.courseThumbnail = courseThumbnail;
    }

    public Set<CourseAtlas> getPictures() {
        return pictures;
    }

    public Course pictures(Set<CourseAtlas> courseAtlases) {
        this.pictures = courseAtlases;
        return this;
    }

    public Course addPictures(CourseAtlas courseAtlas) {
        this.pictures.add(courseAtlas);
        courseAtlas.setCourse(this);
        return this;
    }

    public Course removePictures(CourseAtlas courseAtlas) {
        this.pictures.remove(courseAtlas);
        courseAtlas.setCourse(null);
        return this;
    }

    public void setPictures(Set<CourseAtlas> courseAtlases) {
        this.pictures = courseAtlases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Course course = (Course) o;
        if (course.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), course.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", courseName='" + getCourseName() + "'" +
            ", courseIntroductions='" + getCourseIntroductions() + "'" +
            ", totalClassHour='" + getTotalClassHour() + "'" +
            ", coursePrices='" + getCoursePrices() + "'" +
            ", courseVipprices='" + getCourseVipprices() + "'" +
            ", classNumber='" + getClassNumber() + "'" +
            ", courseOtherInfo='" + getCourseOtherInfo() + "'" +
            ", courseThumbnail='" + getCourseThumbnail() + "'" +
            "}";
    }
}
