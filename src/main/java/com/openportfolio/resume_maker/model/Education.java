package com.openportfolio.resume_maker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "educations")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String degree;

    @Column(nullable = false, length = 150)
    private String institution;

    @Column(length = 100)
    private String fieldOfStudy;

    @Column(length = 10)
    private String startYear;

    @Column(length = 10)
    private String endYear;

    @Column(length = 20)
    private String grade;

    @Column(length = 2000)
    private String description;

    public Education() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Education e = new Education();

        public Builder user(User v) {
            e.user = v;
            return this;
        }

        public Builder degree(String v) {
            e.degree = v;
            return this;
        }

        public Builder institution(String v) {
            e.institution = v;
            return this;
        }

        public Builder fieldOfStudy(String v) {
            e.fieldOfStudy = v;
            return this;
        }

        public Builder startYear(String v) {
            e.startYear = v;
            return this;
        }

        public Builder endYear(String v) {
            e.endYear = v;
            return this;
        }

        public Builder grade(String v) {
            e.grade = v;
            return this;
        }

        public Builder description(String v) {
            e.description = v;
            return this;
        }

        public Education build() {
            return e;
        }
    }
}
