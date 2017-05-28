package com.alienlab.catpower.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CourseAtlas.
 */
@Entity
@Table(name = "course_atlas")
public class CourseAtlas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "picture")
    private String picture;

    @ManyToOne
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public CourseAtlas picture(String picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Course getCourse() {
        return course;
    }

    public CourseAtlas course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CourseAtlas courseAtlas = (CourseAtlas) o;
        if (courseAtlas.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courseAtlas.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourseAtlas{" +
            "id=" + getId() +
            ", picture='" + getPicture() + "'" +
            "}";
    }
}
